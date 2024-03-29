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

package com.teaglu.composite.json;

import java.util.Iterator;
import java.util.Map;
import java.util.TimeZone;

import org.eclipse.jdt.annotation.NonNull;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.teaglu.composite.Composite;
import com.teaglu.composite.exception.WrongTypeException;

/**
 * JsonCompositeMapImpl
 *
 * Implementation of an iterable set of map entries that corresponds to all the entries of a
 * composite.  The entries must all be objects.
 * 
 */
public final class JsonCompositeMapImpl implements Iterable<Map.Entry<@NonNull String, @NonNull Composite>> {	
	private class ObjectMapIterator implements Iterator<Map.Entry<@NonNull String, @NonNull Composite>> {
		private @NonNull Iterator<Map.Entry<@NonNull String, @NonNull JsonElement>> iterator;
		
		private ObjectMapIterator(@NonNull JsonObject object) {
			@SuppressWarnings("null")
			@NonNull Iterator<Map.Entry<@NonNull String, @NonNull JsonElement>> tmp=
					object.entrySet().iterator();
			
			iterator= tmp;
		}
		
		@Override
		public boolean hasNext() {
			return iterator.hasNext();
		}

		@Override
		public Map.Entry<@NonNull String, @NonNull Composite> next() {
			final Map.Entry<@NonNull String, @NonNull JsonElement> entry= iterator.next();
			
			@SuppressWarnings("null") final String name= entry.getKey();
			@SuppressWarnings("null") final JsonElement element= entry.getValue();
			
			if (!element.isJsonObject()) {
				throw new RuntimeException("Map entry is not an object");
			}
			
			@SuppressWarnings("null")
			final @NonNull JsonObject object= element.getAsJsonObject();
			
			return new Map.Entry<@NonNull String, @NonNull Composite>() {
				@Override
				public @NonNull String getKey() {
					return name;
				}

				@Override
				public @NonNull Composite getValue() {
					return new JsonCompositeImpl(object, timezone, prefix + name);
				}

				@Override
				public @NonNull Composite setValue(@NonNull Composite value) {
					throw new RuntimeException("setValue() not implemented");
				}
			};
		}
	}
	
	private @NonNull JsonObject object;
	private @NonNull TimeZone timezone;
	private @NonNull String prefix;
	
	JsonCompositeMapImpl(
			@NonNull JsonObject object,
			@NonNull TimeZone timezone,
			@NonNull String prefix) throws WrongTypeException
	{
		// Verify all the entries are objects.  We can't do that in the iterator because the
		// iterator methods don't have any throw clauses.
		
		for (Map.Entry<String, JsonElement> entry : object.entrySet()) {
			JsonElement el= entry.getValue();
			if (!el.isJsonObject()) {
				throw new WrongTypeException(entry.getKey(), "Object");
			}
		}
		
		this.object= object;
		this.timezone= timezone;
		this.prefix= prefix;
	}
	
	@Override
	public Iterator<Map.Entry<@NonNull String, @NonNull Composite>> iterator() {
		return new ObjectMapIterator(object);
	}
}
