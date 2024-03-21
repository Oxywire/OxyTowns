package com.oxywire.oxytowns.command.commands.town.sub;

import cloud.commandframework.annotations.CommandDescription;
import cloud.commandframework.annotations.CommandMethod;
import com.oxywire.oxytowns.cache.TownCache;
import com.oxywire.oxytowns.command.annotation.MustBeInTown;
import com.oxywire.oxytowns.command.annotation.SendersTown;
import com.oxywire.oxytowns.config.Config;
import com.oxywire.oxytowns.config.Messages;
import com.oxywire.oxytowns.entities.impl.town.Town;
import com.oxywire.oxytowns.entities.types.Upgrade;
import com.oxywire.oxytowns.entities.types.perms.Permission;
import com.oxywire.oxytowns.utils.RegionUtils;
import com.oxywire.oxytowns.utils.TownUtils;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.entity.Player;

public final class OutpostClaimCommand {

    private final TownCache townCache;

    public OutpostClaimCommand(final TownCache townCache) {
        this.townCache = townCache;
    }

    @CommandMethod("town|t claim outpost")
    @CommandDescription("Claim an outpost for your town")
    @MustBeInTown
    public void onOutpostClaim(final Player sender, final @SendersTown Town town) {
        final Messages messages = Messages.get();
        final Config config = Config.get();
        final double outpostPrice = config.getOutpostPrice();

        if (!town.hasPermission(sender.getUniqueId(), Permission.CLAIM_UNCLAIM)) {
            messages.getTown().getOutpost().getNoPermissionClaim().send(sender);
            return;
        }

        if (this.townCache.getTownByLocation(sender.getLocation()) != null) {
            messages.getTown().getClaim().getChunkAlreadyClaimed().send(sender);
            return;
        }

        if (town.getUpgradeValue(Upgrade.OUTPOSTS) <= town.getOutpostChunks().size()) {
            messages.getTown().getOutpost().getUpgradeRequired().send(sender);
            return;
        }

        if (!TownUtils.townExclusive(this.townCache, town, sender, 5, true)) {
            messages.getTown().getClaim().getTownNear().send(sender);
            return;
        }

        if (RegionUtils.isInRegion(sender.getLocation())) {
            messages.getTown().getClaim().getErrorProtectedClaim().send(sender);
            return;
        }

        if (config.getBlacklistedWorlds().contains(sender.getWorld().getName())) {
            messages.getTown().getClaim().getErrorBlacklistedWorld().send(sender);
            return;
        }

        if (town.getBankValue() < outpostPrice) {
            messages.getTown().getClaim().getErrorCannotAffordClaim().send(sender);
            return;
        }

        town.removeWorth(outpostPrice);

        town.claimOutpost(sender.getLocation());
        messages.getTown().getOutpost().getClaimSuccessful().send(sender, Placeholder.unparsed("town", town.getName()));
    }
}
