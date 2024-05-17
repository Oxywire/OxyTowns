package com.oxywire.oxytowns.command.commands.admin.town;

import cloud.commandframework.annotations.Argument;
import cloud.commandframework.annotations.CommandDescription;
import cloud.commandframework.annotations.CommandMethod;
import cloud.commandframework.annotations.CommandPermission;
import com.oxywire.oxytowns.cache.TownCache;
import com.oxywire.oxytowns.config.Messages;
import com.oxywire.oxytowns.entities.impl.town.Town;
import com.oxywire.oxytowns.events.TownPlayerJoinEvent;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

import java.util.Optional;

public final class TownAddPlayerCommand {

    private final TownCache townCache;

    public TownAddPlayerCommand(final TownCache townCache) {
        this.townCache = townCache;
    }

    @CommandMethod("townadmin|ta town <town> add <player>")
    @CommandDescription("Force add a player to a town")
    @CommandPermission("oxytowns.admin")
    public void onTownAdd(final CommandSender sender, final @Argument("town") Town town, final @Argument("player") OfflinePlayer member) {
        final Messages messages = Messages.get();
        final Optional<Town> wantedTown = this.townCache.getTownByPlayer(member.getUniqueId());

        if (wantedTown.isPresent()) {
            messages.getAdmin().getTown().getPlayerInTown().send(sender);
            return;
        }

        TownPlayerJoinEvent joinEvent = new TownPlayerJoinEvent(town, member);
        Bukkit.getPluginManager().callEvent(joinEvent);
        if (joinEvent.isCancelled())
            return;

        town.handleAdminJoin(member.getUniqueId());
        messages.getAdmin().getTown().getPlayerForceAdded().send(
            sender,
            Placeholder.unparsed("player", member.getName()),
            Placeholder.unparsed("town", town.getName())
        );
    }
}
