package com.oxywire.oxytowns.events;

import com.oxywire.oxytowns.entities.impl.town.Town;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public final class TownPlayerBanEvent extends TownPlayerEvent {

    private static final HandlerList HANDLERS = new HandlerList();

    /**
     * Player ban event called when a player is being banned from a town.
     *
     * @param town the town instigating the ban
     * @param offlinePlayer the player being banned
     */
    public TownPlayerBanEvent(final Town town, final OfflinePlayer offlinePlayer) {
        super(town, offlinePlayer);
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }
}
