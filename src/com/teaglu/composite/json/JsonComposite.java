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

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.postgresql.util.PGobject;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.teaglu.composite.Composite;
import com.teaglu.composite.exception.ParseException;
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
	 * @throws WrongTypeException		Element is not an object
	 */
	public static @NonNull Composite Create(
			@NonNull JsonElement element,
			@NonNull TimeZone timezone) throws WrongTypeException
	{
		if (!element.isJsonObject()) {
			throw new WrongTypeException("root", "Object");
		}
		
		@SuppressWarnings("null")
		@NonNull JsonObject object= element.getAsJsonObject();
		
		return new JsonCompositeImpl(object, timezone, null);
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
	 * @throws WrongTypeException		Element is not an object
	 */
	public static @NonNull Composite Create(
			@NonNull JsonElement element) throws WrongTypeException
	{
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
	 * @throws WrongTypeException		Input does not represent and object
	 * @throws JsonSyntaxException		Unable to parse input
	 */
	public static @NonNull Composite Parse(
			@NonNull String data,
			@NonNull TimeZone timezone) throws ParseException, WrongTypeException
	{
		try {
			JsonElement element= JsonParser.parseString(data);
			if (element == null) {
				throw new JsonSyntaxException("Unable to parse JSON data");
			}
	
			return Create(element, timezone);
		} catch (JsonParseException parseException) {
			throw new ParseException("Error parsing JSON input", parseException);
		}
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
	 * @throws WrongTypeException		Input is not an object
	 * @throws JsonSyntaxException		Unable ot parse input
	 */
	public static @NonNull Composite Parse(
			@NonNull String data) throws ParseException, WrongTypeException
	{
		return Parse(data, defaultTimezone);
	}
	
	/**
	 * Parse
	 * 
	 * Parse a Composite from JSON read from an InputStreamReader.
	 *
	 * @param reader					Input source
	 * @param timezone					Timezone for interpretation
	 * 
	 * @return							New Composite
	 * 
	 * @throws WrongTypeException		Input is not an object
	 * @throws JsonSyntaxException		Unable to parse input
	 */
	public static @NonNull Composite Parse(
			@NonNull InputStreamReader reader,
			@NonNull TimeZone timezone) throws ParseException, WrongTypeException
	{
		try {
			JsonElement element= JsonParser.parseReader(reader);
			if (element == null) {
				throw new JsonSyntaxException("Unable to parse JSON data");
			}
	
			return Create(element, timezone);
		} catch (JsonParseException parseException) {
			throw new ParseException("Error parsing JSON input", parseException);
		}
	}
	
	/**
	 * Parse
	 * 
	 * Parse a Composite from JSON read from an InputStreamReader, assuming UTC as the timezone
	 * for interpretation.
	 *
	 * @param reader					Input source
	 * 
	 * @return							New Composite
	 * 
	 * @throws WrongTypeException		Input is not an object
	 * @throws JsonSyntaxException		Unable to parse input
	 */
	public static @NonNull Composite Parse(
			@NonNull InputStreamReader reader) throws ParseException, WrongTypeException
	{
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
	 * @throws WrongTypeException		Field does not contain a JSON object
	 */
	public static @Nullable Composite ParseObject(
			@Nullable PGobject pgObject,
			@NonNull TimeZone timezone) throws ParseException, WrongTypeException
	{
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
	 * @throws WrongTypeException		Field does not contain a JSON object
	 */
	public static @Nullable Composite ParseObject(
			@Nullable PGobject pgObject) throws ParseException, WrongTypeException
	{
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
	 * @throws WrongTypeException		Field does not contain a JSON array
	 */
	public static @NonNull List<@NonNull Composite> ParseArray(
			@Nullable PGobject pgObject,
			@NonNull TimeZone timezone) throws ParseException, WrongTypeException
	{
		try {
			List<@NonNull Composite> rval= new ArrayList<>();
			
			if (pgObject != null) {
				String value= pgObject.getValue();
				if ((value != null) && !value.equals("null")) {
					JsonElement element= JsonParser.parseString(pgObject.getValue());
					
					if (element.isJsonObject()) {
						rval.add(Create(element, timezone));
					} else if (element.isJsonArray()) {
						for (JsonElement subElement : element.getAsJsonArray()) {
							@SuppressWarnings("null")
							@NonNull JsonElement requiredSubElement= subElement;
							
							rval.add(Create(requiredSubElement, timezone));
						}
					}
				}
			}
			
			return rval;
		} catch (JsonParseException parseException) {
			throw new ParseException("Error parsing JSON", parseException);
		}
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
	 * @throws WrongTypeException		Field does not contain a JSON array
	 */
	public static @Nullable List<@NonNull Composite> ParseArray(
			@Nullable PGobject pgObject) throws ParseException, WrongTypeException
	{
		return ParseArray(pgObject, defaultTimezone);
	}
}
