package com.oxywire.oxytowns.command.commands.admin.town;

import cloud.commandframework.annotations.Argument;
import cloud.commandframework.annotations.CommandDescription;
import cloud.commandframework.annotations.CommandMethod;
import cloud.commandframework.annotations.CommandPermission;
import com.oxywire.oxytowns.entities.impl.town.Town;
import com.oxywire.oxytowns.menu.town.OutpostsMenu;
import org.bukkit.entity.Player;

public final class TownOutpostCommand {

    @CommandMethod("townadmin|ta town <town> outposts")
    @CommandDescription("Open the outposts menu for a town")
    @CommandPermission("oxytowns.admin")
    public void onOutpost(final Player sender, final @Argument("town") Town town) {
        OutpostsMenu.open(sender, town);
    }
}
