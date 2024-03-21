package com.oxywire.oxytowns.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TaxCollectionEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    private final List<String> fallenTowns;
    private final double townUpkeep;

    /**
     * Called when taxes are collected, on a new day.
     *
     * @param fallenTowns The list of fallen towns.
     * @param townUpkeep  The total amount of money collected.
     */
    public TaxCollectionEvent(List<String> fallenTowns, double townUpkeep) {
        this.fallenTowns = fallenTowns;
        this.townUpkeep = townUpkeep;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    @SuppressWarnings("unused")
    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @SuppressWarnings("unused")
    public List<String> getFallenTowns() {
        return fallenTowns;
    }

    @SuppressWarnings("unused")
    public double getTownUpkeep() {
        return townUpkeep;
    }
}
