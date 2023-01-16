package com.teaglu.composite.exception;

/**
 * ParseException
 *
 * Exception thrown when an input structure cannot be parsed.
 * 
 * (This was originally added to wrap the Gson exceptions - Gson decided to make their errors
 * unchecked so it's super-easy for those to leak into code and not get handled.)
 * 
 */
public class ParseException extends SchemaException {
	private static final long serialVersionUID = 1L;

	public ParseException(String message) {
		super(message);
	}

	public ParseException(String message, Throwable cause) {
		super(message, cause);
	}
}
