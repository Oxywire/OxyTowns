package com.oxywire.oxytowns.addons;

import com.oxywire.oxytowns.OxyTownsPlugin;
import org.bstats.bukkit.Metrics;
import org.bstats.charts.SingleLineChart;

public final class BStats {

    public BStats(final OxyTownsPlugin plugin) {
        final Metrics metrics = new Metrics(plugin, 19551);

        metrics.addCustomChart(new SingleLineChart("towns", () -> plugin.getTownCache().getTowns().size()));
    }
}
