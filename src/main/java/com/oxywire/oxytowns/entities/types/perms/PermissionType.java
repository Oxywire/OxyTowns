package com.oxywire.oxytowns.entities.types.perms;

import com.oxywire.oxytowns.entities.types.Role;
import lombok.AllArgsConstructor;

import java.util.EnumSet;
import java.util.Set;

/**
 * Type of the Permission - Who it's applicable to.
 */
@AllArgsConstructor
public enum PermissionType {

    MAYOR(EnumSet.of(Role.MAYOR)),
    MODERATOR(EnumSet.of(Role.MEMBER, Role.HELPER, Role.BUILDER, Role.SENATOR, Role.CO_MAYOR)),
    GLOBAL(EnumSet.of(Role.OUTSIDER, Role.TRUSTED, Role.MEMBER, Role.BUILDER, Role.HELPER, Role.SENATOR, Role.CO_MAYOR, Role.MAYOR));

    private final Set<Role> includedRoles;

    /**
     * Check if a role inherits another role or not.
     *
     * @param role the role to check
     * @return if the role inherits other roles or not
     */
    public boolean inherits(final Role role) {
        return this.includedRoles.contains(role);
    }
}
