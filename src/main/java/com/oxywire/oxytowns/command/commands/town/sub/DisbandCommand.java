package com.oxywire.oxytowns.command.commands.town.sub;

import cloud.commandframework.annotations.CommandDescription;
import cloud.commandframework.annotations.CommandMethod;
import cloud.commandframework.annotations.Hidden;
import com.oxywire.oxytowns.cache.TownCache;
import com.oxywire.oxytowns.command.annotation.AcceptConfirmation;
import com.oxywire.oxytowns.command.annotation.CreateConfirmation;
import com.oxywire.oxytowns.command.annotation.MustBeInTown;
import com.oxywire.oxytowns.command.annotation.SendersTown;
import com.oxywire.oxytowns.config.Messages;
import com.oxywire.oxytowns.entities.impl.town.Town;
import com.oxywire.oxytowns.events.TownDisbandEvent;
import com.oxywire.oxytowns.events.TownPlayerLeaveEvent;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public final class DisbandCommand {

    private final TownCache townCache;

    public DisbandCommand(final TownCache townCache) {
        this.townCache = townCache;
    }

    @CommandMethod("town|t disband|delete")
    @CommandDescription("Disband your town")
    @CreateConfirmation("player_town_disband")
    @MustBeInTown
    public void onDisband(final Player sender, final @SendersTown Town town) {
        final Messages messages = Messages.get();
        if (!town.getOwner().equals(sender.getUniqueId())) {
            messages.getTown().getNotOwner().send(sender);
            return;
        }
        messages.getTown().getTownDisbandConfirm().send(sender);
    }

    @CommandMethod("town|t disband|delete confirm")
    @CommandDescription("Confirm the disbanding of your town")
    @AcceptConfirmation("player_town_disband")
    @Hidden
    @MustBeInTown
    public void onDisbandConfirm(Player sender, @SendersTown Town town) {
        final Messages messages = Messages.get();
        if (!town.getOwner().equals(sender.getUniqueId())) {
            messages.getTown().getNotOwner().send(sender);
            return;
        }

        TownDisbandEvent disbandEvent = new TownDisbandEvent(town);
        if (!disbandEvent.callEvent())
            return;

        for (UUID player : town.getOwnerAndMembers()) {
            TownPlayerLeaveEvent leaveEvent = new TownPlayerLeaveEvent(town, Bukkit.getOfflinePlayer(player), TownPlayerLeaveEvent.Reason.DISBANDED);
            Bukkit.getPluginManager().callEvent(leaveEvent);
        }

        messages.getTown().getTownDisbandSuccess().send(Bukkit.getServer(), Placeholder.unparsed("town", town.getName()));
        this.townCache.deleteTown(town);
    }
}
