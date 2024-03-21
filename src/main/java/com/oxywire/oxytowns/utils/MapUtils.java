package com.oxywire.oxytowns.utils;

import com.oxywire.oxytowns.OxyTownsPlugin;
import com.oxywire.oxytowns.cache.TownCache;
import com.oxywire.oxytowns.config.Messages;
import com.oxywire.oxytowns.config.messaging.Message;
import com.oxywire.oxytowns.entities.impl.plot.Plot;
import com.oxywire.oxytowns.entities.impl.town.Town;
import lombok.experimental.UtilityClass;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;

@UtilityClass
public class MapUtils {

    /* Map size */
    private final int LINE_WIDTH = 17;
    private final int LINE_HEIGHT = 17;
    private final int HALF_LINE_WIDTH = LINE_WIDTH / 2;
    /* Map info */
    private final Component[] HELP = {
            Message.MINI_MESSAGE.deserialize("  <yellow>■ <gray>- <white>Your Location"),
            Message.MINI_MESSAGE.deserialize("  <dark_gray>■ <gray>- <white>Wilderness"),
            Message.MINI_MESSAGE.deserialize("  <dark_green>■ <gray>- <white>Your Town"),
            Message.MINI_MESSAGE.deserialize("  <green>■ <gray>- <white>Your Plot"),
            Message.MINI_MESSAGE.deserialize("  <red>■ <gray>- <white>Other Town")
    };
    /* Compass */
    private final Component COMPASS_FILLER = Component.text("  -----  ").color(NamedTextColor.BLACK);
    private final Component COMPASS_PREFIX = Component.text("  -").color(NamedTextColor.BLACK);
    private final Component HYPHEN = Component.text("-");
    private final Component DOUBLE_SPACE = Component.text("  ");
    /* Grid points */
    private final Component YOUR_LOCATION = Component.text("■").color(NamedTextColor.YELLOW);
    private final Component WILDERNESS = Component.text("■").color(NamedTextColor.DARK_GRAY);
    private final Component YOUR_TOWN = Component.text("■").color(NamedTextColor.DARK_GREEN);
    private final Component YOUR_PLOT = Component.text("■").color(NamedTextColor.GREEN);
    private final Component OTHER_TOWN = Component.text("■").color(NamedTextColor.RED);

    public Component[] generateCompass(final Player player) {
        final Compass.Point dir = Compass.getCompassPointForDirection(player.getLocation().getYaw());

        return new Component[] {
            COMPASS_FILLER,
            COMPASS_PREFIX.append(dir == Compass.Point.NW ? Component.text('\\').color(NamedTextColor.GOLD) : HYPHEN)
                .append(dir == Compass.Point.N ? Component.text('N').color(NamedTextColor.GOLD) : Component.text('N').color(NamedTextColor.WHITE))
                .append(dir == Compass.Point.NE ? Component.text('/').color(NamedTextColor.GOLD) : HYPHEN)
                .append(HYPHEN)
                .append(DOUBLE_SPACE),
            COMPASS_PREFIX.append(dir == Compass.Point.W ? Component.text('W').color(NamedTextColor.GOLD) : Component.text('W').color(NamedTextColor.WHITE))
                .append(Component.text('+').color(NamedTextColor.DARK_GRAY))
                .append(dir == Compass.Point.E ? Component.text('E').color(NamedTextColor.GOLD) : Component.text('E').color(NamedTextColor.WHITE))
                .append(HYPHEN)
                .append(DOUBLE_SPACE),
            COMPASS_PREFIX.append(dir == Compass.Point.SW ? Component.text('/').color(NamedTextColor.GOLD) : HYPHEN)
                .append(dir == Compass.Point.S ? Component.text('S').color(NamedTextColor.GOLD) : Component.text('S').color(NamedTextColor.WHITE))
                .append(dir == Compass.Point.SE ? Component.text('\\').color(NamedTextColor.GOLD) : HYPHEN)
                .append(HYPHEN)
                .append(DOUBLE_SPACE)
            };
    }

    public void display(final Player player) {
        Messages messages = Messages.get();
        Component map = Message.MINI_MESSAGE.deserialize(messages.getTown().getTownyMapHeader().getMessage()).append(Component.newline());
        TownCache townCache = OxyTownsPlugin.get().getTownCache();

        int playerX = player.getLocation().getChunk().getX();
        int playerZ = player.getLocation().getChunk().getZ();
        String world = player.getWorld().getName();
        final Component[] compass = generateCompass(player);

        int help = 0;
        int compassI = 0;
        for (int x = -HALF_LINE_WIDTH; x < HALF_LINE_WIDTH; x++) {
            map = map.append(getOrDefault(compass, compassI++, COMPASS_FILLER));
            for (int y = -HALF_LINE_WIDTH; y < HALF_LINE_WIDTH; y++) {
                map = map.append(Component.space());
                if (x == 0 && y == 0) {
                    map = map.append(YOUR_LOCATION);
                    continue;
                }

                ChunkPosition gridPoint = new ChunkPosition(playerX + y, playerZ + x, world);
                Town town = townCache.getTownByChunk(gridPoint);
                if (town == null) {
                    map = map.append(WILDERNESS);
                    continue;
                }

                if (town.isMemberOrOwner(player.getUniqueId())) {
                    Plot plot = town.getPlot(gridPoint);
                    if (plot != null && plot.getAssignedMembers().contains(player.getUniqueId())) {
                        map = map.append(YOUR_PLOT);
                        continue;
                    }
                    map = map.append(YOUR_TOWN);
                } else {
                    map = map.append(OTHER_TOWN);
                }
            }
            map = map.append(getOrDefault(HELP, help++, Component.empty()));
            map = map.append(Component.newline());
        }

        map = map.append(Message.MINI_MESSAGE.deserialize(messages.getTown().getTownyMapFooter().getMessage()));
        player.sendMessage(map);
    }

    private <T> T getOrDefault(T[] arr, int index, T def) {
        if (index < 0 || index >= arr.length) {
            return def;
        }
        return arr[index];
    }
}
