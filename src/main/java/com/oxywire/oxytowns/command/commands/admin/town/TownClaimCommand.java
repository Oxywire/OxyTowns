package com.oxywire.oxytowns.command.commands.admin.town;

import cloud.commandframework.annotations.Argument;
import cloud.commandframework.annotations.CommandDescription;
import cloud.commandframework.annotations.CommandMethod;
import cloud.commandframework.annotations.CommandPermission;
import com.oxywire.oxytowns.cache.TownCache;
import com.oxywire.oxytowns.config.Messages;
import com.oxywire.oxytowns.entities.impl.town.Town;
import com.oxywire.oxytowns.utils.ChunkPosition;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.entity.Player;

public final class TownClaimCommand {

    private final TownCache townCache;

    public TownClaimCommand(final TownCache townCache) {
        this.townCache = townCache;
    }

    @CommandMethod("townadmin|ta town <town> claim")
    @CommandDescription("Force claim land for a town")
    @CommandPermission("oxytowns.admin")
    public void onClaim(final Player sender, final @Argument("town") Town town) {
        final Messages messages = Messages.get();
        final Town presentTown = this.townCache.getTownByLocation(sender.getLocation());

        if (presentTown != null) {
            messages.getAdmin().getTown().getChunkAlreadyClaimed().send(sender, Placeholder.unparsed("town", presentTown.getName()));
            return;
        }
        town.claimChunks(ChunkPosition.chunkPosition(sender.getLocation().getChunk()));
        messages.getAdmin().getTown().getChunkClaimSuccess().send(sender, Placeholder.unparsed("town", town.getName()));
    }
}
