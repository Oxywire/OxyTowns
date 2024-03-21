package com.oxywire.oxytowns.command.commands.admin.misc;

import cloud.commandframework.annotations.CommandDescription;
import cloud.commandframework.annotations.CommandMethod;
import cloud.commandframework.annotations.CommandPermission;
import com.oxywire.oxytowns.cache.TownCache;
import com.oxywire.oxytowns.config.Messages;
import org.bukkit.entity.Player;

public final class BypassCommand {

    private final TownCache townCache;

    public BypassCommand(final TownCache townCache) {
        this.townCache = townCache;
    }

    @CommandMethod("townadmin|ta bypass")
    @CommandDescription("Toggle bypassing town perms")
    @CommandPermission("oxytowns.bypass.protection")
    public void onBypass(final Player sender) {
        final Messages messages = Messages.get();
        if (this.townCache.isBypassing(sender)) { // TODO: 11/2/2022 create TownCache#toggleBypass that utilizes the return of Set#add
            this.townCache.stopBypassing(sender);
            messages.getBypassOff().send(sender);
        } else {
            this.townCache.startBypassing(sender);
            messages.getBypassOn().send(sender);
        }
    }
}
