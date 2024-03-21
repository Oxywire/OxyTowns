package com.oxywire.oxytowns.events;

import com.oxywire.oxytowns.entities.impl.town.Town;
import com.oxywire.oxytowns.entities.types.Role;
import com.oxywire.oxytowns.entities.types.perms.Permission;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public final class TownPermissionChangeEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();
    private final Town town;
    private final Role role;
    private final Permission permission;

    /**
     * Called when a town changes permissions.
     *
     * @param town the town changing the permissions
     * @param role the role being changed
     * @param permission the permission being changed
     */
    public TownPermissionChangeEvent(final Town town, final Role role, final Permission permission) {
        this.town = town;
        this.role = role;
        this.permission = permission;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public Town getTown() {
        return this.town;
    }

    public Role getRole() {
        return this.role;
    }

    public Permission getPermission() {
        return this.permission;
    }
}
