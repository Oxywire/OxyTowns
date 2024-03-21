package com.oxywire.oxytowns.command.commands.town.sub;

import cloud.commandframework.annotations.Argument;
import cloud.commandframework.annotations.CommandDescription;
import cloud.commandframework.annotations.CommandMethod;
import cloud.commandframework.annotations.Hidden;
import com.oxywire.oxytowns.command.annotation.AcceptConfirmation;
import com.oxywire.oxytowns.command.annotation.CreateConfirmation;
import com.oxywire.oxytowns.command.annotation.MustBeInTown;
import com.oxywire.oxytowns.command.annotation.SendersTown;
import com.oxywire.oxytowns.config.Messages;
import com.oxywire.oxytowns.config.messaging.Message;
import com.oxywire.oxytowns.entities.impl.town.Town;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class SetMayorCommand {

    @CommandMethod("town|t setmayor <member>")
    @CommandDescription("Transfer mayorship of your town")
    @CreateConfirmation("player_town_transfer_mayor")
    @MustBeInTown
    private void setMayor(final Player sender, final @Argument(value = "member", suggestions = "town:set-mayor-members") OfflinePlayer member, final @SendersTown Town town) {
        final Messages messages = Messages.get();
        if (!town.getOwner().equals(sender.getUniqueId())) {
            messages.getTown().getNotOwner().send(sender);
            return;
        }

        if (town.getOwner().equals(member.getUniqueId())) {
            messages.getTown().getAlreadyMayor().send(sender);
            return;
        }

        if (!town.isMember(member.getUniqueId())) {
            messages.getTown().getNotMember().send(sender, Placeholder.unparsed("player", member.getName()));
            return;
        }

        sender.sendMessage(
            Message.MINI_MESSAGE.deserialize(
                messages.getTown().getTownTransferMayorConfirm().getMessage().replace("<player>", member.getName()),
                Placeholder.unparsed("player", sender.getName())
            )
        );
    }

    @CommandMethod("town|t setmayor <member> confirm")
    @CommandDescription("Confirm transferring mayorship of your town")
    @AcceptConfirmation("player_town_transfer_mayor")
    @MustBeInTown
    @Hidden
    private void setMayorConfirm(final Player sender, final @Argument("member") OfflinePlayer member, final @SendersTown Town town) {
        town.transferMayor(member.getUniqueId());
        Messages messages = Messages.get();
        messages.getTown().getTownTransferMayorSuccess().send(
            town,
            Placeholder.unparsed("player", member.getName()),
            TagResolver.resolver(town.getPlaceholders())
        );

        if (member.isOnline()) {
            messages.getPlayer().getYouAreNowTheMayor().send(member.getPlayer(), TagResolver.resolver(town.getPlaceholders()));
        }
    }
}
