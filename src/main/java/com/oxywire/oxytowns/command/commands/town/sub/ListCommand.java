package com.oxywire.oxytowns.command.commands.town.sub;

import cloud.commandframework.annotations.CommandDescription;
import cloud.commandframework.annotations.CommandMethod;
import com.oxywire.oxytowns.cache.TownCache;
import com.oxywire.oxytowns.menu.town.TownsMenu;
import org.bukkit.entity.Player;

public final class ListCommand {

    private final TownCache townCache;

    public ListCommand(final TownCache townCache) {
        this.townCache = townCache;
    }

    @CommandMethod("town|t list")
    @CommandDescription("View towns")
    public void onList(final Player player) {
        TownsMenu.open(player, this.townCache);
    }
}
