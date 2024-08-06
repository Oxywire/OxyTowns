package com.oxywire.oxytowns.command.commands.town.sub;

import cloud.commandframework.annotations.CommandDescription;
import cloud.commandframework.annotations.CommandMethod;
import com.oxywire.oxytowns.command.annotation.MustBeInTown;
import com.oxywire.oxytowns.command.annotation.SendersTown;
import com.oxywire.oxytowns.config.Messages;
import com.oxywire.oxytowns.entities.impl.town.Town;
import com.oxywire.oxytowns.events.TownPlayerLeaveEvent;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public final class LeaveCommand {

    @CommandMethod("town|t leave")
    @CommandDescription("Leave your town")
    @MustBeInTown
    public void onLeave(final Player sender, final @SendersTown Town town) {
        final Messages messages = Messages.get();
        if (town.getOwner().equals(sender.getUniqueId())) {
            messages.getPlayer().getMayorCannotLeave().send(sender);
            return;
        }

        TownPlayerLeaveEvent leaveEvent = new TownPlayerLeaveEvent(town, sender, TownPlayerLeaveEvent.Reason.LEFT);
        Bukkit.getPluginManager().callEvent(leaveEvent);
        if (leaveEvent.isCancelled())
            return;

        town.handleLeave(sender);
        messages.getPlayer().getSuccessfullyLeft().send(sender, Placeholder.unparsed("town", town.getName()));
    }
}
