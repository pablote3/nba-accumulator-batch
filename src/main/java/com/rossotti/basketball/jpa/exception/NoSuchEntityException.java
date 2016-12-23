package com.rossotti.basketball.jpa.exception;

/**
 * Exception thrown when the requested entity does not exist.
 */
public class NoSuchEntityException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	private final Class<?> entityClass;

	public NoSuchEntityException(Class<?> entityClass) {
		super("The entity \"" + entityClass + "\" does not exist.");
		this.entityClass = entityClass;
	}
	public Class<?> getEntityClass() {
		return entityClass;
	}
}
