package com.oxywire.oxytowns.utils;

import com.oxywire.oxytowns.cache.TownCache;
import com.oxywire.oxytowns.entities.impl.town.Town;
import lombok.experimental.UtilityClass;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;

@UtilityClass
public final class TownUtils {

    public final String VALID_NAME = "^[a-zA-Z0-9-_]+$";

    public boolean isNextToBorder(final Player player, final Town town) {
        final String world = player.getLocation().getWorld().getName();
        final int baseX = player.getLocation().getChunk().getX();
        final int baseZ = player.getLocation().getChunk().getZ();

        for (int x = baseX - 1; x <= baseX + 1; x++) {
            for (int z = baseZ - 1; z <= baseZ + 1; z++) {
                if (town.hasClaimed(new ChunkPosition(x, z, world))) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean townExclusive(final TownCache townCache, final Town town, final Player player, final int toCheck, final boolean includeWilderness) {
        final Set<Town> towns = townCache.getTownsInChunks(getChunksAroundPlayer(player, toCheck));

        if (includeWilderness && towns.isEmpty()) {
            return true;
        }

        return towns.size() <= 1 && towns.contains(town);
    }

    public Set<ChunkPosition> getChunksAroundPlayer(final Player player, final int range) {
        final World world = player.getWorld();
        final int baseX = player.getLocation().getChunk().getX();
        final int baseZ = player.getLocation().getChunk().getZ();
        final Set<ChunkPosition> chunksAroundPlayer = new HashSet<>();

        for (int x = -range; x <= range; x++) {
            for (int z = -range; z <= range; z++) {
                chunksAroundPlayer.add(new ChunkPosition(baseX + x, baseZ + z, world.getName()));
            }
        }

        return chunksAroundPlayer;
    }
}
