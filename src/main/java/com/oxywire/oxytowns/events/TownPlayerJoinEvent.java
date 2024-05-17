package com.oxywire.oxytowns.events;

import com.oxywire.oxytowns.entities.impl.town.Town;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Called when a player is about to join a town.
 * <p>
 * Note this is also called when a player has created a town as technically they're 'joining' a town.
 * If you cancel the event in this case, nothing happens.
 * </p>
 */
public class TownPlayerJoinEvent extends TownPlayerEvent implements Cancellable {
    private static final HandlerList HANDLERS = new HandlerList();

    @Getter @Setter
    private boolean cancelled;
    public TownPlayerJoinEvent(Town town, OfflinePlayer player) {
        super(town, player);
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
