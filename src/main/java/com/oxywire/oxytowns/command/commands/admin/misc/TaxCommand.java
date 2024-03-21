package com.oxywire.oxytowns.command.commands.admin.misc;

import cloud.commandframework.annotations.CommandDescription;
import cloud.commandframework.annotations.CommandMethod;
import cloud.commandframework.annotations.CommandPermission;
import com.oxywire.oxytowns.OxyTownsPlugin;
import org.bukkit.command.CommandSender;

public final class TaxCommand {

    private final OxyTownsPlugin plugin;

    public TaxCommand(final OxyTownsPlugin plugin) {
        this.plugin = plugin;
    }

    @CommandMethod("townadmin|ta upkeep")
    @CommandDescription("Force collect upkeep")
    @CommandPermission("oxytowns.admin.upkeep")
    public void onTax(final CommandSender sender) {
        this.plugin.getTaxSchedule().takeTownTax();
    }
}
