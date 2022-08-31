/****************************************************************************
 * Copyright (c) 2022 Teaglu, LLC
 * All Rights Reserved
 ****************************************************************************/

package com.teaglu.composite.exception;

public class SchemaException extends Exception {
	private static final long serialVersionUID = 1L;
	
	protected SchemaException(String message) {
		super(message);
	}
	protected SchemaException(String message, Throwable cause) {
		super(message, cause);
	}
}
