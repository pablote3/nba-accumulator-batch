package com.rossotti.basketball.jpa.exception;

/**
 * Exception thrown when attempting to create an entity that already exists.
 */
public class DuplicateEntityException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	private final Class<?> entityClass;

	public DuplicateEntityException(Class<?> entityClass) {
		super("The entity \"" + entityClass + "\" already exists.");
		this.entityClass = entityClass;
	}
	public Class<?> getEntityClass() {
		return entityClass;
	}
}
