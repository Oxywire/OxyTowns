package com.oxywire.oxytowns.command.commands.town.sub;

import cloud.commandframework.annotations.Argument;
import cloud.commandframework.annotations.CommandDescription;
import cloud.commandframework.annotations.CommandMethod;
import com.oxywire.oxytowns.cache.TownCache;
import com.oxywire.oxytowns.config.Messages;
import com.oxywire.oxytowns.entities.impl.town.Town;
import com.oxywire.oxytowns.entities.types.Upgrade;
import com.oxywire.oxytowns.entities.types.settings.Setting;
import com.oxywire.oxytowns.events.TownPlayerJoinEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Optional;

public final class JoinCommand {

    private final TownCache townCache;

    public JoinCommand(final TownCache townCache) {
        this.townCache = townCache;
    }

    @CommandMethod("town|t join <town>")
    @CommandDescription("Join a town")
    public void onJoin(final Player sender, final @Argument(value = "town", suggestions = "town:joinable_towns") String townName) {
        final Messages messages = Messages.get();
        final Optional<Town> senderTown = this.townCache.getTownByPlayer(sender.getUniqueId());

        if (senderTown.isPresent()) {
            messages.getPlayer().getAlreadyInTown().send(sender);
            return;
        }

        final Optional<Town> wantedTown = this.townCache.getTownByName(townName);

        if (wantedTown.isEmpty()) {
            messages.getPlayer().getInvalidTown().send(sender);
            return;
        }

        if (!wantedTown.get().getToggle(Setting.OPEN) && !wantedTown.get().getInvitedPlayers().contains(sender.getUniqueId())) {
            messages.getPlayer().getNotInvited().send(sender);
            return;
        }

        if (wantedTown.get().getUpgradeValue(Upgrade.MEMBERS) <= (wantedTown.get().getMembers().size() + wantedTown.get().getInvitedPlayers().size())) {
            messages.getPlayer().getFullTown().send(sender);
            return;
        }

        if (wantedTown.get().getOwnerAndMembers().contains(sender.getUniqueId())) {
            messages.getPlayer().getAlreadyInTown().send(sender);
            return;
        }

        TownPlayerJoinEvent joinEvent = new TownPlayerJoinEvent(wantedTown.get(), sender);
        Bukkit.getPluginManager().callEvent(joinEvent);
        if (joinEvent.isCancelled())
            return;
        wantedTown.get().handleJoin(sender);
    }
}
