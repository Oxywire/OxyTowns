package com.oxywire.oxytowns.runnable;

import com.oxywire.oxytowns.OxyTownsPlugin;
import com.oxywire.oxytowns.cache.TownCache;
import com.oxywire.oxytowns.config.Config;
import com.oxywire.oxytowns.entities.impl.plot.Plot;
import com.oxywire.oxytowns.entities.impl.town.Town;
import com.oxywire.oxytowns.entities.types.PlotType;
import com.oxywire.oxytowns.entities.types.settings.Setting;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Enemy;
import org.bukkit.entity.Entity;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.Objects;

public final class MobsRunnable extends BukkitRunnable {

    private static final TownCache TOWN_CACHE = OxyTownsPlugin.get().getTownCache();

    @Override
    public void run() {
        final List<World> worlds = Bukkit.getWorlds()
            .stream()
            .filter(world -> !Config.get().getBlacklistedWorlds().contains(world.getName()))
            .toList();

        for (final World world : worlds) {
            for (final Entity entity : world.getLivingEntities()) {
                if (!(entity instanceof Enemy)) continue;
                if (entity.customName() != null) continue;

                final Town town = TOWN_CACHE.getTownByLocation(entity.getLocation());
                if (town == null) continue;
                final Plot plot = town.getPlot(entity.getLocation());

                if (plot == null && !town.getToggle(Setting.MOBS) || (plot != null && plot.getType() != PlotType.MOB_FARM)) {
                    entity.remove();
                }
            }
        }
    }
}
