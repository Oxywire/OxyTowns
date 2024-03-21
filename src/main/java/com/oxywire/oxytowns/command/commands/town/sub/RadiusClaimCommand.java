package com.oxywire.oxytowns.command.commands.town.sub;

import cloud.commandframework.annotations.Argument;
import cloud.commandframework.annotations.CommandDescription;
import cloud.commandframework.annotations.CommandMethod;
import com.oxywire.oxytowns.cache.TownCache;
import com.oxywire.oxytowns.command.annotation.MustBeInTown;
import com.oxywire.oxytowns.command.annotation.SendersTown;
import com.oxywire.oxytowns.config.Config;
import com.oxywire.oxytowns.config.Messages;
import com.oxywire.oxytowns.entities.impl.town.Town;
import com.oxywire.oxytowns.entities.types.Upgrade;
import com.oxywire.oxytowns.utils.ChunkPosition;
import com.oxywire.oxytowns.utils.RegionUtils;
import com.oxywire.oxytowns.utils.TownUtils;
import net.kyori.adventure.text.minimessage.tag.resolver.Formatter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Objects;
import java.util.Set;

public final class RadiusClaimCommand {

    private final TownCache townCache;

    public RadiusClaimCommand(final TownCache townCache) {
        this.townCache = townCache;
    }

    @CommandMethod("town|t claim <radius>")
    @CommandDescription("Claim a radius for your town")
    @MustBeInTown
    public void onClaim(final Player sender, final @SendersTown Town town, final @Argument("radius") int radius) {
        final Messages messages = Messages.get();
        final Config config = Config.get();
        final List<World> blacklistedWorlds = config.getBlacklistedWorlds()
            .stream()
            .map(Bukkit::getWorld)
            .filter(Objects::nonNull)
            .toList();

        final double claimPrice = config.getClaimPrice();

        if (blacklistedWorlds.contains(sender.getLocation().getWorld())) {
            messages.getTown().getClaim().getErrorBlacklistedWorld().send(sender);
            return;
        }

        if (radius == 0) {
            messages.getTown().getClaim().getErrorZeroInput().send(sender);
            return;
        }

        if (radius < 0) {
            messages.getTown().getClaim().getErrorNegativeInput().send(sender);
            return;
        }

        if (this.townCache.getTownByLocation(sender.getLocation()) != null) {
            messages.getTown().getClaim().getChunkAlreadyClaimed().send(sender);
            return;
        }

        int maximumRadius = config.getMaxClaimRadius();

        if (radius > maximumRadius) {
            messages.getTown().getClaim().getErrorOverMaxRadius().send(sender, Formatter.number("max", maximumRadius));
            return;
        }

        if (!TownUtils.townExclusive(this.townCache, town, sender, radius + 5, true)) {
            messages.getTown().getClaim().getTownNear().send(sender);
            return;
        }

        if (!town.getClaimedChunks().isEmpty()) {
            if (!TownUtils.townExclusive(this.townCache, town, sender, radius + 1, false)) {
                messages.getTown().getClaim().getErrorConnectedClaims().send(sender);
                return;
            }
        }

        if (RegionUtils.isInRegion(sender.getLocation())) {
            messages.getTown().getClaim().getErrorProtectedClaim().send(sender);
            return;
        }

        final Set<ChunkPosition> chunksToClaim = TownUtils.getChunksAroundPlayer(sender, radius);

        if (town.getUpgradeValue(Upgrade.CLAIMS) <= town.getClaimedChunks().size() + town.getOutpostChunks().size() + chunksToClaim.size()) {
            messages.getTown().getClaim().getErrorUpgradeRequired().send(sender);
            return;
        }

        int priceToClaim = (int) (claimPrice * chunksToClaim.size());
        int chunksToClaimSize = chunksToClaim.size();

        if (town.getBankValue() < priceToClaim) {
            messages.getTown().getClaim().getErrorCannotAffordClaim().send(sender);
            return;
        }

        for (ChunkPosition chunkPosition : chunksToClaim) {
            Location location = Bukkit.getWorld(chunkPosition.getWorld()).getHighestBlockAt(chunkPosition.getX() << 4, chunkPosition.getZ() << 4).getLocation();

            if (RegionUtils.isInRegion(location) || town.hasClaimed(chunkPosition)) {
                priceToClaim -= claimPrice;
                chunksToClaimSize--;
                continue;
            }

            if (town.getClaimedChunks().isEmpty() && town.getHome() == null) {
                town.setHome(sender.getLocation());
            }

            town.claimChunks(chunkPosition);
        }

        town.removeWorth((double) priceToClaim);
        messages.getTown().getClaim().getClaimSuccess()
            .send(
                sender,
                Formatter.number("claims", chunksToClaimSize),
                Formatter.number("price", priceToClaim)
            );
    }
}
