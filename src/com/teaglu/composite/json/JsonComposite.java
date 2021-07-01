/****************************************************************************
 * Copyright (c) 2020 Teaglu, LLC
 * All Rights Reserved
 ****************************************************************************/

package com.teaglu.composite.json;

import java.io.InputStreamReader;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.postgresql.util.PGobject;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.teaglu.composite.Composite;
import com.teaglu.composite.exception.WrongTypeException;

/**
 * JsonComposite
 * 
 * Implementation of Composite based on GSON Json
 * 
 * Some aspects such as Timestamp handling require knowledge of the local timezone, which
 * is passed in as part of object creation.  If you don't know the timezone or it doesn't matter
 * because you're not using those datatypes, the default is to use UTC time.
 * 
 * The default is UTC, because using the system timezone would introduce an unwanted reliance
 * on system setup from a testing standpoint.
 * 
 * Also, for the most part UTC time is *not* what people want as a default, so this will force
 * them to pass in a proper timezone to get the expected result.
 * 
 */
public final class JsonComposite {
	private static final @NonNull TimeZone defaultTimezone;
	
	static {
		TimeZone utc= TimeZone.getTimeZone("UTC");
		if (utc == null) {
			throw new RuntimeException("Unable to resolve UTC for default timezone");
		}
		
		defaultTimezone= utc;
	}
	
	/**
	 * Create
	 * 
	 * Create a Composite from a JsonElement.  Throws WrongTypeException if the underlying
	 * JSON element is not an object.
	 *
	 * @param element					JSON element
	 * @param timezone					Timezone for interpretation
	 * 
	 * @return							New Composite
	 * 
	 * @throws WrongTypeException
	 */
	public static @NonNull Composite Create(@NonNull JsonElement element, @NonNull TimeZone timezone) throws WrongTypeException {
		if (!element.isJsonObject()) {
			throw new WrongTypeException("root", "Object");
		}
		
		@SuppressWarnings("null") @NonNull JsonObject object= element.getAsJsonObject();
		@SuppressWarnings("null") @NonNull ZoneId zoneId= timezone.toZoneId();
		
		return new JsonCompositeImpl(object, zoneId);
	}
	
	/**
	 * Create
	 * 
	 * Create a Composite from a JsonElement using the UTC timezone.  Throws WrongTypeException
	 * if the underlying JSON element is not an object.
	 *
	 * @param element					JSON element
	 * 
	 * @return							New Composite
	 * 
	 * @throws WrongTypeException
	 */
	public static @NonNull Composite Create(@NonNull JsonElement element) throws WrongTypeException {
		return Create(element, defaultTimezone);
	}
	
	/**
	 * Parse
	 * 
	 * Parse a Composite from a string in JSON format.
	 *
	 * @param data						String in JSON format
	 * @param timezone					Timezone for interpretation
	 * 
	 * @return							New Composite
	 * 
	 * @throws WrongTypeException
	 * @throws JsonSyntaxException
	 */
	public static @NonNull Composite Parse(@NonNull String data, @NonNull TimeZone timezone) throws WrongTypeException, JsonSyntaxException {
		JsonElement element= JsonParser.parseString(data);
		if (element == null) {
			throw new JsonSyntaxException("Unable to parse JSON data");
		}

		return Create(element, timezone);
	}
	
	/**
	 * Parse
	 * 
	 * Parse a Composite from a string in JSON format, assuming UTC for interpretation
	 *
	 * @param data						String in JSON format
	 * 
	 * @return							New Composite
	 * 
	 * @throws WrongTypeException
	 * @throws JsonSyntaxException
	 */
	public static @NonNull Composite Parse(@NonNull String data) throws WrongTypeException, JsonSyntaxException {
		return Parse(data, defaultTimezone);
	}
	
	/**
	 * Parse
	 * 
	 * Parse a Composite from JSON read from an InputStreamReader.
	 *
	 * @param data						String in JSON format
	 * @param timezone					Timezone for interpretation
	 * 
	 * @return							New Composite
	 * 
	 * @throws WrongTypeException
	 * @throws JsonSyntaxException
	 */
	public static @NonNull Composite Parse(@NonNull InputStreamReader reader, @NonNull TimeZone timezone) throws WrongTypeException, JsonSyntaxException {
		JsonElement element= JsonParser.parseReader(reader);
		if (element == null) {
			throw new JsonSyntaxException("Unable to parse JSON data");
		}

		return Create(element, timezone);
	}
	
	/**
	 * Parse
	 * 
	 * Parse a Composite from JSON read from an InputStreamReader, assuming UTC as the timezone
	 * for interpretation.
	 *
	 * @param data						String in JSON format
	 * 
	 * @return							New Composite
	 * 
	 * @throws WrongTypeException
	 * @throws JsonSyntaxException
	 */
	public static @NonNull Composite Parse(@NonNull InputStreamReader reader) throws WrongTypeException, JsonSyntaxException {
		return Parse(reader, defaultTimezone);
	}
	
	/**
	 * ParseObject
	 * 
	 * Parse a Postgres PGobject from a JSON column and create an object
	 *
	 * @param pgObject					Object from JDBC getObject()
	 * @param timezone					Timezone for interpretation
	 * 
	 * @return							Composite object
	 * 
	 * @throws WrongTypeException
	 */
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
	
	/**
	 * ParseObject
	 * 
	 * Parse a Postgres PGobject from a JSON column and create an object
	 *
	 * @param pgObject					Object from JDBC getObject()
	 * 
	 * @return							Composite object
	 * 
	 * @throws WrongTypeException
	 */
	public static @Nullable Composite ParseObject(PGobject pgObject) throws WrongTypeException {
		return ParseObject(pgObject, defaultTimezone);
	}

	/**
	 * ParseArray
	 * 
	 * Parse a Postgres PGobject from a JSON column and create an array of objects
	 *
	 * @param pgObject					Object from JDBC getObject()
	 * @param timezone					Timezone for interpretation
	 * 
	 * @return							List of Composite objects
	 * 
	 * @throws WrongTypeException
	 */
	public static @NonNull List<@NonNull Composite> ParseArray(PGobject pgObject, @NonNull TimeZone timezone) throws WrongTypeException {
		List<@NonNull Composite> rval= new ArrayList<>();
		
		if (pgObject != null) {
			String value= pgObject.getValue();
			if ((value != null) && !value.equals("null")) {
				JsonElement element= JsonParser.parseString(pgObject.getValue());
				
				if (element.isJsonObject()) {
					rval.add(Create(element, timezone));
				} else if (element.isJsonArray()) {
					for (JsonElement subElement : element.getAsJsonArray()) {
						@SuppressWarnings("null") @NonNull JsonElement requiredSubElement= subElement;
						
						rval.add(Create(requiredSubElement, timezone));
					}
				}
			}
		}
		
		return rval;
	}
	
	/**
	 * ParseArray
	 * 
	 * Parse a Postgres PGobject from a JSON column and create an array
	 *
	 * @param pgObject					Object from JDBC getObject()
	 * 
	 * @return							Composite object
	 * 
	 * @throws WrongTypeException
	 */
	public static @Nullable List<@NonNull Composite> ParseArray(PGobject pgObject) throws WrongTypeException {
		return ParseArray(pgObject, defaultTimezone);
	}
}
