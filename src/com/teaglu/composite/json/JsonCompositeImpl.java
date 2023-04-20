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

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.teaglu.composite.Composite;
import com.teaglu.composite.exception.FormatException;
import com.teaglu.composite.exception.MissingValueException;
import com.teaglu.composite.exception.UnsupportedSerializationException;
import com.teaglu.composite.exception.WrongTypeException;

/**
 * JsonCompositeImpl
 * 
 * This is the meat of the JSON composite implementation - or more or less all the null and
 * type safety checks we'd have to write otherwise collected in one place.
 *
 */
public final class JsonCompositeImpl implements Composite {
	private @NonNull String prefix;
	private @NonNull TimeZone timezone;
	private @NonNull JsonObject object;

	public JsonCompositeImpl(@NonNull JsonObject object, @NonNull TimeZone timezone, @Nullable String path) {
		this.object= object;
		this.timezone= timezone;
		
		if (path == null) {
			prefix= "";
		} else {
			prefix= path + ".";
		}
	}
	
	@Deprecated
	public JsonElement getElement(@NonNull String name) {
		return object.get(name);
	}

	@Override
	public int getRequiredInteger(
			@NonNull String name) throws WrongTypeException, MissingValueException
	{
		Integer value= getOptionalInteger(name);
		if (value == null) {
			throw new MissingValueException(prefix + name);
		}

		return value;
	}
	
	@Override
	public long getRequiredLong(
			@NonNull String name) throws WrongTypeException, MissingValueException
	{
		Long value= getOptionalLong(name);
		if (value == null) {
			throw new MissingValueException(prefix + name);
		}

		return value;
	}
	
	@Override
	public double getRequiredDouble(
			@NonNull String name) throws WrongTypeException, MissingValueException
	{
		Double value= getOptionalDouble(name);
		if (value == null) {
			throw new MissingValueException(prefix + name);
		}

		return value;
	}

	@Override
	public @NonNull String getRequiredString(
			@NonNull String name) throws WrongTypeException, MissingValueException
	{
		String value= getOptionalString(name);
		if (value == null) {
			throw new MissingValueException(prefix + name);
		}

		return value;
	}

	@Override
	public boolean getRequiredBoolean(
			@NonNull String name) throws WrongTypeException, MissingValueException
	{
		Boolean value= getOptionalBoolean(name);
		if (value == null) {
			throw new MissingValueException(prefix + name);
		}

		return value;
	}

	@Override
	public @NonNull LocalDate getRequiredLocalDate(
			@NonNull String name) throws WrongTypeException, MissingValueException, FormatException
	{
		LocalDate value= getOptionalLocalDate(name);
		if (value == null) {
			throw new MissingValueException(prefix + name);
		}

		return value;
	}

	@Override
	public @NonNull Timestamp getRequiredTimestamp(
			@NonNull String name) throws WrongTypeException, MissingValueException, FormatException
	{
		Timestamp value= getOptionalTimestamp(name);
		if (value == null) {
			throw new MissingValueException(prefix + name);
		}

		return value;
	}

	@Override
	public @NonNull Composite getRequiredObject(
			@NonNull String name) throws WrongTypeException, MissingValueException
	{
		Composite value= getOptionalObject(name);
		if (value == null) {
			throw new MissingValueException(prefix + name);
		}

		return value;
	}

	@Override
	public @NonNull Iterable<@NonNull Composite> getRequiredObjectArray(
			@NonNull String name) throws WrongTypeException, MissingValueException
	{
		Iterable<@NonNull Composite> value= getOptionalObjectArray(name);
		if (value == null) {
			throw new MissingValueException(prefix + name);
		}
		
		return value;
	}
	
	@Override
	public @NonNull Iterable<@NonNull String> getRequiredStringArray(
			@NonNull String name) throws WrongTypeException, MissingValueException
	{
		Iterable<@NonNull String> value= getOptionalStringArray(name);
		if (value == null) {
			throw new MissingValueException(prefix + name);
		}
		
		return value;
	}
	
	@Override
	public @NonNull Iterable<@NonNull Integer> getRequiredIntegerArray(
			@NonNull String name) throws WrongTypeException, MissingValueException
	{
		Iterable<@NonNull Integer> value= getOptionalIntegerArray(name);
		if (value == null) {
			throw new MissingValueException(prefix + name);
		}
		
		return value;
	}
	
	@Override
	public Integer getOptionalInteger(@NonNull String name) throws WrongTypeException {
		Integer rval= null;
		
		if (object.has(name)) {
			JsonElement el= object.get(name);
			if (!el.isJsonNull()) {
				if (!el.isJsonPrimitive()) {
					throw new WrongTypeException(prefix + name, "integer");
				}
				
				JsonPrimitive pr= el.getAsJsonPrimitive();
				if (pr.isNumber()) {
					int i= pr.getAsInt();
					double d= pr.getAsDouble();
					
					if (d != (double)i) {
						throw new WrongTypeException(prefix + name, "integer");
					}
					
					rval= i;
				} else if (pr.isString()) {
					try {
						rval= Integer.parseInt(pr.getAsString());
					} catch (NumberFormatException e) {
						throw new WrongTypeException(prefix + name, "integer");
					}
				} else {
					throw new WrongTypeException(prefix + name, "integer");
				}
			}
		}

		return rval;
	}
	
