package com.teaglu.composite.exception;

public class UnsupportedSerializationException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public UnsupportedSerializationException(Class<?> representationClass) {
		super("Unable to serialize to " + representationClass.getCanonicalName());
	}
}
