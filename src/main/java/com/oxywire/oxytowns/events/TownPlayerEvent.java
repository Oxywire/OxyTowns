package com.oxywire.oxytowns.events;

import com.oxywire.oxytowns.entities.impl.town.Town;
import org.bukkit.entity.Player;

/**
 * Called when a player does something in relation to a town.
 */
public abstract class TownPlayerEvent extends TownEvent {
    private final Player player;

    public TownPlayerEvent(final Town town, final Player player, final boolean async) {
        super(town, async);
        this.player = player;
    }

    public TownPlayerEvent(final Town town, final Player player) {
        this(town, player, false);
    }

    public Player getPlayer() {
        return player;
    }
}
