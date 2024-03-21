package com.oxywire.oxytowns.command.confirmation;

import cloud.commandframework.execution.postprocessor.CommandPostprocessingContext;
import cloud.commandframework.execution.postprocessor.CommandPostprocessor;
import cloud.commandframework.meta.CommandMeta;
import cloud.commandframework.services.types.ConsumerService;
import cloud.commandframework.types.tuples.Pair;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import org.bukkit.command.CommandSender;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public final class CommandConfirmationManager implements CommandPostprocessor<CommandSender> {

    public static final CommandMeta.Key<String> PROMPT_CONFIRMATION_KEY = CommandMeta.Key.of(
        String.class,
        "oxytowns:prompt_confirmation"
    );
    public static final CommandMeta.Key<String> ACCEPT_CONFIRMATION_KEY = CommandMeta.Key.of(
        String.class,
        "oxytowns:accept_confirmation"
    );

    public static final Table<CommandSender, String, Pair<CommandPostprocessingContext<CommandSender>, Long>> pendingCommands = HashBasedTable.create();

    public CommandConfirmationManager() {
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(
            () -> {
                final long now = System.currentTimeMillis();
                for (final CommandSender sender : pendingCommands.rowKeySet()) {
                    for (final String confirmation : pendingCommands.row(sender).keySet()) {
                        final Pair<CommandPostprocessingContext<CommandSender>, Long> pair = pendingCommands.get(sender, confirmation);
                        if (now - pair.getSecond() > 30_000) {
                            pendingCommands.remove(sender, confirmation);
                            // expiry notification?
                        }
                    }
                }
            },
            0,
            1,
            TimeUnit.SECONDS
        );
    }

    @Override
    public void accept(final CommandPostprocessingContext<CommandSender> context) {
        context.getCommand()
            .getCommandMeta()
            .get(PROMPT_CONFIRMATION_KEY)
            .ifPresent(value ->
                pendingCommands.put(
                    context.getCommandContext().getSender(),
                    value,
                    Pair.of(context, System.currentTimeMillis())
                )
            );

        context.getCommand().getCommandMeta().get(ACCEPT_CONFIRMATION_KEY).ifPresent(value -> {
            if (!pendingCommands.contains(context.getCommandContext().getSender(), value)) {
                ConsumerService.interrupt();
            }
        });
    }
}
