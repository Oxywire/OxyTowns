package com.oxywire.oxytowns.command.commands.admin.town;

import cloud.commandframework.annotations.Argument;
import cloud.commandframework.annotations.CommandDescription;
import cloud.commandframework.annotations.CommandMethod;
import cloud.commandframework.annotations.CommandPermission;
import com.oxywire.oxytowns.cache.TownCache;
import com.oxywire.oxytowns.config.Messages;
import com.oxywire.oxytowns.entities.impl.town.Town;
import com.oxywire.oxytowns.events.TownPlayerLeaveEvent;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

import java.util.Optional;

public final class TownKickPlayerCommand {

    private final TownCache townCache;

    public TownKickPlayerCommand(final TownCache townCache) {
        this.townCache = townCache;
    }

    @CommandMethod("townadmin|ta town remove <player>")
    @CommandDescription("Force remove a player from a town")
    @CommandPermission("oxytowns.admin")
    public void onTownRemove(final CommandSender sender, final @Argument("player") OfflinePlayer member) {
        final Messages messages = Messages.get();
        final Optional<Town> wantedTown = this.townCache.getTownByPlayer(member.getUniqueId());

        if (wantedTown.isEmpty()) {
            messages.getAdmin().getTown().getPlayerNotInTown().send(sender);
            return;
        }

        final Town town = wantedTown.get();

        if (member.getUniqueId().equals(town.getOwner())) {
            messages.getAdmin().getTown().getPlayerIsMayor().send(sender);
            return;
        }

        TownPlayerLeaveEvent leaveEvent = new TownPlayerLeaveEvent(town, member, TownPlayerLeaveEvent.Reason.KICKED);
        Bukkit.getPluginManager().callEvent(leaveEvent);
        if (leaveEvent.isCancelled())
            return;

        town.handleAdminLeave(member);
        messages.getAdmin().getTown().getPlayerForceRemoved().send(
            sender,
            Placeholder.unparsed("player", member.getName()),
            Placeholder.unparsed("town", town.getName())
        );
    }
}
