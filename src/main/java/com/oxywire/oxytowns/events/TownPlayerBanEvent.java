package com.oxywire.oxytowns.events;

import com.oxywire.oxytowns.entities.impl.town.Town;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public final class TownPlayerBanEvent extends TownEvent {

    private static final HandlerList HANDLERS = new HandlerList();
    private final OfflinePlayer offlinePlayer;

    /**
     * Player ban event called when a player is being banned from a town.
     *
     * @param town the town instigating the ban
     * @param offlinePlayer the player being banned
     */
    public TownPlayerBanEvent(final Town town, final OfflinePlayer offlinePlayer) {
        super(town);
        this.offlinePlayer = offlinePlayer;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public OfflinePlayer getOfflinePlayer() {
        return this.offlinePlayer;
    }
}