	@Override
	public Long getOptionalLong(@NonNull String name) throws WrongTypeException {
		Long rval= null;
		
		if (object.has(name)) {
			JsonElement el= object.get(name);
			if (!el.isJsonNull()) {
				if (!el.isJsonPrimitive()) {
					throw new WrongTypeException(prefix + name, "long");
				}
				JsonPrimitive pr= el.getAsJsonPrimitive();
				if (pr.isNumber()) {
					long l= pr.getAsLong();
					double d= pr.getAsDouble();
					
					if (d != (double)l) {
						throw new WrongTypeException(prefix + name, "long");
					}
					
					rval= l;
				} else if (pr.isString()) {
					try {
						rval= Long.parseLong(pr.getAsString());
					} catch (NumberFormatException e) {
						throw new WrongTypeException(prefix + name, "long");
					}
				} else {
					throw new WrongTypeException(prefix + name, "long");
				}
			}
		}

		return rval;
	}
	
	@Override
	public Double getOptionalDouble(@NonNull String name) throws WrongTypeException {
		Double rval= null;
		
		if (object.has(name)) {
			JsonElement el= object.get(name);
			if (!el.isJsonNull()) {
				if (!el.isJsonPrimitive()) {
					throw new WrongTypeException(prefix + name, "double");
				}
				JsonPrimitive pr= el.getAsJsonPrimitive();
				if (pr.isNumber()) {
					rval= pr.getAsDouble();
				} else if (pr.isString()) {
					try {
						rval= Double.parseDouble(pr.getAsString());
					} catch (NumberFormatException e) {
						throw new WrongTypeException(prefix + name, "double");
					}
				} else {
					throw new WrongTypeException(prefix + name, "double"); 
				}
			}
		}
		
		return rval;
	}

	@Override
	public String getOptionalString(@NonNull String name) throws WrongTypeException {
		String rval= null;
		
		if (object.has(name)) {
			JsonElement el= object.get(name);
			if (!el.isJsonNull()) {
				if (!el.isJsonPrimitive()) {
					throw new WrongTypeException(prefix + name, "string");
				}
				JsonPrimitive pr= el.getAsJsonPrimitive();

				if (!pr.isString()) {
					throw new WrongTypeException(prefix + name, "string");
				}

				rval= pr.getAsString();
			}
		}

		return rval;
	}

	@Override
	public Boolean getOptionalBoolean(@NonNull String name) throws WrongTypeException {
		Boolean rval= null;
		
		if (object.has(name)) {
			JsonElement el= object.get(name);
			if (!el.isJsonNull()) {
				if (!el.isJsonPrimitive()) {
					throw new WrongTypeException(prefix + name, "boolean");
				}
				JsonPrimitive pr= el.getAsJsonPrimitive();
				if (!pr.isBoolean()) {
					throw new WrongTypeException(prefix + name, "boolean");
				}

				rval= pr.getAsBoolean();
			}
		}
		
		return rval;
	}
	
	@Override
	public boolean getOptionalBoolean(
			@NonNull String name,
			boolean defaultVal) throws WrongTypeException
	{
		Boolean value= getOptionalBoolean(name);
		if (value == null) {
			value= defaultVal;
		}
		
		return value;
	}

	@Override
	public LocalDate getOptionalLocalDate(
			@NonNull String name) throws WrongTypeException, FormatException
	{
		LocalDate rval= null;
		
		if (object.has(name)) {
			JsonElement el= object.get(name);
			if (!el.isJsonNull()) {
				if (!el.isJsonPrimitive()) {
					throw new WrongTypeException(prefix + name, "date");
				}
				JsonPrimitive pr= el.getAsJsonPrimitive();
				if (!pr.isString()) {
					throw new WrongTypeException(prefix + name, "date");
				}

				String value= pr.getAsString();
				
				try {
					rval= LocalDate.parse(value);
				} catch (NumberFormatException e) {
					throw new FormatException("Unable to parse " + name + " value '" +
							value + "' into LocalDate");
				}
			}
		}
		
		return rval;
	}

