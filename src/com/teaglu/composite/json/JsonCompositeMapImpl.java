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
		private Iterator<Map.Entry<String, JsonElement>> iterator;
		private @NonNull ZoneId zoneId;
		
		private ObjectMapIterator(@NonNull JsonObject object, @NonNull ZoneId zoneId) {
			iterator= object.entrySet().iterator();
			this.zoneId= zoneId;
		}
		
		@Override
		public boolean hasNext() {
			return iterator.hasNext();
		}

		@Override
		public Map.Entry<@NonNull String, @NonNull Composite> next() {
			final Map.Entry<String, JsonElement> el= iterator.next();
			
			return new Map.Entry<@NonNull String, @NonNull Composite>() {
				// Gson doesn't support null annotations, so we have to eat the warning somewhere
				@SuppressWarnings("null")
				@Override
				public @NonNull String getKey() {
					return el.getKey();
				}

				// Gson doesn't support null annotations, so we have to eat the warning somewhere
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
	
	private @NonNull JsonObject object;
	private @NonNull ZoneId zoneId;
	
	JsonCompositeMapImpl(@NonNull JsonObject object, @NonNull ZoneId zoneId) {
		this.object= object;
		this.zoneId= zoneId;
	}
	
	@Override
	public Iterator<Map.Entry<@NonNull String, @NonNull Composite>> iterator() {
		return new ObjectMapIterator(object, zoneId);
	}
}
