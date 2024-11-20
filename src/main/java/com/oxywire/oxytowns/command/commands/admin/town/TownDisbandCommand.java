package com.oxywire.oxytowns.command.commands.admin.town;

import cloud.commandframework.annotations.Argument;
import cloud.commandframework.annotations.CommandDescription;
import cloud.commandframework.annotations.CommandMethod;
import cloud.commandframework.annotations.CommandPermission;
import cloud.commandframework.annotations.Hidden;
import com.oxywire.oxytowns.cache.TownCache;
import com.oxywire.oxytowns.command.annotation.AcceptConfirmation;
import com.oxywire.oxytowns.command.annotation.CreateConfirmation;
import com.oxywire.oxytowns.config.Messages;
import com.oxywire.oxytowns.config.messaging.Message;
import com.oxywire.oxytowns.entities.impl.town.Town;
import com.oxywire.oxytowns.events.TownDisbandEvent;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

@CommandPermission("oxytowns.admin")
public final class TownDisbandCommand {

    private final TownCache townCache;

    public TownDisbandCommand(final TownCache townCache) {
        this.townCache = townCache;
    }

    @CommandMethod("townadmin|ta town <town> disband")
    @CommandDescription("Force disband a town")
    @CreateConfirmation("admin_town_disband")
    public void onDisband(final CommandSender sender, final @Argument("town") Town town) {
        sender.sendMessage(
            Message.LEGACY_SERIALIZER.serialize(
                Message.MINI_MESSAGE.deserialize(
                    Messages.get().getAdmin().getTown().getDisbandWarning().getMessage()
                        .replace("<town>", town.getName())
                )
            )
        );
    }

    @CommandMethod("townadmin|ta town <town> disband confirm")
    @CommandDescription("Confirm force disbanding a town")
    @AcceptConfirmation("admin_town_disband")
    @Hidden
    public void onDisbandConfirm(final CommandSender sender, final @Argument("town") Town town) {
        TownDisbandEvent disbandEvent = new TownDisbandEvent(town);
        Bukkit.getPluginManager().callEvent(disbandEvent);
        if (disbandEvent.isCancelled())
            return;

        Messages.get().getAdmin().getTown().getDisbandSuccess().send(sender, Placeholder.unparsed("town", town.getName()));
        this.townCache.deleteTown(town);
    }
}
