package com.oxywire.oxytowns.command.argument;

import cloud.commandframework.arguments.parser.ArgumentParseResult;
import cloud.commandframework.arguments.parser.ArgumentParser;
import cloud.commandframework.arguments.standard.StringArgument;
import cloud.commandframework.context.CommandContext;
import cloud.commandframework.exceptions.parsing.NoInputProvidedException;
import org.bukkit.command.CommandSender;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.List;
import java.util.Queue;
import java.util.function.BiFunction;
import java.util.function.Function;

public class ArgumentType<T> implements ArgumentParser<CommandSender, T> {

    private final Function<String, @Nullable T> argumentMapper;
    private final Function<String, List<String>> suggestions;
    private final BiFunction<CommandContext<CommandSender>, String, Throwable> failureMapper;

    public ArgumentType(final Function<String, @Nullable T> argumentMapper, final Function<String, List<String>> suggestions) {
        this(
            argumentMapper,
            suggestions,
            (context, input) -> new StringArgument.StringParseException(input, StringArgument.StringMode.SINGLE, context)
        );
    }

    public ArgumentType(
        final Function<String, @Nullable T> argumentMapper,
        final Function<String, List<String>> suggestions,
        final BiFunction<CommandContext<CommandSender>, String, Throwable> failureMapper
    ) {
        this.argumentMapper = argumentMapper;
        this.suggestions = suggestions;
        this.failureMapper = failureMapper;
    }

    @Override
    public ArgumentParseResult<T> parse(final CommandContext<CommandSender> commandContext, final Queue<String> inputQueue) {
        final String input = inputQueue.peek();
        if (input == null) {
            return ArgumentParseResult.failure(new NoInputProvidedException(ArgumentType.class, commandContext));
        }

        final T parsed = argumentMapper.apply(input);
        if (parsed == null) {
            return ArgumentParseResult.failure(this.failureMapper.apply(commandContext, input));
        }

        inputQueue.remove();
        return ArgumentParseResult.success(parsed);
    }

    @Override
    public List<String> suggestions(final CommandContext<CommandSender> commandContext, final String input) {
        return suggestions.apply(input);
    }
}
