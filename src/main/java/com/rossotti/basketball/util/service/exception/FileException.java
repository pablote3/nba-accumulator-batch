package com.rossotti.basketball.util.service.exception;

/**
 * Exception thrown when unable to handle file.
 */
public class FileException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public FileException(String exception) {
		super("File Exception " + exception);
	}
}
