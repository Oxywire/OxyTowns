package com.oxywire.oxytowns.events;

import com.oxywire.oxytowns.entities.impl.town.Town;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Called when a player has joined a town.
 * <p>
 * Note this is also called when a player has created a town as technically they're 'joining' a town.
 * If you cancel the event in this case, nothing happens.
 * </p>
 */
public class TownPlayerJoinEvent extends TownPlayerEvent implements Cancellable {
    private static final HandlerList HANDLERS = new HandlerList();

    private boolean cancelled;
    public TownPlayerJoinEvent(Town town, Player player) {
        super(town, player);
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        cancelled = b;
    }
}
