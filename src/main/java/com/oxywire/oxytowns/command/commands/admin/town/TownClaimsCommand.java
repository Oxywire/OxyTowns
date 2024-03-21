package com.oxywire.oxytowns.command.commands.admin.town;

import cloud.commandframework.annotations.Argument;
import cloud.commandframework.annotations.CommandDescription;
import cloud.commandframework.annotations.CommandMethod;
import cloud.commandframework.annotations.CommandPermission;
import com.oxywire.oxytowns.entities.impl.town.Town;
import com.oxywire.oxytowns.menu.town.ClaimsMenu;
import org.bukkit.entity.Player;

public final class TownClaimsCommand {

    @CommandMethod("townadmin|ta town <town> claims")
    @CommandDescription("View the town's claim list")
    @CommandPermission("oxytowns.admin")
    public void onClaims(final Player sender, final @Argument("town") Town town) {
        ClaimsMenu.open(sender, town);
    }
}
