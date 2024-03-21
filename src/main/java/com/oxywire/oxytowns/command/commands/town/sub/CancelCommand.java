package com.oxywire.oxytowns.command.commands.town.sub;

import cloud.commandframework.annotations.CommandMethod;
import cloud.commandframework.annotations.Hidden;
import com.oxywire.oxytowns.command.annotation.MustBeInTown;
import com.oxywire.oxytowns.command.annotation.SendersTown;
import com.oxywire.oxytowns.command.confirmation.CommandConfirmationManager;
import com.oxywire.oxytowns.config.Messages;
import com.oxywire.oxytowns.entities.impl.town.Town;
import org.bukkit.entity.Player;

public final class CancelCommand {

    @CommandMethod("town|t cancel")
    @Hidden
    @MustBeInTown
    public void onCancel(final Player sender, final @SendersTown Town town) {
        final Messages messages = Messages.get();

        if (CommandConfirmationManager.pendingCommands.rowKeySet().contains(sender)) {
            CommandConfirmationManager.pendingCommands.row(sender).clear();
            messages.getActionCancelled().send(sender);
            return;
        }

        if (town.removeInvite(sender.getUniqueId())) {
            messages.getActionCancelled().send(sender);
        }
    }
}
