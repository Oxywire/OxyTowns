package com.oxywire.oxytowns.api;

import com.oxywire.oxytowns.OxyTownsPlugin;
import com.oxywire.oxytowns.cache.TownCache;
import com.oxywire.oxytowns.entities.impl.plot.Plot;
import com.oxywire.oxytowns.entities.impl.town.Town;
import com.oxywire.oxytowns.entities.types.Role;
import com.oxywire.oxytowns.entities.types.perms.Permission;
import com.oxywire.oxytowns.utils.ChunkPosition;
import org.bukkit.Location;
import org.bukkit.entity.Player;

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

        final Plot plot = town.getPlot(chunkPosition);
        final Role role = town.getRole(player);

        // They're either the mayor or have plots_modify
        if (town.getPermission(role, Permission.PLOTS_MODIFY)) {
            return true;
        }

        // Let plot perms override their town perms
        if (plot.getAssignedMembers().contains(player) || plot.getPermission(role, permission)) {
            return true;
        }

        // Check their town perms
        if (plot.getAssignedMembers().isEmpty() && plot.getPermissions().isEmpty()) {
            return town.hasPermission(player, permission);
        }

        return false;
    }
}
