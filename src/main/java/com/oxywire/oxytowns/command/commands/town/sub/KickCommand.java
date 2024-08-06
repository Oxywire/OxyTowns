package com.oxywire.oxytowns.command.commands.town.sub;

import cloud.commandframework.annotations.Argument;
import cloud.commandframework.annotations.CommandDescription;
import cloud.commandframework.annotations.CommandMethod;
import com.oxywire.oxytowns.command.annotation.MustBeInTown;
import com.oxywire.oxytowns.command.annotation.SendersTown;
import com.oxywire.oxytowns.config.Messages;
import com.oxywire.oxytowns.entities.impl.town.Town;
import com.oxywire.oxytowns.entities.types.perms.Permission;
import com.oxywire.oxytowns.events.TownPlayerLeaveEvent;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public final class KickCommand {

    @CommandMethod("town|t kick <player>")
    @CommandDescription("Kick a player from your town")
    @MustBeInTown
    public void onKick(final Player sender, final @SendersTown Town town, final @Argument(value = "player", suggestions = "town:members") String playerName) {
        final Messages messages = Messages.get();
        final OfflinePlayer wantedPlayer = Bukkit.getOfflinePlayer(playerName);

        if (wantedPlayer == null) {
            messages.getTown().getNoTown().send(sender, Placeholder.unparsed("player", playerName));
            return;
        }

        if (!town.hasPermission(sender.getUniqueId(), Permission.KICK)) {
            messages.getTown().getNoPermissionKick().send(sender);
            return;
        }

        if (sender.getUniqueId().equals(wantedPlayer.getUniqueId())) {
            messages.getPlayer().getCantKickSelf().send(sender);
            return;
        }

        if (town.getOwner().equals(wantedPlayer.getUniqueId())) {
            messages.getTown().getCantKickMayor().send(sender, Placeholder.unparsed("player", playerName));
            return;
        }

        if (!town.getMembers().containsKey(wantedPlayer.getUniqueId())) {
            messages.getTown().getNotMember().send(sender, Placeholder.unparsed("player", playerName));
            return;
        }

        TownPlayerLeaveEvent leaveEvent = new TownPlayerLeaveEvent(town, wantedPlayer, TownPlayerLeaveEvent.Reason.KICKED);
        Bukkit.getPluginManager().callEvent(leaveEvent);
        if (leaveEvent.isCancelled())
            return;

        town.handleKick(wantedPlayer);

        if (wantedPlayer.isOnline()) {
            messages.getPlayer().getKickedFromTown().send(
                wantedPlayer.getPlayer(),
                Placeholder.unparsed("town", town.getName()),
                Placeholder.unparsed("kicker", sender.getName())
            );
        }

        messages.getTown().getKickSuccess().send(town, Placeholder.unparsed("player", wantedPlayer.getName()), Placeholder.unparsed("kicker", sender.getName()));
    }
}
