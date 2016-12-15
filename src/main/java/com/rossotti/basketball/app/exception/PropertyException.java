package com.rossotti.basketball.app.exception;

/**
 * Exception thrown when unable to evaluate a read property.
 */
public class PropertyException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public PropertyException(String key) {
		super("Invalid property value for " + key);
	}
}
