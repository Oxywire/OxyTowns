package com.oxywire.oxytowns.command.commands.admin;

import cloud.commandframework.annotations.Argument;
import cloud.commandframework.annotations.CommandMethod;
import cloud.commandframework.annotations.CommandPermission;
import cloud.commandframework.annotations.specifier.Greedy;
import com.oxywire.oxytowns.config.messaging.Message;
import com.oxywire.oxytowns.entities.impl.town.Town;
import org.bukkit.command.CommandSender;

public final class TownAdminCommand {

    @CommandMethod("townadmin|ta <town> debugplaceholders <msg>")
    @CommandPermission("oxytowns.admin")
    public void debugPlaceholders(final CommandSender sender, final @Argument Town town, final @Argument @Greedy String msg) {
        sender.sendMessage(Message.MINI_MESSAGE.deserialize(msg, town.getPlaceholders()));
    }
}
