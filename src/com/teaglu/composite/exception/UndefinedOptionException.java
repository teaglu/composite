/****************************************************************************
 * Copyright 2022 Teaglu, LLC                                               *
 *                                                                          *
 * Licensed under the Apache License, Version 2.0 (the "License");          *
 * you may not use this file except in compliance with the License.         *
 * You may obtain a copy of the License at                                  *
 *                                                                          *
 *   http://www.apache.org/licenses/LICENSE-2.0                             *
 *                                                                          *
 * Unless required by applicable law or agreed to in writing, software      *
 * distributed under the License is distributed on an "AS IS" BASIS,        *
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. *
 * See the License for the specific language governing permissions and      *
 * limitations under the License.                                           *
 ****************************************************************************/

package com.teaglu.composite.exception;

/**
 * UndefinedOptionException
 *
 * This exception is thrown when a parameter represents an enumeration or set of valid values,
 * and the value present isn't valid.  This isn't normally thrown by the library itself because
 * it doesn't have enumeration support - it's meant to allow a bad value exception to be
 * included under SchemaException.
 * 
 */
public class UndefinedOptionException extends SchemaException {
	private static final long serialVersionUID = 1L;

	public UndefinedOptionException(String message) {
		super(message);
	}
}
