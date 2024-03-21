package com.oxywire.oxytowns.command.commands.town.sub;

import cloud.commandframework.annotations.CommandDescription;
import cloud.commandframework.annotations.CommandMethod;
import com.oxywire.oxytowns.command.annotation.MustBeInTown;
import com.oxywire.oxytowns.command.annotation.SendersTown;
import com.oxywire.oxytowns.config.Messages;
import com.oxywire.oxytowns.entities.impl.town.Town;
import com.oxywire.oxytowns.entities.types.perms.Permission;
import org.bukkit.entity.Player;

public final class SetSpawnCommand {

    @CommandMethod("town|t setspawn")
    @CommandDescription("Set your town's spawn")
    @MustBeInTown
    public void onSetSpawn(final Player sender, final @SendersTown Town town) {
        final Messages messages = Messages.get();
        if (!town.hasPermission(sender.getUniqueId(), Permission.SPAWN)) {
            messages.getTown().getNoPermissionSpawnSet().send(sender);
            return;
        }

        if (!town.hasClaimed(sender.getLocation())) {
            messages.getTown().getChunkNotClaimed().send(sender);
            return;
        }

        town.setHome(sender.getLocation());
        messages.getTown().getSpawnSet().send(sender);
    }
}
