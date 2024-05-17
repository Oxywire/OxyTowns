package com.oxywire.oxytowns.events;

import com.oxywire.oxytowns.entities.impl.town.Town;
import lombok.Getter;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

/**
 * Called when a player does something in relation to a town.
 */
public abstract class TownPlayerEvent extends TownEvent {
    @Getter
    private final OfflinePlayer player;

    public TownPlayerEvent(final Town town, final OfflinePlayer player, final boolean async) {
        super(town, async);
        this.player = player;
    }

    public TownPlayerEvent(final Town town, final OfflinePlayer player) {
        this(town, player, false);
    }
}
