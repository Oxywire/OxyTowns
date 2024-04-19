package com.oxywire.oxytowns.events;

import com.oxywire.oxytowns.entities.impl.town.Town;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Called when a town has been disbanded by a player.
 */
public class TownDisbandEvent extends TownPlayerEvent implements Cancellable {
    private static final HandlerList HANDLERS = new HandlerList();

    /** The player disbanding the town */
    private final Player player;
    private boolean cancelled;
    public TownDisbandEvent(Town town, Player player) {
        super(town, player);
        this.player = player;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    @SuppressWarnings("unused")
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
