package com.oxywire.oxytowns.command;

import cloud.commandframework.Command;
import cloud.commandframework.annotations.AnnotationParser;
import cloud.commandframework.annotations.injection.ParameterInjector;
import cloud.commandframework.arguments.parser.ArgumentParser;
import cloud.commandframework.exceptions.ArgumentParseException;
import cloud.commandframework.exceptions.CommandExecutionException;
import cloud.commandframework.exceptions.InvalidCommandSenderException;
import cloud.commandframework.exceptions.InvalidSyntaxException;
import cloud.commandframework.exceptions.NoPermissionException;
import cloud.commandframework.execution.AsynchronousCommandExecutionCoordinator;
import cloud.commandframework.execution.postprocessor.CommandPostprocessor;
import cloud.commandframework.meta.SimpleCommandMeta;
import cloud.commandframework.paper.PaperCommandManager;
import com.google.common.reflect.ClassPath;
import com.oxywire.oxytowns.command.parser.OfflinePlayerParser;
import com.oxywire.oxytowns.config.Messages;
import io.leangen.geantyref.TypeToken;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.util.ComponentMessageThrowable;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class CommandManager extends PaperCommandManager<CommandSender> {

    private final AnnotationParser<CommandSender> annotationParser;

    public CommandManager(final Plugin owningPlugin) throws Exception {
        super(
            owningPlugin,
            AsynchronousCommandExecutionCoordinator.simpleCoordinator(), // TODO: This isn't actually the async command executor. It's fine, but figure out if it's safe to use
            Function.identity(),
            Function.identity()
        );

        this.annotationParser = new AnnotationParser<>(this, CommandSender.class, params -> SimpleCommandMeta.empty());

        registerAsynchronousCompletions();
        registerBrigadier();

        setSetting(ManagerSettings.ALLOW_UNSAFE_REGISTRATION, true);
        setSetting(ManagerSettings.OVERRIDE_EXISTING_COMMANDS, true);

        registerExceptionHandler(ArgumentParseException.class, (c, e) -> {
            final Component msg = Optional.ofNullable(ComponentMessageThrowable.getOrConvertMessage(e))
                .orElseGet(() -> ComponentMessageThrowable.getOrConvertMessage(e.getCause()));
            Messages.get().getCommandFeedback().getArgumentParse().send(c, Placeholder.component("value", msg == null ? Component.text("null") : msg));
        });
        registerExceptionHandler(
            CommandExecutionException.class,
            (c, e) -> {
                Messages.get().getCommandFeedback().getCommandExecution().send(c);
                e.printStackTrace();
            }
        );
        registerExceptionHandler(
            InvalidCommandSenderException.class,
            (c, e) -> Messages.get().getCommandFeedback().getInvalidCommandSender().send(c, Placeholder.unparsed("value", e.getRequiredSender().getSimpleName()))
        );
        registerExceptionHandler(
            InvalidSyntaxException.class,
            (c, e) -> Messages.get().getCommandFeedback().getInvalidSyntax().send(c, Placeholder.unparsed("value", e.getCorrectSyntax()))
        );
        registerExceptionHandler(
            NoPermissionException.class,
            (c, e) -> Messages.get().getCommandFeedback().getNoPermission().send(c)
        );

        parserRegistry().registerParserSupplier(TypeToken.get(OfflinePlayer.class), parserParameters -> new OfflinePlayerParser<>());
    }

    public static CommandManager install(final Plugin plugin) {
        try {
            return new CommandManager(plugin);
        } catch (Exception e) {
            throw new RuntimeException("Failed to install command manager", e);
        }
    }

    public CommandManager withCommands(final Object... commands) {
        for (final Object command : commands) {
            this.annotationParser.parse(command);
        }
        return this;
    }

    public CommandManager withCommands(final String pckg, final Object... injectables) {
        final Map<Class<?>, Object> injectableMap = Arrays.stream(injectables)
            .collect(Collectors.toMap(Object::getClass, Function.identity()));

        try {
            ClassPath.from(getOwningPlugin().getClass().getClassLoader())
                .getTopLevelClassesRecursive(pckg)
                .stream()
                .map(ClassPath.ClassInfo::load)
                .forEach(clazz -> {
                    try {
                        final Object[] instances = Arrays.stream(clazz.getDeclaredConstructors()[0].getParameters())
                            .map(Parameter::getType)
                            .map(type -> {
                                final Object injectable = injectableMap.get(type);
                                if (injectable != null) {
                                    return injectable;
                                }

                                throw new IllegalArgumentException("No injectable found for type " + type);
                            })
                            .toArray();

                        annotationParser.parse(clazz.getDeclaredConstructors()[0].newInstance(instances));
                    } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return this;
    }

    public <T> CommandManager withArgumentType(final Class<T> clazz, final ArgumentParser<CommandSender, T> argumentParser) {
        parserRegistry().registerParserSupplier(TypeToken.get(clazz), params -> argumentParser);
        return this;
    }

    public <T extends Annotation> CommandManager withBuilderModifier(final Class<T> clazz, final BiFunction<T, Command.Builder<CommandSender>, Command.Builder<CommandSender>> builderModifier) {
        this.annotationParser.registerBuilderModifier(clazz, builderModifier);
        return this;
    }

    public <T> CommandManager withInjector(final Class<T> clazz, final ParameterInjector<CommandSender, T> injector) {
        parameterInjectorRegistry().registerInjector(clazz, injector);
        return this;
    }

    public CommandManager withPostProcessor(final CommandPostprocessor<CommandSender> postProcessor) {
        registerCommandPostProcessor(postProcessor);
        return this;
    }
}
