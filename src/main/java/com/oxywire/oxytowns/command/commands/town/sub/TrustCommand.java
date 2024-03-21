package com.oxywire.oxytowns.command.commands.town.sub;

import cloud.commandframework.annotations.Argument;
import cloud.commandframework.annotations.CommandDescription;
import cloud.commandframework.annotations.CommandMethod;
import cloud.commandframework.annotations.specifier.Greedy;
import com.oxywire.oxytowns.command.annotation.MustBeInTown;
import com.oxywire.oxytowns.command.annotation.SendersTown;
import com.oxywire.oxytowns.config.Messages;
import com.oxywire.oxytowns.entities.impl.TrustedEntry;
import com.oxywire.oxytowns.entities.impl.town.Town;
import com.oxywire.oxytowns.entities.types.perms.Permission;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.Date;
import java.util.UUID;

public final class TrustCommand {

    @CommandMethod("town|t trust <target> [reason]")
    @CommandDescription("Trust a player in your town")
    @MustBeInTown
    public void onTrust(final Player sender, final @SendersTown Town town, final @Argument OfflinePlayer target, final @Greedy @Argument(defaultValue = "No reason") String reason) {
        final Messages messages = Messages.get();
        final UUID uuid = target.getUniqueId();

        if (town.isMemberOrOwner(uuid)) {
            messages.getTown().getCantTrustThem().send(sender, Placeholder.unparsed("player", target.getName()));
            return;
        }
        if (!town.hasPermission(sender.getUniqueId(), Permission.TRUST)) {
            messages.getTown().getNoPermissionTrust().send(sender);
            return;
        }

        town.getTrusted().add(new TrustedEntry(sender.getUniqueId(), uuid, new Date(), reason));
        messages.getTown().getTrustAdded().send(sender, Placeholder.unparsed("player", target.getName()));

        final Player targetAudience = target.getPlayer();
        if (targetAudience != null) {
            messages.getPlayer().getYouWereTrusted().send(
                targetAudience,
                Placeholder.unparsed("player", sender.getName()),
                Placeholder.unparsed("town", town.getName())
            );
        }
    }

    @CommandMethod("town|t untrust <target>")
    @CommandDescription("Untrust a player in your town")
    @MustBeInTown
    public void onUnTrust(final Player sender, final @SendersTown Town town, final @Argument OfflinePlayer target) {
        if (!town.hasPermission(sender.getUniqueId(), Permission.TRUST)) {
            Messages.get().getTown().getNoPermissionTrust().send(sender);
            return;
        }

        town.getTrusted().removeIf(it -> it.getUser().equals(target.getUniqueId()));
        Messages.get().getTown().getTrustRemoved().send(sender, Placeholder.unparsed("player", target.getName()));

        final Player targetAudience = target.getPlayer();
        if (targetAudience != null) {
            Messages.get().getPlayer().getYouWereUntrusted().send(
                targetAudience,
                Placeholder.unparsed("player", sender.getName()),
                Placeholder.unparsed("town", town.getName())
            );
        }
    }
}
