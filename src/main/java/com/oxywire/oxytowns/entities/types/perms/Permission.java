package com.oxywire.oxytowns.entities.types.perms;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Permission {

    /**
     * Global Permissions.
     */
    BLOCK_BREAK(PermissionType.GLOBAL),
    BLOCK_PLACE(PermissionType.GLOBAL),
    CHESTS(PermissionType.GLOBAL),
    FURNACES(PermissionType.GLOBAL),
    DOORS(PermissionType.GLOBAL),
    BUTTONS(PermissionType.GLOBAL),
    PLATES(PermissionType.GLOBAL),
    ANVIL(PermissionType.GLOBAL),
    BREWING(PermissionType.GLOBAL),
    ANIMALS(PermissionType.GLOBAL),
    LEASH(PermissionType.GLOBAL),
    VEHICLES(PermissionType.GLOBAL),
    REDSTONE(PermissionType.GLOBAL),
    ARMOR_STAND(PermissionType.GLOBAL),
    COMPOSTING(PermissionType.GLOBAL),
    BEACON(PermissionType.GLOBAL),
    LECTERN(PermissionType.GLOBAL),

    /**
     * Moderator Permissions.
     */
    WITHDRAW(PermissionType.MODERATOR),
    RENAME(PermissionType.MODERATOR),
    INVITE(PermissionType.MODERATOR),
    KICK(PermissionType.MODERATOR),
    SPAWN(PermissionType.MODERATOR),
    OUTPOST(PermissionType.MODERATOR),
    VAULT(PermissionType.MODERATOR),
    PLOTS_ASSIGN(PermissionType.MODERATOR),
    PLOTS_EVICT(PermissionType.MODERATOR),
    PLOTS_TYPE(PermissionType.MODERATOR),
    PLOTS_MODIFY(PermissionType.MODERATOR),
    PLOTS_RENAME(PermissionType.MODERATOR),
    PUNISH(PermissionType.MODERATOR),
    UPGRADES(PermissionType.MODERATOR),
    TRUST(PermissionType.MODERATOR),
    CLAIM_UNCLAIM(PermissionType.MODERATOR);

    private final PermissionType permissionType;

}
