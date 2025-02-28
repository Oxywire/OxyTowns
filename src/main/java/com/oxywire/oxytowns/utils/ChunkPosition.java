package com.oxywire.oxytowns.utils;

import lombok.Data;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;

@Data
public class ChunkPosition {

    private final int x;
    private final int z;
    private final String world;

    public static ChunkPosition chunkPosition(final Location location) {
        return new ChunkPosition(location.getBlockX() >> 4, location.getBlockZ() >> 4, location.getWorld() == null ? "not-loaded" : location.getWorld().getName());
    }

    public static ChunkPosition chunkPosition(final FinePosition finePosition) {
        return new ChunkPosition(((int) finePosition.getX()) >> 4, ((int) finePosition.getZ()) >> 4, finePosition.getWorld());
    }

    public static ChunkPosition chunkPosition(final Chunk chunk) {
        return new ChunkPosition(chunk.getX(), chunk.getZ(), chunk.getWorld().getName());
    }

    public boolean contains(final Location other) {
        return this.x == other.getBlockX() >> 4 && this.z == other.getBlockZ() >> 4 && this.world.equals(other.getWorld().getName());
    }

    public boolean contains(final FinePosition other) {
        return this.x == ((int) other.getX()) >> 4 && this.z == ((int) other.getZ()) >> 4 && this.world.equals(other.getWorld());
    }

    public Chunk getBukkitChunk() {
        return Bukkit.getWorld(this.world).getChunkAt(this.x, this.z);
    }
}
