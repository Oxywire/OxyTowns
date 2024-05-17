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
 * Called when a town is about to be disbanded by a player.
 */
public class TownDisbandEvent extends TownEvent implements Cancellable {
    private static final HandlerList HANDLERS = new HandlerList();

    @Getter @Setter
    private boolean cancelled;
    public TownDisbandEvent(Town town) {
        super(town);
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    @SuppressWarnings("unused")
    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
