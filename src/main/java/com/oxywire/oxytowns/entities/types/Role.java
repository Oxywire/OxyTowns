package com.oxywire.oxytowns.entities.types;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Role {
    MAYOR(8),
    CO_MAYOR(7),
    SENATOR(6),
    HELPER(5),
    BUILDER(4),
    MEMBER(3),
    TRUSTED(2),
    OUTSIDER(1);

    private final int priority;

    /**
     * Get the role by priority.
     *
     * @param priority the priority to get
     * @return the role to get if it exists
     */
    public static Role getRoleByPriority(final int priority) {
        for (Role role : values()) {
            if (role.priority == priority) {
                return role;
            }
        }

        return null;
    }
}
