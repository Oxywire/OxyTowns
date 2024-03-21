package com.oxywire.oxytowns.config;

import com.oxywire.oxytowns.OxyTownsPlugin;
import com.oxywire.oxytowns.config.messaging.Message;
import com.oxywire.oxytowns.entities.types.PlotType;
import com.oxywire.oxytowns.entities.types.Upgrade;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import java.time.ZoneId;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Getter
@SuppressWarnings({"FieldMayBeFinal", "FieldCanBeLocal"})
@ConfigSerializable
public final class Config {

    @Setting
    private double claimPrice = 150.0;

    @Setting
    private double outpostPrice = 50_000.0;

    @Setting
    private int maxClaimRadius = 1;

    @Setting
    private String banConsoleCommand = "spawn <player>";

    @Setting
    private Map<PlotType, Plot> plots = Map.of(
        PlotType.FARM, new Plot(EnumSet.of(Material.CARROTS, Material.POTATOES, Material.WHEAT), EnumSet.noneOf(EntityType.class), Set.of()),
        PlotType.MOB_FARM, new Plot(EnumSet.noneOf(Material.class), EnumSet.of(EntityType.COW, EntityType.PIG, EntityType.SHEEP, EntityType.CHICKEN), Set.of()),
        PlotType.ARENA, new Plot(EnumSet.noneOf(Material.class), EnumSet.of(EntityType.PLAYER), Set.of("/fly"))
    );

    @Setting
    private Map<Upgrade, Map<Integer, Double>> upgrades = new LinkedHashMap<>(Map.of(
        Upgrade.CLAIMS, Map.of(15, 10_000.0, 20, 20_000.0, 50, 30_000.0, 100, 40_000.0, 200, 50_000.0, 350, 60_000.0, 500, 70_000.0),
        Upgrade.MEMBERS, Map.of(10, 10_000.0, 25, 20_000.0, 50, 30_000.0, 75, 40_000.0, 100, 50_000.0, 150, 60_000.0, 250, 70_000.0),
        Upgrade.VAULT_AMOUNT, Map.of(2, 10_000.0, 3, 20_000.0, 4, 30_000.0, 5, 40_000.0, 6, 50_000.0, 7, 60_000.0, 8, 70_000.0),
        Upgrade.OUTPOSTS, Map.of(1, 10_000.0, 2, 20_000.0, 3, 30_000.0, 4, 40_000.0, 5, 50_000.0, 6, 60_000.0, 7, 70_000.0)
    ));

    @Setting
    private List<String> blacklistedWorlds = List.of(
        "resource_world"
    );

    @Setting
    private Upkeep upkeep = new Upkeep();

    @Setting
    private TownChat townChat = new TownChat();

    public static Config get() {
        return OxyTownsPlugin.configManager.get(Config.class);
    }

    @Getter
    @ConfigSerializable
    public static final class Upkeep {

        @Setting
        private boolean enabled = true;

        @Setting
        private double townValue = 25;

        @Setting
        private int hour = 12;

        @Setting
        private ZoneId timezone = ZoneId.of("America/New_York");

    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @ConfigSerializable
    public static final class Plot {

        @Setting
        private Set<Material> blocks = EnumSet.noneOf(Material.class);

        @Setting
        private Set<EntityType> entities = EnumSet.noneOf(EntityType.class);

        @Setting
        private Set<String> blacklistedCommands = new HashSet<>();
    }

    @Getter
    @ConfigSerializable
    public static final class TownChat {

        @Setting
        private boolean enabled = true;

        @Setting
        private Message format = new Message().setMessage("<blue>[Town] <white><sender>: <gray><message>");
    }
}
