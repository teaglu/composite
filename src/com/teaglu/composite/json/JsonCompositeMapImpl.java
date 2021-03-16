package com.teaglu.composite.json;

import java.time.ZoneId;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNull;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.teaglu.composite.Composite;

public final class JsonCompositeMapImpl implements Iterable<Map.Entry<@NonNull String, @NonNull Composite>> {	
	private static class ObjectMapIterator implements Iterator<Map.Entry<@NonNull String, @NonNull Composite>> {
		Iterator<Map.Entry<String, JsonElement>> iterator;
		ZoneId zoneId;
		
		private ObjectMapIterator(JsonObject object, ZoneId zoneId) {
			iterator= object.entrySet().iterator();
		}
		
		@Override
		public boolean hasNext() {
			return iterator.hasNext();
		}

		@Override
		public Map.Entry<@NonNull String, @NonNull Composite> next() {
			final Map.Entry<String, JsonElement> el= iterator.next();
			
			return new Map.Entry<@NonNull String, @NonNull Composite>() {
				@SuppressWarnings("null")
				@Override
				public @NonNull String getKey() {
					return el.getKey();
				}

				@SuppressWarnings("null")
				@Override
				public @NonNull Composite getValue() {
					return new JsonCompositeImpl(el.getValue().getAsJsonObject(), zoneId);
				}

				@Override
				public @NonNull Composite setValue(@NonNull Composite value) {
					throw new RuntimeException("setValue() not implemented");
				}
			};
		}
	}
	
	private JsonObject object;
	private ZoneId zoneId;
	
	JsonCompositeMapImpl(JsonObject object, ZoneId zoneId) {
		this.object= object;
		this.zoneId= zoneId;
	}
	
	@Override
	public Iterator<Map.Entry<@NonNull String, @NonNull Composite>> iterator() {
		return new ObjectMapIterator(object, zoneId);
	}
}
