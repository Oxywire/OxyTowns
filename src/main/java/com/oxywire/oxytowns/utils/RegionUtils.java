package com.oxywire.oxytowns.utils;

import com.oxywire.oxytowns.config.Config;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.Location;

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

        final RegionManager regionManager = WorldGuard.getInstance().getPlatform().getRegionContainer().get(BukkitAdapter.adapt(location.getWorld()));
        if (regionManager == null) return false;

        final Set<ProtectedRegion> protectedRegion = regionManager.getApplicableRegions(BukkitAdapter.asBlockVector(location)).getRegions();
        return !protectedRegion.isEmpty() || Config.get().getBlacklistedWorlds().stream().anyMatch(world -> world.equalsIgnoreCase(location.getWorld().getName()));
    }
}
