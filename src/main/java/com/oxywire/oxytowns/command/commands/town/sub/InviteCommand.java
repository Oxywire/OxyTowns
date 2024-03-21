package com.oxywire.oxytowns.command.commands.town.sub;

import cloud.commandframework.annotations.Argument;
import cloud.commandframework.annotations.CommandDescription;
import cloud.commandframework.annotations.CommandMethod;
import com.oxywire.oxytowns.OxyTownsPlugin;
import com.oxywire.oxytowns.cache.TownCache;
import com.oxywire.oxytowns.command.annotation.MustBeInTown;
import com.oxywire.oxytowns.command.annotation.SendersTown;
import com.oxywire.oxytowns.config.Messages;
import com.oxywire.oxytowns.entities.impl.town.Town;
import com.oxywire.oxytowns.entities.types.Upgrade;
import com.oxywire.oxytowns.entities.types.perms.Permission;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.Optional;

public final class InviteCommand {

    private final TownCache townCache;

    public InviteCommand(final TownCache townCache) {
        this.townCache = townCache;
    }

    @CommandMethod("town|t invite <player>")
    @CommandDescription("Invite a player to your town")
    @MustBeInTown
    public void onInvite(final Player sender, final @SendersTown Town town, final @Argument(value = "player", suggestions = "town:invitable-members") Player player) {
        final Messages messages = Messages.get();

        if (!town.hasPermission(sender.getUniqueId(), Permission.INVITE)) {
            messages.getTown().getErrorNoPermissionInvite().send(sender);
            return;
        }

        if (town.getUpgradeValue(Upgrade.MEMBERS) <= (town.getMembers().size() + town.getInvitedPlayers().size())) {
            messages.getTown().getErrorMemberUpgradeNeeded().send(sender);
            return;
        }

        final Optional<Town> wantedTown = this.townCache.getTownByPlayer(player.getUniqueId());

        if (wantedTown.isPresent()) {
            messages.getTown().getErrorPlayerAlreadyInTown().send(sender, Placeholder.unparsed("player", player.getName()));
            return;
        }

        if (!town.invitePlayer(player.getUniqueId(), sender.getUniqueId())) {
            messages.getTown().getErrorPlayerAlreadyInvited().send(
                    sender,
                    Placeholder.unparsed("player", player.getName()),
                    Placeholder.unparsed("town", town.getName())
            );
            return;
        }

        messages.getTown().getBroadcastPlayerInvited().send(
                town,
                Placeholder.unparsed("player", player.getName()),
                Placeholder.unparsed("town", town.getName())
        );

        messages.getPlayer().getTownJoinConfirmation().send(
                player,
                Map.of("<town>", town.getName()),
                Placeholder.unparsed("player", sender.getName())
        );

        Bukkit.getScheduler().runTaskLater(OxyTownsPlugin.get(), () -> {
            if (town.removeInvite(player.getUniqueId())) {
                messages.getPlayer().getTownInvitedExpired().send(player, Placeholder.unparsed("town", town.getName()));
            }
        }, 30 * 20L);
    }
}
