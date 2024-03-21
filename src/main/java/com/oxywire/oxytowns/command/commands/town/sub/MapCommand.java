package com.oxywire.oxytowns.command.commands.town.sub;

import cloud.commandframework.annotations.CommandDescription;
import cloud.commandframework.annotations.CommandMethod;
import com.oxywire.oxytowns.utils.MapUtils;
import org.bukkit.entity.Player;

public final class MapCommand {

    @CommandMethod("town|t map")
    @CommandDescription("View the map")
    public void onMap(Player sender) {
        MapUtils.display(sender);
    }

}
