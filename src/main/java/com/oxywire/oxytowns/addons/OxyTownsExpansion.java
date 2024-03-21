package com.oxywire.oxytowns.addons;

import com.oxywire.oxytowns.OxyTownsPlugin;
import com.oxywire.oxytowns.cache.TownCache;
import com.oxywire.oxytowns.config.messaging.Message;
import com.oxywire.oxytowns.entities.impl.town.Town;

import java.util.Map;
import java.util.function.Function;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

public final class OxyTownsExpansion extends PlaceholderExpansion {

    private final TownCache townCache = OxyTownsPlugin.get().getTownCache();
    private final Map<String, Function<OfflinePlayer, String>> placeholderData = Map.of(
        "name", player -> townCache.getTownByPlayer(player.getUniqueId()).map(Town::getName).orElse("&cNone"),
    "balance", player -> String.valueOf(townCache.getTownByPlayer(player.getUniqueId()).map(Town::getBankValue).orElse(0.00)),
    "chunks", player -> String.valueOf(townCache.getTownByPlayer(player.getUniqueId()).map(town -> town.getClaimedChunks().size()).orElse(0)),
    "outposts", player -> String.valueOf(townCache.getTownByPlayer(player.getUniqueId()).map(town -> town.getOutpostChunks().size()).orElse(0)),
    "all_claims", player -> String.valueOf(townCache.getTownByPlayer(player.getUniqueId()).map(town -> town.getOutpostAndClaimedChunks().size()).orElse(0)),
    "owner", player -> townCache.getTownByPlayer(player.getUniqueId()).map(town -> Bukkit.getOfflinePlayer(town.getOwner()).getName()).orElse(""),
    "member_count", player -> String.valueOf(townCache.getTownByPlayer(player.getUniqueId()).map(town -> town.getOwnerAndMemberNames().size()).orElse(0)),
        "town_rank", player -> townCache.getTownByPlayer(player.getUniqueId()).map(town -> town.getOwnerAndMembersWithRoles().get(player.getUniqueId())).map(Message::formatEnum).orElse("")
    );
    private static final Function<OfflinePlayer, String> NULL = player -> "";

    public OxyTownsExpansion() {
        register();
    }

    @Override
    public String getIdentifier() {
        return "OxyTowns";
    }

    @Override
    public String getAuthor() {
        return "Oxywire";
    }

    @Override
    public String getVersion() {
        return "1.0-SNAPSHOT";
    }

    @Override
    public String onRequest(final OfflinePlayer p, final String params) {
        return this.placeholderData.getOrDefault(params, NULL).apply(p);
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public boolean canRegister() {
        return true;
    }
}
