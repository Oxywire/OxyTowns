package com.oxywire.oxytowns.entities.types;

import com.oxywire.oxytowns.config.Config;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Set;

public enum PlotType {

    DEFAULT,
    FARM,
    MOB_FARM,
    ARENA;

    public boolean test(@Nullable final Object queryObject) {
        if (queryObject == null) {
            return false;
        }

        final Config.Plot config = Config.get().getPlots().get(this);
        if (config == null) {
            return false;
        }

        if (queryObject instanceof Material material) {
            return config.getBlocks().contains(material);
        } else if (queryObject instanceof Entity entity) {
            return config.getEntities().contains(entity.getType());
        } else if (queryObject instanceof Block block) {
            return config.getBlocks().contains(block.getType());
        }
        return false;
    }

    public boolean isCommandBlacklisted(final String command) {
        final Config.Plot configurable = Config.get().getPlots().get(this);
        if (configurable == null) return false;
        final Set<String> blacklistedCommands = configurable.getBlacklistedCommands();
        return blacklistedCommands != null && blacklistedCommands.contains(command);
    }
}
