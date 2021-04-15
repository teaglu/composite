/****************************************************************************
 * Copyright (c) 2020 Teaglu, LLC
 * All Rights Reserved
 ****************************************************************************/

package com.teaglu.composite.json;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.postgresql.util.PGobject;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.teaglu.composite.Composite;
import com.teaglu.composite.exception.WrongTypeException;

/**
 * JsonComposite
 * 
 * Implementation of Composite based on GSON Json
 * 
 */
public final class JsonComposite {	
	@SuppressWarnings("null")
	private static final @NonNull TimeZone defaultTimezone= TimeZone.getDefault();
	
	@SuppressWarnings("null")
	public static @NonNull Composite Create(@NonNull JsonElement element, @NonNull TimeZone timezone) throws WrongTypeException {
		if (!element.isJsonObject()) {
			throw new WrongTypeException("root", "Object");
		}
		
		return new JsonCompositeImpl(element.getAsJsonObject(), timezone.toZoneId());
	}
	
	public static @NonNull Composite Create(@NonNull JsonElement element) throws WrongTypeException {
		return Create(element, defaultTimezone);
	}
	
	@SuppressWarnings("null")
	public static @NonNull Composite Parse(@NonNull String data, @NonNull TimeZone timezone) throws WrongTypeException, JsonSyntaxException {
		JsonElement element= JsonParser.parseString(data);

		return Create(element, timezone);
	}
	
	public static @NonNull Composite Parse(@NonNull String data) throws WrongTypeException, JsonSyntaxException {
		return Parse(data, defaultTimezone);
	}
	
	@SuppressWarnings("null")
	public static @NonNull Composite Parse(@NonNull InputStreamReader reader, @NonNull TimeZone timezone) throws WrongTypeException, JsonSyntaxException {
		JsonElement element= JsonParser.parseReader(reader);

		return Create(element, timezone);
	}
	
	public static @NonNull Composite Parse(@NonNull InputStreamReader reader) throws WrongTypeException, JsonSyntaxException {
		return Parse(reader, defaultTimezone);
	}
	
	public static @Nullable Composite ParseObject(PGobject pgObject, @NonNull TimeZone timezone) throws WrongTypeException {
		Composite rval= null;
		
		if (pgObject != null) {
			String value= pgObject.getValue();
			if ((value != null) && !value.equals("null")) {
				rval= Parse(value, timezone);
			}
		}
		
		return rval;
	}

	@SuppressWarnings("null")
	public static @NonNull List<@NonNull Composite> ParseArray(PGobject pgObject, @NonNull TimeZone timezone) throws WrongTypeException {
		List<@NonNull Composite> rval= new ArrayList<>();
		
		if (pgObject != null) {
			String value= pgObject.getValue();
			if ((value != null) && !value.equals("null")) {
				JsonElement el= JsonParser.parseString(pgObject.getValue());
				
				if (el.isJsonObject()) {
					rval.add(Create(el, timezone));
				} else if (el.isJsonArray()) {
					for (JsonElement subEl : el.getAsJsonArray()) {
						rval.add(Create(subEl, timezone));
					}
				}
			}
		}
		
		return rval;
	}
}
