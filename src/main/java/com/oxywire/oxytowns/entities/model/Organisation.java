package com.oxywire.oxytowns.entities.model;

/**
 * @param <T> Type of Identifier for the members.
 */
public interface Organisation<T> {

    /**
     * @param entity Identifier of the entity to cross-reference with members.
     * @return Whether the entity is currently a member.
     */
    boolean isMember(T entity);
}
