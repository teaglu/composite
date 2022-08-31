/****************************************************************************
 * Copyright (c) 2022 Teaglu, LLC
 * All Rights Reserved
 ****************************************************************************/

package com.teaglu.composite.json;

import java.time.ZoneId;
import java.util.Iterator;

import org.eclipse.jdt.annotation.NonNull;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.teaglu.composite.Composite;

public final class JsonCompositeArrayImpl implements Iterable<@NonNull Composite> {
	public class JsonArrayIterator implements Iterator<@NonNull Composite>  {
		Iterator<@NonNull JsonElement> iterator;
		ZoneId zoneId;
		
		@SuppressWarnings("null")	// JsonArray.iterator() is not @NonNull aware
		JsonArrayIterator(@NonNull JsonArray array, @NonNull ZoneId zoneId) {
			iterator= array.iterator();
			this.zoneId= zoneId;
		}
		
		@Override
		public boolean hasNext() {
			return iterator.hasNext();
		}

		@SuppressWarnings("null")	// iterator.next() is not @NonNull aware
		@Override
		public @NonNull Composite next() {
			@NonNull JsonElement el= iterator.next();
			
			return new JsonCompositeImpl(el.getAsJsonObject(), zoneId);
		}
	}
	
	private @NonNull JsonArray array;
	private @NonNull ZoneId zoneId;
	
	JsonCompositeArrayImpl(@NonNull JsonArray array, @NonNull ZoneId zoneId) {
		this.array= array;
		this.zoneId= zoneId;
	}
	
	@Override
	public Iterator<@NonNull Composite> iterator() {
		return new JsonArrayIterator(array, zoneId);
	}
}
