package com.oxywire.oxytowns.command.commands.town.sub;

import cloud.commandframework.annotations.Argument;
import cloud.commandframework.annotations.CommandDescription;
import cloud.commandframework.annotations.CommandMethod;
import cloud.commandframework.annotations.specifier.Greedy;
import com.oxywire.oxytowns.OxyTownsPlugin;
import com.oxywire.oxytowns.cache.TownCache;
import com.oxywire.oxytowns.command.annotation.MustBeInTown;
import com.oxywire.oxytowns.command.annotation.SendersTown;
import com.oxywire.oxytowns.config.Config;
import com.oxywire.oxytowns.config.Messages;
import com.oxywire.oxytowns.entities.impl.town.Town;
import com.oxywire.oxytowns.entities.types.perms.Permission;
import com.oxywire.oxytowns.events.TownPlayerBanEvent;
import com.oxywire.oxytowns.events.TownPlayerLeaveEvent;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;


public final class BanCommand {

    private final TownCache townCache;

    public BanCommand(final TownCache townCache) {
        this.townCache = townCache;
    }

    @CommandMethod("town|t ban <player> [reason]")
    @CommandDescription("Ban a player from your town")
    @MustBeInTown
    public void onBan(
        final Player sender,
        final @SendersTown Town town,
        final @Argument(value = "player", suggestions = "town:bannable-members") OfflinePlayer offlinePlayer,
        final @Greedy @Argument(value = "reason", defaultValue = "No reason.") String reason
    ) {
        final Messages messages = Messages.get();
        if (!town.hasPermission(sender.getUniqueId(), Permission.PUNISH)) {
            messages.getTown().getBan().getNoPermission().send(sender);
            return;
        }

        if (town.getBannedUUIDs().contains(offlinePlayer.getUniqueId())) {
            messages.getTown().getBan().getAlreadyBanned().send(sender);
            return;
        }

        if (town.getOwnerAndMembers().contains(offlinePlayer.getUniqueId())) {
            messages.getTown().getBan().getTargetInTown().send(sender);
            return;
        }

        if (reason.length() > 20) {
            messages.getTown().getBan().getInvalidReason().send(sender);
            return;
        }

        TownPlayerLeaveEvent leaveEvent = new TownPlayerLeaveEvent(town, sender, TownPlayerLeaveEvent.Reason.BANNED);
        Bukkit.getPluginManager().callEvent(leaveEvent);
        if (leaveEvent.isCancelled())
            return;

        final TownPlayerBanEvent event = new TownPlayerBanEvent(town, offlinePlayer);
        Bukkit.getPluginManager().callEvent(event);

        town.addBan(sender, offlinePlayer, reason);

        messages.getTown().getBan().getBroadcastBan().send(town, Placeholder.unparsed("player", offlinePlayer.getName()));
        if (!offlinePlayer.isOnline()) {
            return;
        }

        final Player target = offlinePlayer.getPlayer();

        final Town targetTown = this.townCache.getTownByLocation(target.getLocation());
        // The player is currently in the banned town
        if (targetTown != null && targetTown.getTownId().equals(town.getTownId())) {
            final String command = Config.get().getBanConsoleCommand().replace("%player%", target.getName());
            Bukkit.getScheduler().runTask(OxyTownsPlugin.get(), () -> Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), command));
        }

        messages.getPlayer().getBannedFromTown().send(target, Placeholder.unparsed("player", sender.getName()), Placeholder.unparsed("town", town.getName()));
    }
}
