package com.oxywire.oxytowns.command.commands.town.sub;

import cloud.commandframework.annotations.Argument;
import cloud.commandframework.annotations.CommandDescription;
import cloud.commandframework.annotations.CommandMethod;
import com.oxywire.oxytowns.command.annotation.MustBeInTown;
import com.oxywire.oxytowns.command.annotation.SendersTown;
import com.oxywire.oxytowns.config.Messages;
import com.oxywire.oxytowns.entities.impl.town.Town;
import com.oxywire.oxytowns.entities.types.perms.Permission;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public final class UnbanCommand {

    @CommandMethod("town|t unban <name>")
    @CommandDescription("Unban a player from your town")
    @MustBeInTown
    public void onUnban(final Player sender, final @SendersTown Town town, final @Argument(value = "name", suggestions = "town:unbannable-members") String user) {
        final Messages messages = Messages.get();
        if (!town.hasPermission(sender.getUniqueId(), Permission.PUNISH)) {
            messages.getTown().getUnban().getNoPermission().send(sender);
            return;
        }

        if (!town.getBanNames().stream().map(String::toLowerCase).toList().contains(user.toLowerCase())) {
            messages.getTown().getUnban().getNotBanned().send(sender);
            return;
        }

        final OfflinePlayer wantedPlayer = Bukkit.getOfflinePlayer(user);
        if (wantedPlayer == null) {
            messages.getTown().getInvalidName().send(sender);
            return;
        }

        town.removeBan(wantedPlayer);

        messages.getTown().getUnban().getBroadcastUnban().send(town, Placeholder.unparsed("player", wantedPlayer.getName()));
        if (!wantedPlayer.isOnline()) {
            return;
        }

        messages.getPlayer().getUnbanMessage().send(wantedPlayer.getPlayer(), Placeholder.unparsed("town", town.getName()));
    }
}
