/****************************************************************************
 * Copyright (c) 2022 Teaglu, LLC
 * All Rights Reserved
 ****************************************************************************/

package com.teaglu.composite.exception;

public class MissingValueException extends SchemaException {
	private static final long serialVersionUID = 1L;

	public MissingValueException(String parameterName) {
		super("Missing parameter " + parameterName);
	}
}
