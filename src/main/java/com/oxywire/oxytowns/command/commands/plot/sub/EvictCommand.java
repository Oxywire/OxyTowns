package com.oxywire.oxytowns.command.commands.plot.sub;

import cloud.commandframework.annotations.Argument;
import cloud.commandframework.annotations.CommandDescription;
import cloud.commandframework.annotations.CommandMethod;
import com.oxywire.oxytowns.cache.TownCache;
import com.oxywire.oxytowns.command.annotation.MustBeInTown;
import com.oxywire.oxytowns.command.annotation.SendersTown;
import com.oxywire.oxytowns.config.Messages;
import com.oxywire.oxytowns.entities.impl.plot.Plot;
import com.oxywire.oxytowns.entities.impl.town.Town;
import com.oxywire.oxytowns.entities.types.perms.Permission;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;


public final class EvictCommand {

    private final TownCache townCache;

    public EvictCommand(final TownCache townCache) {
        this.townCache = townCache;
    }

    @CommandMethod("plot evict|kick <name>")
    @CommandDescription("Evict a member from a plot")
    @MustBeInTown
    public void onEvict(final Player sender, final @SendersTown Town town, final @Argument(value = "name", suggestions = "plot:evictable-members") String name) {
        final Messages messages = Messages.get();
        final Plot plot = town.getPlot(sender.getLocation());

        if (!town.hasClaimed(sender.getLocation()) || plot == null) {
            messages.getTown().getPlot().getNotClaimed().send(sender);
            return;
        }

        final OfflinePlayer foundPlayer = Bukkit.getOfflinePlayerIfCached(name);
        if (foundPlayer == null) {
            messages.getTown().getNotFound().send(sender, Placeholder.unparsed("player", name));
            return;
        }

        if (!town.hasPermission(sender.getUniqueId(), Permission.PLOTS_EVICT)) {
            messages.getTown().getPlot().getEvictedNoPermission().send(sender);
            return;
        }
        if (!plot.getAssignedMembers().contains(foundPlayer.getUniqueId())) {
            messages.getTown().getPlot().getMember().getNotMember().send(sender, Placeholder.unparsed("player", name));
            return;
        }

        plot.getAssignedMembers().remove(foundPlayer.getUniqueId());

        if (plot.getAssignedMembers().isEmpty()) {
            town.getPlayerPlots().remove(plot.getChunkPosition());
        }

        messages.getTown().getPlot().getEvicted().send(sender, Placeholder.unparsed("player", name));
    }
}
