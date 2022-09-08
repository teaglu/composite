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

import java.time.ZoneId;
import java.util.Iterator;
import org.eclipse.jdt.annotation.NonNull;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.teaglu.composite.Composite;
import com.teaglu.composite.exception.WrongTypeException;

/**
 * JsonCompositeArrayImpl
 *
 * Implementation of an array of objects
 */
public final class JsonCompositeArrayImpl implements Iterable<@NonNull Composite> {
	public class JsonArrayIterator implements Iterator<@NonNull Composite>  {
		private @NonNull Iterator<@NonNull JsonElement> iterator;
		
		JsonArrayIterator()
		{
			@SuppressWarnings("null")
			@NonNull Iterator<@NonNull JsonElement> tmp= array.iterator();
			
			iterator= tmp;
		}
		
		@Override
		public boolean hasNext() {
			return iterator.hasNext();
		}

		@Override
		public @NonNull Composite next() {
			@SuppressWarnings("null")
			@NonNull JsonElement el= iterator.next();
			
			@SuppressWarnings("null")
			@NonNull JsonObject ob= el.getAsJsonObject();
			
			return new JsonCompositeImpl(ob, zoneId);
		}
	}
	
	private @NonNull JsonArray array;
	private @NonNull ZoneId zoneId;
	
	JsonCompositeArrayImpl(
			@NonNull String name,
			@NonNull JsonArray array,
			@NonNull ZoneId zoneId) throws WrongTypeException
	{
		// Verify all the entries are objects.  We can't do that in the iterator because the
		// iterator methods don't have any throw clauses.
		
		int entryNo= 0;
		for (JsonElement el : array) {
			if (!el.isJsonObject()) {
				throw new WrongTypeException(name + "[" + entryNo + "]", "Object");
			}
			entryNo++;
		}
		
		this.array= array;
		this.zoneId= zoneId;
	}
	
	@Override
	public Iterator<@NonNull Composite> iterator() {
		return new JsonArrayIterator();
	}
}
