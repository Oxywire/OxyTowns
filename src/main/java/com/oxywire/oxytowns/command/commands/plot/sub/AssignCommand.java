package com.oxywire.oxytowns.command.commands.plot.sub;

import cloud.commandframework.annotations.Argument;
import cloud.commandframework.annotations.CommandDescription;
import cloud.commandframework.annotations.CommandMethod;
import com.oxywire.oxytowns.cache.TownCache;
import com.oxywire.oxytowns.command.annotation.MustBeInTown;
import com.oxywire.oxytowns.command.annotation.SendersTown;
import com.oxywire.oxytowns.config.Messages;
import com.oxywire.oxytowns.entities.impl.plot.Plot;
import com.oxywire.oxytowns.entities.impl.town.Town;
import com.oxywire.oxytowns.entities.types.perms.Permission;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public final class AssignCommand {

    private final TownCache townCache;

    public AssignCommand(final TownCache townCache) {
        this.townCache = townCache;
    }

    @CommandMethod("plot assign|add <name>")
    @CommandDescription("Assign a plot to a member")
    @MustBeInTown
    public void onAssign(final Player sender, final @SendersTown Town town, final @Argument(value = "name", suggestions = "plot:assignable-members") String name) {
        final Messages messages = Messages.get();
        final Plot foundPlot = town.getPlot(sender.getLocation());
        final Town foundTown = this.townCache.getTownByLocation(sender.getLocation());
        if (foundTown == null || !foundTown.equals(town)) {
            messages.getTown().getPlot().getNotClaimed().send(sender);
            return;
        }

        if (!town.hasPermission(sender.getUniqueId(), Permission.PLOTS_ASSIGN)) {
            messages.getTown().getPlot().getNoPermissionAssign().send(sender);
            return;
        }

        final OfflinePlayer foundPlayer = Bukkit.getOfflinePlayerIfCached(name);

        if (foundPlayer == null) {
            messages.getTown().getNotOnline().send(sender, Placeholder.unparsed("player", name));
            return;
        }

        if (!town.getOwnerAndMembers().contains(foundPlayer.getUniqueId())) {
            messages.getTown().getPlot().getMember().getNotMember()
                .send(sender, Placeholder.unparsed("player", foundPlayer.getName()),
                    Placeholder.unparsed("adder", sender.getName()));
            return;
        }

        if (foundPlot.getAssignedMembers().contains(foundPlayer.getUniqueId())) {
            messages.getTown().getPlot().getMember().getAlreadyMember().send(
                sender, Placeholder.unparsed("player", foundPlayer.getName()),
                Placeholder.unparsed("adder", sender.getName()));
            return;
        }

        foundPlot.addMember(foundPlayer.getUniqueId());

        messages.getTown().getPlot().getMember().getSuccess().send(sender, Placeholder.unparsed("player", foundPlayer.getName()));
        if (foundPlayer.isOnline()) {
            messages.getTown().getPlot().getMember().getAdded().send(foundPlayer.getPlayer(), Placeholder.unparsed("player", sender.getName()));
        }
    }
}
