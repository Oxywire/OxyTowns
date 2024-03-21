package com.oxywire.oxytowns.command.commands.plot.sub;

import cloud.commandframework.annotations.CommandDescription;
import cloud.commandframework.annotations.CommandMethod;
import com.oxywire.oxytowns.cache.TownCache;
import com.oxywire.oxytowns.config.Messages;
import com.oxywire.oxytowns.entities.impl.plot.Plot;
import com.oxywire.oxytowns.entities.impl.town.Town;
import org.bukkit.entity.Player;


public final class HereCommand {

    private final TownCache townCache;

    public HereCommand(final TownCache townCache) {
        this.townCache = townCache;
    }

    @CommandMethod("plot here")
    @CommandDescription("Get information about the plot you are standing on")
    public void onShow(Player sender) {
        final Messages messages = Messages.get();
        final Town foundTown = this.townCache.getTownByLocation(sender.getLocation());

        if (foundTown == null) {
            messages.getTown().getChunkNotClaimed().send(sender);
            return;
        }

        final Plot wantedPlot = foundTown.getPlot(sender.getLocation());

        if (wantedPlot == null) {
            messages.getTown().getPlot().getNotClaimed().send(sender);
            return;
        }

        messages.getTown().getPlot().getPlotInfo().send(sender, wantedPlot.getPlaceholders());
    }
}
