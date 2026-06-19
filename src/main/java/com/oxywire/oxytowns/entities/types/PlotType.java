package com.oxywire.oxytowns.entities.types;

import com.oxywire.oxytowns.config.Config;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.List;
import java.util.Set;

public enum PlotType {

    DEFAULT,
    FARM,
    PRIVATE_COMMUNAL,
    PUBLIC_COMMUNAL,
    PRIVATE_MOB_FARM,
    PUBLIC_MOB_FARM,
    EMBASSY,
    ARENA,
    @Deprecated
    MOB_FARM;

    private static final List<PlotType> SELECTABLE_VALUES = List.of(
        DEFAULT,
        FARM,
        PRIVATE_COMMUNAL,
        PUBLIC_COMMUNAL,
        PRIVATE_MOB_FARM,
        PUBLIC_MOB_FARM,
        EMBASSY,
        ARENA
    );

    public static List<PlotType> selectableValues() {
        return SELECTABLE_VALUES;
    }

    public boolean test(@Nullable final Object queryObject) {
        if (queryObject == null) {
            return false;
        }

        final Config.Plot config = this.config();
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
        final Config.Plot configurable = this.config();
        if (configurable == null) return false;
        final Set<String> blacklistedCommands = configurable.getBlacklistedCommands();
        return blacklistedCommands != null && blacklistedCommands.contains(command);
    }

    public boolean allowsMobFarmBehavior() {
        return this == PRIVATE_MOB_FARM || this == PUBLIC_MOB_FARM || this == MOB_FARM;
    }

    public boolean allowsPublicChestAccess() {
        return this == PUBLIC_MOB_FARM || this == PUBLIC_COMMUNAL;
    }

    public boolean allowsTownChestAccess() {
        return this == PRIVATE_COMMUNAL;
    }

    public boolean allowsOutsiderAssignments() {
        return this == EMBASSY;
    }

    public PlotType displayType() {
        return this == MOB_FARM ? PRIVATE_MOB_FARM : this;
    }

    private PlotType configType() {
        return this.allowsMobFarmBehavior() ? PRIVATE_MOB_FARM : this;
    }

    private Config.Plot config() {
        final Config.Plot config = Config.get().getPlots().get(this.configType());
        if (config != null || this.configType() != PRIVATE_MOB_FARM) {
            return config;
        }

        return Config.get().getPlots().get(MOB_FARM);
    }
}
