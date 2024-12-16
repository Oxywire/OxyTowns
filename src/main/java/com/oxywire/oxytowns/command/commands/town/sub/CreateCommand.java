package com.oxywire.oxytowns.command.commands.town.sub;

import cloud.commandframework.annotations.Argument;
import cloud.commandframework.annotations.CommandDescription;
import cloud.commandframework.annotations.CommandMethod;
import cloud.commandframework.annotations.Regex;
import com.oxywire.oxytowns.cache.TownCache;
import com.oxywire.oxytowns.config.Config;
import com.oxywire.oxytowns.config.Messages;
import com.oxywire.oxytowns.entities.impl.town.Town;
import com.oxywire.oxytowns.events.TownCreateEvent;
import com.oxywire.oxytowns.events.TownPlayerJoinEvent;
import com.oxywire.oxytowns.utils.ChunkPosition;
import com.oxywire.oxytowns.utils.RegionUtils;
import com.oxywire.oxytowns.utils.TownUtils;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public final class CreateCommand {

    private final TownCache townCache;

    public CreateCommand(final TownCache townCache) {
        this.townCache = townCache;
    }

    @CommandMethod("town|t create|make|new <name>")
    @CommandDescription("Create a town")
    public void onCreate(final Player sender, final @Argument("name") @Regex(TownUtils.VALID_NAME) String name) {
        final Messages messages = Messages.get();

        if (this.townCache.getTownByPlayer(sender).isPresent()) {
            messages.getTown().getAlreadyMember().send(sender);
            return;
        }
        if (RegionUtils.isInRegion(sender.getLocation())) {
            messages.getTown().getClaim().getErrorProtectedClaim().send(sender);
            return;
        }
        if (!this.townCache.getTownsInChunks(TownUtils.getChunksAroundPlayer(sender, 5)).isEmpty()) {
            messages.getTown().getClaim().getTownNear().send(sender);
            return;
        }
        if (!sender.hasPermission("oxytowns.create.town")) {
            messages.getTown().getCreationNoPermission().send(sender);
            return;
        }

        if (Config.get().getBlacklistedWorlds().contains(sender.getWorld().getName())) {
            messages.getTown().getClaim().getErrorBlacklistedWorld().send(sender);
            return;
        }

        if (this.townCache.getTownByName(name).isPresent()) {
            messages.getTown().getCreationNameAlreadyExists().send(sender, Placeholder.unparsed("name", name));
            return;
        }

        if (!this.townCache.getTownsInChunks(TownUtils.getChunksAroundPlayer(sender, 5)).isEmpty()) {
            messages.getTown().getClaim().getTownNear().send(sender);
            return;
        }

        final UUID uuid = UUID.randomUUID();
        final Town town = new Town(uuid, name, sender.getUniqueId());
        TownCreateEvent createEvent = new TownCreateEvent(town, sender);
        if (!createEvent.callEvent())
            return;

        town.claimChunks(ChunkPosition.chunkPosition(sender.getLocation()));
        town.setHome(sender.getLocation());

        this.townCache.createTown(town);
        this.townCache.updateVaultLogic(town);

        messages.getTown().getCreationTownCreated().send(
            Bukkit.getServer(),
            Placeholder.unparsed("name", name),
            Placeholder.unparsed("player", sender.getName())
        );

        messages.getTown().getSuccessTitle().send(sender);

        TownPlayerJoinEvent joinEvent = new TownPlayerJoinEvent(town, sender);
        Bukkit.getPluginManager().callEvent(joinEvent);
    }
}
