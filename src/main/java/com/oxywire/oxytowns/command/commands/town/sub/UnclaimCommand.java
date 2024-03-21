package com.oxywire.oxytowns.command.commands.town.sub;

import cloud.commandframework.annotations.CommandDescription;
import cloud.commandframework.annotations.CommandMethod;
import cloud.commandframework.annotations.Hidden;
import com.oxywire.oxytowns.cache.TownCache;
import com.oxywire.oxytowns.command.annotation.AcceptConfirmation;
import com.oxywire.oxytowns.command.annotation.CreateConfirmation;
import com.oxywire.oxytowns.command.annotation.MustBeInTown;
import com.oxywire.oxytowns.command.annotation.SendersTown;
import com.oxywire.oxytowns.config.Messages;
import com.oxywire.oxytowns.entities.impl.town.Town;
import com.oxywire.oxytowns.entities.types.perms.Permission;
import com.oxywire.oxytowns.utils.ChunkPosition;
import com.oxywire.oxytowns.utils.TownUtils;
import org.bukkit.entity.Player;

public final class UnclaimCommand {

    private final TownCache townCache;

    public UnclaimCommand(final TownCache townCache) {
        this.townCache = townCache;
    }

    @CommandMethod("town|t unclaim confirm")
    @CommandDescription("Confirm unclaiming a chunk")
    @AcceptConfirmation("player_unclaim")
    @Hidden
    @MustBeInTown
    public void onUnclaimConfirm(final Player sender, final @SendersTown Town town) {
        final Messages messages = Messages.get();
        final ChunkPosition chunkPosition = ChunkPosition.chunkPosition(sender.getLocation().getChunk());

        if (!town.hasPermission(sender.getUniqueId(), Permission.CLAIM_UNCLAIM)) {
            messages.getTown().getNoPermissionClaim().send(sender);
            return;
        }

        if (!town.hasClaimed(chunkPosition)) {
            messages.getTown().getUnclaim().getNotClaimed().send(sender);
            return;
        }


        if (!TownUtils.townExclusive(this.townCache, town, sender, 1, false)) {
            messages.getTown().getUnclaim().getChunkLinkedOutpost().send(sender);
            return;
        }

        town.unclaimChunk(chunkPosition, sender);
        messages.getTown().getUnclaim().getUnclaimSuccess().send(sender);
    }

    @CommandMethod("town|t unclaim")
    @CommandDescription("Unclaim a chunk")
    @CreateConfirmation("player_unclaim")
    @MustBeInTown
    public void onUnclaim(final Player sender, final @SendersTown Town town) {
        final Messages messages = Messages.get();
        final ChunkPosition chunkPosition = ChunkPosition.chunkPosition(sender.getLocation().getChunk());

        if (!town.hasClaimed(chunkPosition)) {
            messages.getTown().getUnclaim().getNotClaimed().send(sender);
            return;
        }

        if (!town.hasPermission(sender.getUniqueId(), Permission.CLAIM_UNCLAIM)) {
            messages.getTown().getNoPermissionClaim().send(sender);
            return;
        }

        // Check if it's a claim but not an outpost
        if (!town.getOutpostChunks().contains(chunkPosition)) {
            // Check if it's home chunk
            if (town.getHome() != null && chunkPosition.contains(town.getHome())) {
                messages.getTown().getUnclaim().getConfirmHomeBlockUnclaim().send(sender);
            } else {
                // It's a regular claim. Unclaim and tell them they unclaimed it.
                town.unclaimChunk(chunkPosition, sender);
                messages.getTown().getUnclaim().getUnclaimSuccess().send(sender);
            }
        } else {
            // It's an outpost
            messages.getTown().getUnclaim().getUnclaimConfirm().send(sender);
        }
    }
}
