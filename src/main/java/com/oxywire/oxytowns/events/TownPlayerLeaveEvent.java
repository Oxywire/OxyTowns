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
 * Called when a player is about to leave a town.
 * <p>
 * Note this is also called when a player has disbanded their town as technically they're 'leaving' their town.
 * If you cancel the event in this case, nothing happens.
 * </p>
 */
public class TownPlayerLeaveEvent extends TownPlayerEvent implements Cancellable {
    private static final HandlerList HANDLERS = new HandlerList();

    public enum Reason {
        LEFT, KICKED, BANNED, DISBANDED
    }
    @Getter
    private final Reason reason;
    @Getter @Setter
    private boolean cancelled;
    public TownPlayerLeaveEvent(Town town, OfflinePlayer player, Reason reason) {
        super(town, player);
        this.reason = reason;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
