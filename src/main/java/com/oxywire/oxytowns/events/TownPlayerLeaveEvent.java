package com.oxywire.oxytowns.events;

import com.oxywire.oxytowns.entities.impl.town.Town;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Called when a player left a town.
 * <p>
 * Note this is also called when a player has disbanded their town as technically they're 'leaving' their town.
 * If you cancel the event in this case, nothing happens.
 * </p>
 */
public class TownPlayerLeaveEvent extends TownEvent implements Cancellable {
    private static final HandlerList HANDLERS = new HandlerList();

    public enum Reason {
        LEFT, KICKED, BANNED, DISBANDED
    }
    private final Reason reason;
    private final OfflinePlayer player;
    private boolean cancelled;
    public TownPlayerLeaveEvent(Town town, OfflinePlayer player, Reason reason) {
        super(town);
        this.player = player;
        this.reason = reason;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public Reason getReason() {
        return reason;
    }

    public OfflinePlayer getPlayer() {
        return player;
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
