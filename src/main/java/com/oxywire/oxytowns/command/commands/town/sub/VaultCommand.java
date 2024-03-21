package com.oxywire.oxytowns.command.commands.town.sub;

import cloud.commandframework.annotations.Argument;
import cloud.commandframework.annotations.CommandDescription;
import cloud.commandframework.annotations.CommandMethod;
import com.oxywire.oxytowns.command.annotation.MustBeInTown;
import com.oxywire.oxytowns.command.annotation.SendersTown;
import com.oxywire.oxytowns.config.Messages;
import com.oxywire.oxytowns.entities.impl.town.Town;
import com.oxywire.oxytowns.entities.types.perms.Permission;
import com.oxywire.oxytowns.menu.town.VaultSelectorMenu;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.Nullable;

public final class VaultCommand {

    @CommandMethod("town|t vault|v [number]")
    @CommandDescription("View your town's vault")
    @MustBeInTown
    public void onVault(final Player sender, final @SendersTown Town town, final @Argument("number") @Nullable Integer vaultNumber) {
        final Messages messages = Messages.get();
        if (!town.hasPermission(sender.getUniqueId(), Permission.VAULT)) {
            messages.getTown().getVault().getNoOpenPermission().send(sender);
            return;
        }

        if (vaultNumber == null) {
            VaultSelectorMenu.open(sender, town);
            return;
        }

        try {
            town.getVaults().get(vaultNumber - 1).open(sender);
        } catch (Exception ex) {
            messages.getTown().getVault().getInvalidNumber().send(sender);
        }
    }
}
