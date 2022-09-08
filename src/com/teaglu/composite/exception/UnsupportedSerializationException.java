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
 * UnsupportedSerializationException
 *
 * Exception throws if you ask for serialization to a class and the library doesn't
 * support that serialization.  The current implementations wrap native classes
 * from GSON or YAML, so they're only capable of serializing back to the original
 * format.
 * 
 */
public class UnsupportedSerializationException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public UnsupportedSerializationException(Class<?> representationClass) {
		super("Unable to serialize to " + representationClass.getCanonicalName());
	}
}
