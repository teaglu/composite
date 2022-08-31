/****************************************************************************
 * Copyright (c) 2022 Teaglu, LLC
 * All Rights Reserved
 ****************************************************************************/

package com.teaglu.composite.exception;

public class WrongTypeException extends SchemaException {
	private static final long serialVersionUID = 1L;

	public WrongTypeException(String parameter, String expectedType) {
		super("Parameter " + parameter + " was expected to be type " + expectedType + " and was not");
	}
}
