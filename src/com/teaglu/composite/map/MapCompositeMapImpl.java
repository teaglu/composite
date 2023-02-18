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

package com.teaglu.composite.map;

import java.util.Iterator;
import java.util.Map;
import java.util.TimeZone;

import org.eclipse.jdt.annotation.NonNull;

import com.teaglu.composite.Composite;
import com.teaglu.composite.exception.WrongTypeException;

/**
 * JsonCompositeMapImpl
 *
 * Implementation of an iterable set of map entries that corresponds to all the entries of a
 * composite.  The entries must all be objects.
 * 
 */
public final class MapCompositeMapImpl implements Iterable<Map.Entry<@NonNull String, @NonNull Composite>> {	
	private class ObjectMapIterator implements Iterator<Map.Entry<@NonNull String, @NonNull Composite>> {
		private Iterator<Map.Entry<String, Object>> iterator;
		
		private ObjectMapIterator() {
			iterator= members.entrySet().iterator();
		}
		
		@Override
		public boolean hasNext() {
			return iterator.hasNext();
		}

		@Override
		public Map.Entry<@NonNull String, @NonNull Composite> next() {
			Map.Entry<String, Object> entry= iterator.next();
			
			final String name= entry.getKey();
			if (name == null) {
				throw new RuntimeException("Map key is unexpectedly null");
			}
			
			final Object value= entry.getValue();
			if (value == null) {
				throw new RuntimeException("Value key is unexpectedly null");
			}
			
			if (!(value instanceof Map)) {
				throw new RuntimeException("Value key is not a map instance");
			}
			
			return new Map.Entry<@NonNull String, @NonNull Composite>() {
				@Override
				public @NonNull String getKey() {
					return name;
				}

				@Override
				public @NonNull Composite getValue() {
					@SuppressWarnings("unchecked")
					Map<String, Object> map= (Map<String, Object>)value;
					
					return new MapCompositeImpl(map, timezone, serializer, prefix + name);
				}

				@Override
				public @NonNull Composite setValue(@NonNull Composite value) {
					throw new RuntimeException("setValue() not implemented");
				}
			};
		}
	}
	
	private @NonNull Map<String, Object> members;
	private @NonNull TimeZone timezone;
	private @NonNull MapSerializer serializer;
	private @NonNull String prefix;
	
	MapCompositeMapImpl(
			@NonNull Map<String, Object> members,
			@NonNull TimeZone timezone,
			@NonNull MapSerializer serializer,
			@NonNull String prefix) throws WrongTypeException
	{
		this.members= members;
		this.timezone= timezone;
		this.serializer= serializer;
		this.prefix= prefix;
		
		// Pre-scan the entry set to make sure it's only objects, since we can only throw an
		// unchecked exception from the iterator.
		for (Map.Entry<String, Object> entry : members.entrySet()) {
			String name= entry.getKey();
			Object value= entry.getValue();
			
			if (value == null) {
				throw new WrongTypeException(prefix + name, "object");
			}
			
			if (!(value instanceof Map)) {
				throw new WrongTypeException(prefix + name, "object");
			}
		}
	}
	
	@Override
	public Iterator<Map.Entry<@NonNull String, @NonNull Composite>> iterator() {
		return new ObjectMapIterator();
	}
}
