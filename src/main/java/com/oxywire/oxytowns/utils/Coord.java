package com.oxywire.oxytowns.utils;

import lombok.Data;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;

@Data
public final class Coord {

    private static final int CELL_SIZE = 16;
    private final int x;
    private final int z;

    /**
     * Convert a value to the grid cell counterpart
     *
     * @param value x/z integer
     * @return cell position
     */
    public static int toCell(final int value) {
        // Floor divides means that for negative values will round to the next negative value
        // and positive value to the previous positive value.
        return Math.floorDiv(value, getCellSize());
    }

    /**
     * Convert regular grid coordinates to their grid cell's counterparts.
     *
     * @param x - X int (Coordinates)
     * @param z - Z int (Coordinates)
     * @return a new instance of Coord.
     */
    public static Coord parseCoord(final int x, final int z) {
        return new Coord(toCell(x), toCell(z));
    }

    public static Coord parseCoord(final Entity entity) {
        return parseCoord(entity.getLocation());
    }

    public static Coord parseCoord(final Location loc) {
        return parseCoord(loc.getBlockX(), loc.getBlockZ());
    }

    public static Coord parseCoord(final Block block) {
        return parseCoord(block.getX(), block.getZ());
    }

    public static int getCellSize() {
        return CELL_SIZE;
    }

    public Coord add(final int xOffset, final int zOffset) {
        return new Coord(this.getX() + xOffset, this.getZ() + zOffset);
    }

    @Override
    public String toString() {
        return this.getX() + "," + this.getZ();
    }
}
