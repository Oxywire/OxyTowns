package com.oxywire.oxytowns.command.commands.town.sub;

import cloud.commandframework.annotations.Argument;
import cloud.commandframework.annotations.CommandDescription;
import cloud.commandframework.annotations.CommandMethod;
import com.oxywire.oxytowns.cache.TownCache;
import com.oxywire.oxytowns.config.Messages;
import com.oxywire.oxytowns.entities.impl.town.Town;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.Nullable;


public final class HereCommand {

    private final TownCache townCache;

    public HereCommand(final TownCache townCache) {
        this.townCache = townCache;
    }

    @CommandMethod("town|t here")
    @CommandDescription("View information about a town")
    public void onInfo(final Player sender) {
        onHere(sender, null);
    }

    @CommandMethod("town|t info [name]")
    @CommandDescription("View information about a town")
    public void onHere(final Player sender, final @Argument("name") @Nullable Town name) {
        final Messages messages = Messages.get();
        final Town foundTown = name == null ? this.townCache.getTownByLocation(sender.getLocation()) : name;

        if (foundTown == null) {
            messages.getTown().getNotValidClaim().send(sender);
            return;
        }

        messages.getTown().getViewTownChunk().send(sender, foundTown.getPlaceholders());
    }
}
