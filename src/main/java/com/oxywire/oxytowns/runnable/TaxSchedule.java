package com.oxywire.oxytowns.runnable;

import com.oxywire.oxytowns.OxyTownsPlugin;
import com.oxywire.oxytowns.config.Config;
import com.oxywire.oxytowns.config.Messages;
import com.oxywire.oxytowns.config.UpkeepTimes;
import com.oxywire.oxytowns.entities.impl.town.Town;
import com.oxywire.oxytowns.events.TaxCollectionEvent;
import net.kyori.adventure.text.minimessage.tag.resolver.Formatter;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Bukkit;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class TaxSchedule {

    private final OxyTownsPlugin plugin;

    public TaxSchedule(final OxyTownsPlugin plugin) {
        this.plugin = plugin;
        handleSchedule();
    }

    public static double getTownTaxValue() {
        return Config.get().getUpkeep().getTownValue();
    }

    /**
     * Take the taxes from all the towns This will also disband towns if they don't have enough
     */
    public Map.Entry<List<String>, Double> takeTownTax() {
        final Config config = Config.get();
        final Messages messages = Messages.get();

        Bukkit.getOnlinePlayers().forEach(player -> messages.getTax().getCollectionMessage().send(player));

        final Set<Town> toDelete = new HashSet<>();
        double totalTax = 0;

        for (Town town : this.plugin.getTownCache().all()) {
            double taxToTake = config.getUpkeep().getTownValue() * town.getOutpostAndClaimedChunks().size();
            totalTax += taxToTake;

            if (taxToTake > 0 && taxToTake > town.getBankValue()) {
                toDelete.add(town);
                messages.getTax().getTownDisbanded().send(Bukkit.getServer(), Placeholder.unparsed("town", town.getName()));
                continue;
            }

            if (taxToTake > 0) {
                town.removeWorth(taxToTake);
            }
        }

        toDelete.forEach(this.plugin.getTownCache()::deleteTown);
        return Map.entry(toDelete.stream().map(Town::getName).toList(), totalTax);
    }

    /**
     * Handles the repeating schedule for when taxes will run
     */
    private void handleSchedule() {
        Bukkit.getScheduler().runTaskTimer(this.plugin, () -> {
            if (!Config.get().getUpkeep().isEnabled()) return;

            final Config.Upkeep config = Config.get().getUpkeep();
            final int currentHour = Instant.now().atZone(config.getTimezone()).getHour();

            if (config.getHour() - 1 == currentHour && !isSameDay(UpkeepTimes.get().getLastUpkeepWarning(), System.currentTimeMillis())) {
                Messages.get().getTax().getCollectionWarning().send(Bukkit.getServer(), Formatter.number("time", 60 - LocalDateTime.now().getMinute()));
                UpkeepTimes.get().setLastUpkeepWarning(System.currentTimeMillis());
                OxyTownsPlugin.configManager.save(UpkeepTimes.get());
            }

            if (config.getHour() != currentHour || isSameDay(UpkeepTimes.get().getLastUpkeep(), System.currentTimeMillis())) {
                return;
            }

            Map.Entry<List<String>, Double> taxData = this.takeTownTax();
            Bukkit.getPluginManager().callEvent(new TaxCollectionEvent(taxData.getKey(), taxData.getValue()));

            UpkeepTimes.get().setLastUpkeep(System.currentTimeMillis());
            OxyTownsPlugin.configManager.save(UpkeepTimes.get());
        }, 20, 20);
    }

    private boolean isSameDay(long m1, long m2) {
        return m1 / 86_400_000 == m2 / 86_400_000;
    }
}
