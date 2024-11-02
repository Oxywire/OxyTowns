package com.oxywire.oxytowns.api;

import com.oxywire.oxytowns.OxyTownsPlugin;
import com.oxywire.oxytowns.cache.TownCache;
import com.oxywire.oxytowns.entities.impl.plot.Plot;
import com.oxywire.oxytowns.entities.impl.town.Town;
import com.oxywire.oxytowns.entities.types.PlotType;
import com.oxywire.oxytowns.entities.types.Role;
import com.oxywire.oxytowns.entities.types.perms.Permission;
import com.oxywire.oxytowns.utils.ChunkPosition;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

public final class OxyTownsApi {

    /**
     * @return A mutable map with the default town permissions
     */
    public Map<Role, Set<Permission>> createDefault() {
        return Arrays.stream(Role.values())
            .filter(role -> role != Role.MAYOR)
            .collect(Collectors.toMap(UnaryOperator.identity(), v -> EnumSet.noneOf(Permission.class)));
    }

    /**
     * Checks if a player has permission to do something at a given location
     *
     * @param player player
     * @param permission the permission
     * @param location the location
     * @return true if the player has permission, false otherwise
     */
    public boolean hasPermission(final Player player, final Permission permission, final Location location) {
        return hasPermission(player, permission, ChunkPosition.chunkPosition(location));
    }

    /**
     * Checks if a player has permission to do something at a given chunk position
     *
     * @param player the player
     * @param permission the permission
     * @param position the position
     * @return true if the player has permission, false otherwise
     */
    public boolean hasPermission(final Player player, final Permission permission, final ChunkPosition position) {
        return hasPermission(player.getUniqueId(), permission, position);
    }

    /**
     * Checks if a player has permission to do something at a given chunk position
     *
     * @param player the player's uuid
     * @param permission the permission
     * @param chunkPosition the position
     * @return true if the player has permission, false otherwise
     */
    public boolean hasPermission(final UUID player, final Permission permission, final ChunkPosition chunkPosition) {
        return hasPermission(player, permission, chunkPosition, null);
    }

    /**
     * Checks if a player has permission to do something at a given chunk position
     *
     * @param player the player's uuid
     * @param permission the permission
     * @param chunkPosition the position
     * @param queryObject the object to check
     * @return true if the player has permission, false otherwise
     */
    public boolean hasPermission(final UUID player, final Permission permission, final ChunkPosition chunkPosition, @Nullable final Object queryObject) {
        final TownCache townCache = OxyTownsPlugin.get().getTownCache();

        // They're bypassing via /ta bypass
        if (townCache.isBypassing(player)) {
            return true;
        }

        final Town town = OxyTownsPlugin.get().getTownCache().getTownByChunk(chunkPosition);
        // It's wilderness
        if (town == null) {
            return true;
        }

        // Always allow if the player is the mayor or has plots_modify
        if (town.hasPermission(player, Permission.PLOTS_MODIFY)) {
            return true;
        }

        // Get the plot
        final Plot foundPlot = town.getPlot(chunkPosition);
        // If there isn't a plot there, check the town perms
        if (foundPlot == null) {
            return town.hasPermission(player, permission);
        }

        // If the plot was at all modified, check the plot perms
        if (foundPlot.isModified()) {
            return foundPlot.getAssignedMembers().contains(player)  // always allow assigned members
                || foundPlot.getPermission(town.getRole(player), permission) // plot perms
                || foundPlot.getType().test(queryObject); // ex: farm plots & crops
        }

        // Otherwise, check town permissions
        return town.hasPermission(player, permission);
    }
}
