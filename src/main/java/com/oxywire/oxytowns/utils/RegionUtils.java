package com.oxywire.oxytowns.utils;

import com.oxywire.oxytowns.config.Config;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.Set;

@UtilityClass
public final class RegionUtils {

    /**
     * Check if a location is in a region in general
     *
     * @param location the location to check
     * @return if the specified location is in a region or not
     */
    public boolean isInRegion(final Location location) {
        if (!Bukkit.getPluginManager().isPluginEnabled("WorldGuard")) return false;

        final World world = location.getWorld();
        final RegionManager regionManager = WorldGuard.getInstance().getPlatform().getRegionContainer().get(BukkitAdapter.adapt(world));
        if (regionManager == null) return false;

        final Chunk chunk = location.getChunk();
        final BlockVector3 min = BukkitAdapter.asBlockVector(chunk.getBlock(0, world.getMinHeight(), 0).getLocation());
        final BlockVector3 max = BukkitAdapter.asBlockVector(chunk.getBlock(15, world.getMaxHeight(), 15).getLocation());

        final Set<ProtectedRegion> protectedRegion = regionManager.getApplicableRegions(new ProtectedCuboidRegion("tmp", min, max)).getRegions();
        return !protectedRegion.isEmpty() || Config.get().getBlacklistedWorlds().stream().anyMatch(it -> it.equalsIgnoreCase(world.getName()));
    }
}
