package com.oxywire.oxytowns.command.parser;


import cloud.commandframework.arguments.parser.ArgumentParseResult;
import cloud.commandframework.arguments.parser.ArgumentParser;
import cloud.commandframework.bukkit.BukkitCommandContextKeys;
import cloud.commandframework.bukkit.parsers.OfflinePlayerArgument;
import cloud.commandframework.context.CommandContext;
import cloud.commandframework.exceptions.parsing.NoInputProvidedException;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public final class OfflinePlayerParser<C> implements ArgumentParser<C, OfflinePlayer> {

    @Override
    public @NonNull ArgumentParseResult<OfflinePlayer> parse(
        final @NonNull CommandContext<C> commandContext,
        final @NonNull Queue<String> inputQueue
    ) {
        final String input = inputQueue.peek();
        if (input == null) {
            return ArgumentParseResult.failure(new NoInputProvidedException(
                OfflinePlayerArgument.OfflinePlayerParser.class,
                commandContext
            ));
        }

        // This is the only change from the original parser
        OfflinePlayer player = Bukkit.getOfflinePlayerIfCached(input);
        if (player != null) {
            inputQueue.remove();
            return ArgumentParseResult.success(player);
        }

        player = Bukkit.getOfflinePlayer(input);
        if (!player.hasPlayedBefore()) {
            return ArgumentParseResult.failure(new OfflinePlayerArgument.OfflinePlayerParseException(input, commandContext));
        }

        inputQueue.remove();
        return ArgumentParseResult.success(player);
    }

    @Override
    public @NonNull List<@NonNull String> suggestions(
        final @NonNull CommandContext<C> commandContext,
        final @NonNull String input
    ) {
        List<String> output = new ArrayList<>();

        for (Player player : Bukkit.getOnlinePlayers()) {
            final CommandSender bukkit = commandContext.get(BukkitCommandContextKeys.BUKKIT_COMMAND_SENDER);
            if (bukkit instanceof Player && !((Player) bukkit).canSee(player)) {
                continue;
            }
            output.add(player.getName());
        }

        return output;
    }
}
