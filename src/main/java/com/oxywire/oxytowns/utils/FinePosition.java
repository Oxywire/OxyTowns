package com.oxywire.oxytowns.utils;

import lombok.Data;
import org.bukkit.Bukkit;
import org.bukkit.Location;

@Data
public class FinePosition {

    private final String world;
    private final double x;
    private final double y;
    private final double z;
    private final float yaw;
    private final float pitch;

    public static FinePosition finePosition(final Location location) {
        return new FinePosition(location.getWorld().getName(), location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
    }

    public Location getBukkitLocation() {
        return new Location(Bukkit.getWorld(this.world), this.x, this.y, this.z, this.yaw, this.pitch);
    }
}
