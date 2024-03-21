package com.oxywire.oxytowns.addons;

import com.oxywire.oxytowns.OxyTownsPlugin;
import com.oxywire.oxytowns.config.Config;
import com.oxywire.oxytowns.config.SquareMapConfig;
import com.oxywire.oxytowns.config.messaging.Message;
import com.oxywire.oxytowns.entities.impl.town.Town;
import com.oxywire.oxytowns.entities.types.settings.SpawnSetting;
import com.oxywire.oxytowns.utils.ChunkPosition;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.jpenilla.squaremap.api.BukkitAdapter;
import xyz.jpenilla.squaremap.api.Key;
import xyz.jpenilla.squaremap.api.Point;
import xyz.jpenilla.squaremap.api.SimpleLayerProvider;
import xyz.jpenilla.squaremap.api.Squaremap;
import xyz.jpenilla.squaremap.api.SquaremapProvider;
import xyz.jpenilla.squaremap.api.marker.Marker;
import xyz.jpenilla.squaremap.api.marker.MarkerOptions;

import javax.imageio.ImageIO;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

public final class SquareMapAddon extends BukkitRunnable {

    private static final Key OXYTOWNS_KEY = Key.key("OxyTowns");
    private static final Key TOWN_SPAWN_KEY = Key.key("town-spawn");

    private Map<String, SimpleLayerProvider> providers = new HashMap<>();

    public SquareMapAddon() {
        try {
            final Squaremap squaremap = SquaremapProvider.get();

            squaremap.iconRegistry().register(TOWN_SPAWN_KEY, ImageIO.read(SquareMapConfig.get().getTownSpawnIcon().toURL()));
            this.providers = Bukkit.getWorlds()
                .stream()
                .filter(world -> !Config.get().getBlacklistedWorlds().contains(world.getName()))
                .collect(Collectors.toMap(UnaryOperator.identity(), it -> squaremap.getWorldIfEnabled(BukkitAdapter.worldIdentifier(it))))
                .entrySet()
                .stream()
                .filter(it -> it.getValue().isPresent())
                .map(it -> Map.entry(it.getKey().getName(), it.getValue().get()))
                .collect(
                    Collectors.toMap(
                        Map.Entry::getKey,
                        it -> {
                            final SimpleLayerProvider layerProvider = SimpleLayerProvider.builder(OXYTOWNS_KEY.getKey()).build();
                            it.getValue().layerRegistry().register(OXYTOWNS_KEY, layerProvider);
                            return layerProvider;
                        }
                    )
                );

            this.runTaskTimerAsynchronously(OxyTownsPlugin.get(), 0L, 20L * 30);
        } catch (final Exception e) {
            OxyTownsPlugin.get().getSLF4JLogger().error("Failed to load SquareMapAddon: {}", e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        final OxyTownsPlugin plugin = OxyTownsPlugin.get();
        final SquareMapConfig config = SquareMapConfig.get();

        this.providers.values().forEach(SimpleLayerProvider::clearMarkers);

        plugin.getTownCache().getTowns().forEach(town -> {
            final MarkerOptions options = MarkerOptions.builder()
                .stroke(config.isStroke())
                .strokeColor(new Color(config.getStrokeColor()))
                .strokeOpacity(config.getStrokeOpacity())
                .fill(config.isFill())
                .fillColor(new Color(config.getFillColor()))
                .fillOpacity(config.getFillOpacity())
                .fillRule(config.getFillRule())
                .clickTooltip(replace(town, config.getClickTooltip()))
                .hoverTooltip(replace(town, config.getHoverTooltip()))
                .build();

            if (town.getSpawnPosition() != null) {
                final SimpleLayerProvider layer = this.providers.get(town.getSpawnPosition().getWorld().getName());
                if (layer == null) return;

                layer.addMarker(
                    Key.key(String.format("town-%s", town.getName())),
                    Marker.icon(
                        Point.point(town.getSpawnPosition().x(), town.getSpawnPosition().z()),
                        TOWN_SPAWN_KEY,
                        config.getTownSpawnIconSize()
                    )
                );
            }

            for (final ChunkPosition claim : town.getOutpostAndClaimedChunks()) {
                final Point point1 = Point.point(claim.getX() << 4, claim.getZ() << 4);
                final Point point2 = Point.point((claim.getX() << 4) + 16, (claim.getZ() << 4) + 16);
                final Marker marker = Marker.rectangle(point1, point2).markerOptions(options);

                final SimpleLayerProvider layer = this.providers.get(claim.getWorld());
                if (layer == null) continue;
                layer.addMarker(Key.key(String.format("oxytowns-%s-%s", claim.getX(), claim.getZ())), marker);
            }
        });
    }

    private static String replace(final Town entity, final String message) {
        return message
            .replace("<worth>", Message.formatCurrency(entity.getWorth()))
            .replace("<name>", entity.getName())
            .replace("<town>", entity.getName())
            .replace("<spawn-setting>", Optional.ofNullable(entity.getSpawnSetting()).orElse(SpawnSetting.MEMBERS).getName())
            .replace("<owner>", Objects.requireNonNullElse(Bukkit.getOfflinePlayer(entity.getOwner()).getName(), "null"))
            .replace("<members>", Message.formatNumber(entity.getOwnerAndMembers().size()))
            .replace("<claims>", Message.formatNumber(entity.getClaimedChunks().size()))
            .replace("<outposts>", Message.formatNumber(entity.getOutpostChunks().size()))
            .replace("<age>", entity.getCreationDate().toString())
            .replace("<upkeep>", Message.formatCurrency(Config.get().getUpkeep().getTownValue() * entity.getClaimedChunks().size()))
            .replace("<residents>", String.join(", ", entity.getOwnerAndMemberNames()));
    }
}
