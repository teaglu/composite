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
 * JsonCompositeArrayImpl
 *
 * Implementation of an array of objects
 */
public final class MapCompositeArrayImpl implements Iterable<@NonNull Composite> {
	public class MapCompositeIterator implements Iterator<@NonNull Composite>  {
		private Iterator<?> iterator;
		private int index;
		
		private MapCompositeIterator() {
			iterator= iterable.iterator();
		}
		
		@Override
		public boolean hasNext() {
			return iterator.hasNext();
		}

		@Override
		public @NonNull Composite next() {
			Object object= iterator.next();
			if (object == null) {
				throw new RuntimeException("Null item in iterable");
			}

			Map<?, ?> genericMap= (Map<?, ?>)object;
			
			@SuppressWarnings("unchecked")
			Map<String, Object> map= (Map<String, Object>)genericMap;

			int position= index++;
			return new MapCompositeImpl(map, timezone, serializer, path + "[" + position + "]");
		}
	}
	
	private @NonNull Iterable<?> iterable;
	private @NonNull String path;
	private @NonNull TimeZone timezone;
	private @NonNull MapSerializer serializer;
	
	MapCompositeArrayImpl(
			@NonNull String path,
			@NonNull Iterable<?> iterable,
			@NonNull TimeZone timezone,
			@NonNull MapSerializer serializer) throws WrongTypeException
	{
		this.path= path;
		
		// Verify all the entries are objects.  We can't do that in the iterator because the
		// iterator methods don't have any throw clauses.
		
		int index= 0;
		for (Object object : iterable) {
			if (!(object instanceof Map<?, ?>)) {
				throw new WrongTypeException(
						path + "[" + index + "]", "Map<String, Object>");
			}
		}
		
		this.iterable= iterable;
		this.timezone= timezone;
		this.serializer= serializer;
	}
	
	@Override
	public Iterator<@NonNull Composite> iterator() {
		return new MapCompositeIterator();
	}
}
