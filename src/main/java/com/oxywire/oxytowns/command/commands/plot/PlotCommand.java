package com.oxywire.oxytowns.command.commands.plot;

import cloud.commandframework.annotations.CommandDescription;
import cloud.commandframework.annotations.CommandMethod;
import cloud.commandframework.annotations.suggestions.Suggestions;
import cloud.commandframework.context.CommandContext;
import com.oxywire.oxytowns.cache.TownCache;
import com.oxywire.oxytowns.command.annotation.MustBeInTown;
import com.oxywire.oxytowns.command.annotation.SendersTown;
import com.oxywire.oxytowns.config.Messages;
import com.oxywire.oxytowns.entities.impl.plot.Plot;
import com.oxywire.oxytowns.entities.impl.town.Town;
import com.oxywire.oxytowns.entities.types.PlotType;
import com.oxywire.oxytowns.entities.types.perms.Permission;
import com.oxywire.oxytowns.menu.plot.PlotMenu;
import com.oxywire.oxytowns.utils.ChunkPosition;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Optional;

public final class PlotCommand {

    private final TownCache townCache;

    public PlotCommand(final TownCache townCache) {
        this.townCache = townCache;
    }

    @CommandMethod("plot")
    @CommandDescription("View plot")
    @MustBeInTown
    public void onDefault(final Player sender, final @SendersTown Town sendersTown) {
        if (!sendersTown.hasClaimed(sender.getLocation())) {
            Messages.get().getTown().getChunkNotClaimed().send(sender);
            return;
        }

        final Plot plot = sendersTown.getPlot(sender.getLocation());
        if (plot == null) {
            if (!sendersTown.hasPermission(sender.getUniqueId(), Permission.PLOTS_MODIFY)) {
                Messages.get().getTown().getPlot().getManageNoPermission().send(sender);
                return;
            }

            final Plot newPlot = new Plot(PlotType.DEFAULT, ChunkPosition.chunkPosition(sender.getLocation()), "");
            sendersTown.claimPlot(newPlot);

            PlotMenu.open(sender, sendersTown, sendersTown.getPlot(sender.getLocation()));

            return;
        }

        if (!sendersTown.hasPermission(sender.getUniqueId(), Permission.PLOTS_MODIFY)) {
            Messages.get().getTown().getPlot().getManageNoPermission().send(sender);
            return;
        }

        PlotMenu.open(sender, sendersTown, sendersTown.getPlot(sender.getLocation()));
    }

    @Suggestions("plot:assignable-members")
    public List<String> setMayorMembers(final CommandContext<Player> context, final String input) {
        Optional<Town> optional = this.townCache.getTownByPlayer(context.getSender());
        if (optional.isPresent()) {
            Town town = optional.get();
            Plot plot = town.getPlot(context.getSender().getLocation());
            if (plot != null) {
                return town.getOwnerAndMembers().stream()
                        .filter(it -> !plot.getAssignedMembers().contains(it))
                        .map(Bukkit::getOfflinePlayer)
                        .map(OfflinePlayer::getName)
                        .toList();
            } else {
                return town.getOwnerAndMemberNames();
            }
        }
        return List.of();
    }

    @Suggestions("plot:evictable-members")
    public List<String> evictableMembers(final CommandContext<Player> context, final String input) {
        Optional<Town> optional = this.townCache.getTownByPlayer(context.getSender());
        if (optional.isPresent()) {
            Town town = optional.get();
            Plot plot = town.getPlot(context.getSender().getLocation());
            if (plot != null) {
                return plot.getAssignedMembers().stream()
                        .map(Bukkit::getOfflinePlayer)
                        .map(OfflinePlayer::getName)
                        .toList();
            }

        }
        return List.of();
    }
}
