package com.oxywire.oxytowns.command.commands.town.sub;

import cloud.commandframework.annotations.CommandDescription;
import cloud.commandframework.annotations.CommandMethod;
import com.google.common.collect.Sets;
import com.oxywire.oxytowns.cache.TownCache;
import com.oxywire.oxytowns.command.annotation.MustBeInTown;
import com.oxywire.oxytowns.command.annotation.SendersTown;
import com.oxywire.oxytowns.config.Config;
import com.oxywire.oxytowns.config.Messages;
import com.oxywire.oxytowns.entities.impl.town.Town;
import com.oxywire.oxytowns.entities.types.Upgrade;
import com.oxywire.oxytowns.entities.types.perms.Permission;
import com.oxywire.oxytowns.utils.ChunkPosition;
import com.oxywire.oxytowns.utils.RegionUtils;
import com.oxywire.oxytowns.utils.TownUtils;
import net.kyori.adventure.text.minimessage.tag.resolver.Formatter;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Set;

public final class ClaimCommand {

    private final TownCache townCache;

    public ClaimCommand(final TownCache townCache) {
        this.townCache = townCache;
    }

    @CommandMethod("town|t claim")
    @CommandDescription("Claim a chunk for your town")
    @MustBeInTown
    public void onClaim(final Player sender, final @SendersTown Town town) {
        final Messages messages = Messages.get();
        final Config config = Config.get();

        if (!town.hasPermission(sender.getUniqueId(), Permission.CLAIM_UNCLAIM)) {
            messages.getTown().getNoPermissionClaim().send(sender);
            return;
        }

        final List<String> blacklistedWorlds = config.getBlacklistedWorlds();
        final double claimPrice = config.getClaimPrice();

        if (blacklistedWorlds.contains(sender.getLocation().getWorld().getName())) {
            messages.getTown().getClaim().getErrorBlacklistedWorld().send(sender);
            return;
        }

        if (this.townCache.getTownByLocation(sender.getLocation()) != null) {
            messages.getTown().getClaim().getChunkAlreadyClaimed().send(sender);
            return;
        }

        if (!TownUtils.townExclusive(this.townCache, town, sender, 5, true)) {
            messages.getTown().getClaim().getTownNear().send(sender);
            return;
        }

        if (!town.getClaimedChunks().isEmpty() && !TownUtils.isNextToBorder(sender, town)) {
            messages.getTown().getClaim().getErrorConnectedClaims().send(sender);
            return;
        }

        if (RegionUtils.isInRegion(sender.getLocation())) {
            messages.getTown().getClaim().getErrorProtectedClaim().send(sender);
            return;
        }

        final Chunk chunk = sender.getLocation().getChunk();
        final Set<ChunkPosition> chunksToClaim = Sets.newHashSet(ChunkPosition.chunkPosition(chunk));

        if (town.getUpgradeValue(Upgrade.CLAIMS) <= town.getClaimedChunks().size()) {
            messages.getTown().getClaim().getErrorUpgradeRequired().send(sender);
            return;
        }

        int priceToClaim = (int) (claimPrice * chunksToClaim.size());

        if (town.getBankValue() < priceToClaim) {
            messages.getTown().getClaim().getErrorCannotAffordClaim().send(sender);
            return;
        }

        town.removeWorth((double) priceToClaim);

        chunksToClaim.forEach(chunkPosition -> {
            final Location location = Bukkit.getWorld(chunkPosition.getWorld()).getHighestBlockAt(chunkPosition.getX() << 4, chunkPosition.getZ() << 4).getLocation();

            if (RegionUtils.isInRegion(location) || town.hasClaimed(chunkPosition)) {
                chunksToClaim.remove(chunkPosition);
                return;
            }

            if (town.getClaimedChunks().isEmpty() && town.getHome() == null) {
                town.setHome(sender.getLocation());
            }

            town.claimChunks(chunkPosition);
        });

        messages.getTown().getClaim().getClaimSuccess().send(
            sender,
            Placeholder.unparsed("claims", String.valueOf(chunksToClaim.size())),
            Formatter.number("price", priceToClaim)
        );
    }
}
