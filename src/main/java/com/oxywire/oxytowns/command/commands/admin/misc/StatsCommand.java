package com.oxywire.oxytowns.command.commands.admin.misc;

import cloud.commandframework.annotations.CommandMethod;
import cloud.commandframework.annotations.CommandPermission;
import com.oxywire.oxytowns.cache.TownCache;
import com.oxywire.oxytowns.config.Messages;
import com.oxywire.oxytowns.entities.impl.town.Town;
import net.kyori.adventure.text.minimessage.tag.resolver.Formatter;
import org.bukkit.command.CommandSender;

import java.util.Set;

public final class StatsCommand {

    private final TownCache townCache;

    public StatsCommand(final TownCache townCache) {
        this.townCache = townCache;
    }


    @CommandMethod("townadmin|ta stats")
    @CommandPermission("oxytowns.admin")
    public void onStats(final CommandSender sender) {
        final Set<Town> towns = this.townCache.getTowns();
        Messages.get().getAdmin().getStats().send(
            sender,
            Formatter.number("towns", towns.size()),
            Formatter.number("members", towns.stream().mapToInt(it -> it.getMembers().size()).sum()),
            Formatter.number("chunks", towns.stream().mapToInt(it -> it.getClaimedChunks().size()).sum()),
            Formatter.number("outposts", towns.stream().mapToInt(it -> it.getOutpostChunks().size()).sum()),
            Formatter.number("money", towns.stream().mapToDouble(Town::getBankValue).sum()),
            Formatter.number("plots", towns.stream().mapToInt(it -> it.getPlayerPlots().size()).sum())
        );
    }
}
