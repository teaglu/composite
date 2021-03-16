/****************************************************************************
 * Copyright (c) 2020 Teaglu, LLC
 * All Rights Reserved
 ****************************************************************************/

package com.teaglu.composite.json;

import java.util.TimeZone;

import org.eclipse.jdt.annotation.NonNull;

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
	public static @NonNull Composite Create(@NonNull JsonElement element, @NonNull TimeZone timezone) throws WrongTypeException {
		if (!element.isJsonObject()) {
			throw new WrongTypeException("root", "Object");
		}
		
		return new JsonCompositeImpl(element.getAsJsonObject(), timezone.toZoneId());
	}
	
	@SuppressWarnings("null")
	public static Composite Parse(@NonNull String data, @NonNull TimeZone timezone) throws WrongTypeException, JsonSyntaxException {
		JsonElement element= JsonParser.parseString(data);

		return Create(element, timezone);
	}
}