	@Override
	public Timestamp getOptionalTimestamp(
			@NonNull String name) throws WrongTypeException, FormatException
	{
		Timestamp rval= null;
		
		if (object.has(name)) {
			JsonElement el= object.get(name);
			if (!el.isJsonNull()) {
				if (!el.isJsonPrimitive()) {
					throw new WrongTypeException(prefix + name, "timestamp");
				}
				JsonPrimitive pr= el.getAsJsonPrimitive();
				if (!pr.isString()) {
					throw new WrongTypeException(prefix + name, "timestamp");
				}

				String value= pr.getAsString();
				
				try {
					int timePart= value.indexOf('T');
					if (timePart != -1) {
						OffsetDateTime odt= OffsetDateTime.parse(value);
						rval= Timestamp.from(odt.toInstant());
					} else {
						// If we get a string without a time part, use 00:00:00 in timezone passed on creation
						LocalDate lt= LocalDate.parse(value);
						rval= Timestamp.from(lt.atStartOfDay(timezone.toZoneId()).toInstant());
					}
				} catch (NumberFormatException e) {
					throw new FormatException("Unable to parse " + name + " value '" +
							value + "' into LocalDate");
				}
			}
		}
		
		return rval;
	}

	@Override
	public Composite getOptionalObject(@NonNull String name) throws WrongTypeException {
		Composite rval= null;
		
		if (object.has(name)) {
			JsonElement el= object.get(name);
			if (!el.isJsonNull()) {
				if (!el.isJsonObject()) {
					throw new WrongTypeException(name, "object");
				}
				
				@SuppressWarnings("null")
				@NonNull JsonObject ob= el.getAsJsonObject();
				
				rval= new JsonCompositeImpl(ob, timezone, prefix + name);
			}
		}
		
		return rval;
	}

	@Override
	public @NonNull Iterable<Map.Entry<@NonNull String, @NonNull Composite>> getObjectMap(
			) throws WrongTypeException
	{
		return new JsonCompositeMapImpl(object, timezone, prefix);
	}
	
	@Override
	public Iterable<@NonNull Composite> getOptionalObjectArray(
			@NonNull String name) throws WrongTypeException
	{
		JsonCompositeArrayImpl rval= null;
		
		JsonElement el= object.get(name);
		if (el != null) {
			if (!el.isJsonNull()) {
				if (!el.isJsonArray()) {
					throw new WrongTypeException(name, "Object[]");
				}
				
				@SuppressWarnings("null")
				@NonNull JsonArray array= el.getAsJsonArray();
				
				// The JsonCompositeArrayImpl construct validated each member is an object
				rval= new JsonCompositeArrayImpl(name, array, timezone, prefix);
			}
		}
		
		return rval;
	}
	
	@Override
	public Iterable<@NonNull String> getOptionalStringArray(
			@NonNull String name) throws WrongTypeException
	{
		List<@NonNull String> rval= null;
		
		JsonElement el= object.get(name);
		if (el != null) {
			if (!el.isJsonNull()) {
				rval= new ArrayList<@NonNull String>();
				
				if (!el.isJsonArray()) {
					throw new WrongTypeException(name, "String[]");
				}
				
				JsonArray array= el.getAsJsonArray();
				
				// Flatten to string list
				int index= 0;
				for (JsonElement itemEl : array) {
					if (!itemEl.isJsonPrimitive()) {
						throw new WrongTypeException(name + "[" + index + "]", "String");
					}
					JsonPrimitive itemPr= itemEl.getAsJsonPrimitive();
					if (!itemPr.isString()) {
						throw new WrongTypeException(name + "[" + index + "]", "String");
					}
					
					@SuppressWarnings("null")
					@NonNull String value= itemPr.getAsString();
					
					rval.add(value);
					index++;
				}
			}
		}
		
		return rval;
	}
	
	@Override
	public Iterable<@NonNull Integer> getOptionalIntegerArray(
			@NonNull String name) throws WrongTypeException
	{
		List<@NonNull Integer> rval= null;
		
		JsonElement el= object.get(name);
		if (el != null) {
			if (!el.isJsonNull()) {
				rval= new ArrayList<@NonNull Integer>();
				
				if (!el.isJsonArray()) {
					throw new WrongTypeException(name, "Integer[]");
				}

				JsonArray array= el.getAsJsonArray();
					
				// Flatten to string list
				int index= 0;
				for (JsonElement itemEl : array) {
					if (!itemEl.isJsonPrimitive()) {
						throw new WrongTypeException(name + "[" + index + "]", "Integer");
					}
					JsonPrimitive itemPr= itemEl.getAsJsonPrimitive();
					if (!itemPr.isNumber()) {
						throw new WrongTypeException(name + "[" + index + "]", "Integer");
					}

					rval.add(itemPr.getAsInt());
					index++;
				}
			}
		}
		
		return rval;
	}

	public String toString() { 
		return object.toString();
	}
	
	public @NonNull <Representation extends Object> Representation serialize(
			@NonNull Class<? extends Representation> representationClass)
	{
		if (!representationClass.isAssignableFrom(JsonObject.class)) {
			throw new UnsupportedSerializationException(representationClass);
		}
		
		@SuppressWarnings("unchecked")
		Representation rval= (Representation)object;
		
		return rval;
	}
}