package com.oxywire.oxytowns.events;

import com.oxywire.oxytowns.entities.impl.town.Town;
import org.bukkit.event.Event;

/**
 * Abstract event class for all town related events
 */
public abstract class TownEvent extends Event {
    private final Town town;
    public TownEvent(final Town town, final boolean async) {
        super(async);
        this.town = town;
    }

    public TownEvent(final Town town) {
        this(town, false);
    }

    public Town getTown() {
        return town;
    }
}
