package com.oxywire.oxytowns.command.commands.plot.sub;

import cloud.commandframework.annotations.Argument;
import cloud.commandframework.annotations.CommandDescription;
import cloud.commandframework.annotations.CommandMethod;
import cloud.commandframework.annotations.Regex;
import com.oxywire.oxytowns.cache.TownCache;
import com.oxywire.oxytowns.command.annotation.MustBeInTown;
import com.oxywire.oxytowns.command.annotation.SendersTown;
import com.oxywire.oxytowns.config.Messages;
import com.oxywire.oxytowns.entities.impl.plot.Plot;
import com.oxywire.oxytowns.entities.impl.town.Town;
import com.oxywire.oxytowns.entities.types.perms.Permission;
import com.oxywire.oxytowns.utils.TownUtils;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.entity.Player;

public final class RenameCommand {

    private final TownCache townCache;

    public RenameCommand(final TownCache townCache) {
        this.townCache = townCache;
    }

    @CommandMethod("plot rename <name>")
    @CommandDescription("Rename a plot")
    @MustBeInTown
    public void onRename(final Player sender, final @SendersTown Town town, final @Argument("name") @Regex(TownUtils.VALID_NAME) String name) {
        final Messages messages = Messages.get();
        final Plot plot = town.getPlot(sender.getLocation());

        if (plot == null) {
            messages.getTown().getPlot().getIsNotPlot().send(sender);
            return;
        }

        if (!this.townCache.getTownByLocation(sender.getLocation()).equals(town)) {
            messages.getTown().getPlot().getNotClaimed().send(sender);
            return;
        }

        if (!town.hasPermission(sender.getUniqueId(), Permission.PLOTS_RENAME) && !plot.getAssignedMembers().contains(sender.getUniqueId())) {
            messages.getTown().getPlot().getRenameNoPermission().send(sender);
            return;
        }

        plot.setName(name);
        messages.getTown().getPlot().getRenameSuccess().send(sender, Placeholder.unparsed("name", name));
    }
}
