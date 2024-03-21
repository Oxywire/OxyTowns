package com.oxywire.oxytowns.command.commands.town.sub;

import cloud.commandframework.annotations.CommandDescription;
import cloud.commandframework.annotations.CommandMethod;
import com.oxywire.oxytowns.command.annotation.MustBeInTown;
import com.oxywire.oxytowns.command.annotation.SendersTown;
import com.oxywire.oxytowns.entities.impl.town.Town;
import com.oxywire.oxytowns.menu.town.OutpostsMenu;
import org.bukkit.entity.Player;

public final class OutpostCommand {

    @CommandMethod("town|t outpost|outposts")
    @CommandDescription("View outposts")
    @MustBeInTown
    public void onOutpost(final Player sender, final @SendersTown Town town) {
        OutpostsMenu.open(sender, town);
    }
}
