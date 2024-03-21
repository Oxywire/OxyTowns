package com.oxywire.oxytowns.command.commands.admin.town;

import cloud.commandframework.annotations.Argument;
import cloud.commandframework.annotations.CommandDescription;
import cloud.commandframework.annotations.CommandMethod;
import cloud.commandframework.annotations.CommandPermission;
import cloud.commandframework.annotations.specifier.Range;
import com.oxywire.oxytowns.config.Messages;
import com.oxywire.oxytowns.entities.impl.town.Town;
import com.oxywire.oxytowns.menu.town.VaultSelectorMenu;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.Nullable;

public final class TownVaultCommand {

    @CommandMethod("townadmin|ta town <town> vault [number]")
    @CommandDescription("Open the vault menu for a town")
    @CommandPermission("oxytowns.admin")
    public void onVault(final Player player, final @Argument("town") Town town, final @Argument("number") @Range(min = "1") @Nullable Integer vaultNumber) {
        if (vaultNumber == null) {
            VaultSelectorMenu.open(player, town);
            return;
        }

        try {
            town.getVaults().get(vaultNumber - 1).open(player);
        } catch (Exception ex) {
            Messages.get().getTown().getVault().getInvalidNumber().send(player);
        }
    }
}
