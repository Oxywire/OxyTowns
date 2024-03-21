package com.oxywire.oxytowns.command.commands.admin.misc;

import cloud.commandframework.annotations.CommandDescription;
import cloud.commandframework.annotations.CommandMethod;
import cloud.commandframework.annotations.CommandPermission;
import com.oxywire.oxytowns.OxyTownsPlugin;
import org.bukkit.command.CommandSender;

public final class ReloadCommand {

    @CommandMethod("townadmin|ta reload")
    @CommandDescription("Reload the configs")
    @CommandPermission("oxytowns.admin.reload")
    public void onReload(final CommandSender sender) {
        OxyTownsPlugin.configManager.reload();
        sender.sendMessage("Reloaded");
    }
}
