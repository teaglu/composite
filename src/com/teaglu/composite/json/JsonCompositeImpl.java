package com.teaglu.composite.json;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNull;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.teaglu.composite.Composite;
import com.teaglu.composite.exception.FormatException;
import com.teaglu.composite.exception.MissingValueException;
import com.teaglu.composite.exception.UnsupportedSerializationException;
import com.teaglu.composite.exception.WrongTypeException;

public final class JsonCompositeImpl implements Composite {
	private @NonNull ZoneId zoneId;
	private @NonNull JsonObject object;

	JsonCompositeImpl(@NonNull JsonObject object, @NonNull ZoneId zoneId) {
		this.object= object;
		this.zoneId= zoneId;
	}

	@Override
	public int getRequiredInteger(@NonNull String name) throws WrongTypeException, MissingValueException {
		int rval= 0;
		
		if (!object.has(name)) {
			throw new MissingValueException(name);
		}
		
		JsonElement el= object.get(name);

		if (!el.isJsonPrimitive()) {
			throw new WrongTypeException(name, "Integer");
		}
		
		JsonPrimitive pr= el.getAsJsonPrimitive();
		if (pr.isNumber()) {
			rval= pr.getAsInt();
		} else if (pr.isString()) {
			try {
				rval= Integer.parseInt(pr.getAsString());
			} catch (NumberFormatException e) {
				throw new WrongTypeException(name, "Integer");
			}
		} else {
			throw new WrongTypeException(name, "Integer");
		}

		return rval;
	}
	
	@Override
	public double getRequiredDouble(@NonNull String name) throws WrongTypeException, MissingValueException {
		double rval= 0;
		
		if (!object.has(name)) {
			throw new MissingValueException(name);
		}
		
		JsonElement el= object.get(name);

		if (!el.isJsonPrimitive()) {
			throw new WrongTypeException(name, "Double");
		}
		
		JsonPrimitive pr= el.getAsJsonPrimitive();
		if (pr.isNumber()) {
			rval= pr.getAsDouble();
		} else if (pr.isString()) {
			try {
				rval= Double.parseDouble(pr.getAsString());
			} catch (NumberFormatException e) {
				throw new WrongTypeException(name, "Double");
			}
		} else {
			throw new WrongTypeException(name, "Double");
		}
		
		return rval;
	}

	@Override
	public @NonNull String getRequiredString(@NonNull String name) throws WrongTypeException, MissingValueException {
		if (!object.has(name)) {
			throw new MissingValueException(name);
		}
		
		JsonElement el= object.get(name);

		if (!el.isJsonPrimitive()) {
			throw new WrongTypeException(name, "String");
		}
		
		JsonPrimitive pr= el.getAsJsonPrimitive();
		if (!pr.isString()) {
			throw new WrongTypeException(name, "String");
		}
		
		
		@SuppressWarnings("null") @NonNull String rval= pr.getAsString();
		return rval;
	}

	@Override
	public boolean getRequiredBoolean(@NonNull String name) throws WrongTypeException, MissingValueException {
		if (!object.has(name)) {
			throw new MissingValueException(name);
		}
		
		JsonElement el= object.get(name);

		if (!el.isJsonPrimitive()) {
			throw new WrongTypeException(name, "Boolean");
		}
		
		JsonPrimitive pr= el.getAsJsonPrimitive();
		if (!pr.isBoolean()) {
			throw new WrongTypeException(name, "Boolean");
		}
		
		return pr.getAsBoolean();
	}

	@Override
	public @NonNull LocalDate getRequiredLocalDate(@NonNull String name) throws WrongTypeException, MissingValueException, FormatException {
		if (!object.has(name)) {
			throw new MissingValueException(name);
		}
		
		JsonElement el= object.get(name);
		if (!el.isJsonPrimitive()) {
			throw new WrongTypeException(name, "Date");
		}
		
		JsonPrimitive pr= el.getAsJsonPrimitive();
		if (!pr.isString()) {
			throw new WrongTypeException(name, "Date");
		}

		String value= pr.getAsString();

		try {
			LocalDate rval= LocalDate.parse(value);
			
			if (rval == null) {
				throw new RuntimeException("LocalDate parse returned null, which is disallowed by spec");
			}
			
			return rval;
		} catch (NumberFormatException e) {
			throw new FormatException("Unable to parse " + name + " value '" + value + "' to a LocalDate");
		}
	}

	@Override
	public @NonNull Timestamp getRequiredTimestamp(@NonNull String name) throws WrongTypeException, MissingValueException, FormatException {
		if (!object.has(name)) {
			throw new MissingValueException(name);
		}
		
		JsonElement el= object.get(name);
		if (!el.isJsonPrimitive()) {
			throw new WrongTypeException(name, "Date");
		}
		
		JsonPrimitive pr= el.getAsJsonPrimitive();
		if (!pr.isString()) {
			throw new WrongTypeException(name, "Date");
		}

		String value= pr.getAsString();

		Timestamp rval= null;
		try {
			int timePart= value.indexOf('T');
			if (timePart != -1) {
				OffsetDateTime odt= OffsetDateTime.parse(value);
				rval= Timestamp.from(odt.toInstant());
			} else {
				// If we get a string without a time part, use 00:00:00 in timezone passed on creation
				LocalDate lt= LocalDate.parse(value);
				rval= Timestamp.from(lt.atStartOfDay(zoneId).toInstant());
			}
			
			if (rval == null) {
				throw new RuntimeException("Timestamp parse returned null, which is disallowed by spec");
			}

			return rval;
		} catch (NumberFormatException e) {
			throw new FormatException("Unable to parse " + name + " value '" + value + "' into Timestamp");
		}
	}

	@Override
	public @NonNull Composite getRequiredObject(@NonNull String name) throws WrongTypeException, MissingValueException {
		if (!object.has(name)) {
			throw new MissingValueException(name);
		}
		
		JsonElement el= object.get(name);
		if (!el.isJsonObject()) {
			throw new WrongTypeException(name, "Object");
		}
		
		@SuppressWarnings("null") @NonNull JsonObject rval= el.getAsJsonObject();
		
		return new JsonCompositeImpl(rval, zoneId);
	}

	@Override
	public @NonNull Iterable<@NonNull Composite> getRequiredObjectArray(@NonNull String name) throws WrongTypeException, MissingValueException {
		if (!object.has(name)) {
			throw new MissingValueException(name);
		}
		
		JsonElement el= object.get(name);
		if (!el.isJsonArray()) {
			throw new WrongTypeException(name, "Object[]");
		}
			
		@SuppressWarnings("null")
		@NonNull JsonArray array= el.getAsJsonArray();
			
		// We can't throw a wrong type exception from the iterator, so walk the
		// array now and make sure everything is an object.
		for (JsonElement testEl : array) {
			if (!testEl.isJsonObject()) {
				throw new WrongTypeException(name, "Object[]");
			}
		}

		return new JsonCompositeArrayImpl(array, zoneId);
	}
	
	@Override
	public @NonNull Iterable<@NonNull String> getRequiredStringArray(@NonNull String name) throws WrongTypeException, MissingValueException {
		JsonElement el= object.get(name);
		if (el == null) {
			throw new MissingValueException(name);
		}
		if (!el.isJsonArray()) {
			throw new WrongTypeException(name, "String[]");
		}
		
		JsonArray array= el.getAsJsonArray();
		@NonNull List<@NonNull String> rval= new ArrayList<@NonNull String>();
		
		// Flatten to string list
		for (JsonElement itemEl : array) {
			if (!itemEl.isJsonPrimitive()) {
				throw new WrongTypeException(name, "String[]");
			}
			
			JsonPrimitive itemPr= itemEl.getAsJsonPrimitive();
			if (!itemPr.isString()) {
				throw new WrongTypeException(name, "String[]");
			}
			
			@SuppressWarnings("null") @NonNull String value= itemPr.getAsString();
			rval.add(value);
		}
		
		return rval;
	}
	
	@SuppressWarnings("null")
	@Override
	public @NonNull Iterable<@NonNull Integer> getRequiredIntegerArray(@NonNull String name) throws WrongTypeException, MissingValueException {
		List<Integer> rval= new ArrayList<Integer>();
		
		if (!object.has(name)) {
			throw new MissingValueException(name);
		}

		JsonElement el= object.get(name);
		
		if (el.isJsonPrimitive()) {
			JsonPrimitive pr= el.getAsJsonPrimitive();
			if (pr.isNumber()) {
				rval.add(pr.getAsInt());
			} else {
				throw new WrongTypeException(name, "Integer[]");
			}
		} else if (el.isJsonArray()) {
			JsonArray array= el.getAsJsonArray();
			
			// Flatten to string list
			for (JsonElement itemEl : array) {
				if (!itemEl.isJsonPrimitive()) {
					throw new WrongTypeException(name, "Integer[]");
				}
				JsonPrimitive itemPr= itemEl.getAsJsonPrimitive();
				if (!itemPr.isNumber()) {
					throw new WrongTypeException(name, "Integer[]");
				}
				rval.add(itemPr.getAsInt());
			}
		} else {
			throw new WrongTypeException(name, "Integer[]");
		}
		
		return rval;
	}
	
	@Override
	public Integer getOptionalInteger(@NonNull String name) throws WrongTypeException {
		Integer rval= null;
		
		if (object.has(name)) {
			JsonElement el= object.get(name);
			if (!el.isJsonNull()) {
				if (!el.isJsonPrimitive()) {
					throw new WrongTypeException(name, "Integer");
				}
				JsonPrimitive pr= el.getAsJsonPrimitive();
				if (pr.isNumber()) {
					rval= pr.getAsInt();
				} else if (pr.isString()) {
					try {
						rval= Integer.parseInt(pr.getAsString());
					} catch (NumberFormatException e) {
						throw new WrongTypeException(name, "Integer");
					}
				} else {
					throw new WrongTypeException(name, "String");
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
					throw new WrongTypeException(name, "Double");
				}
				JsonPrimitive pr= el.getAsJsonPrimitive();
				if (pr.isNumber()) {
					rval= pr.getAsDouble();
				} else if (pr.isString()) {
					try {
						rval= Double.parseDouble(pr.getAsString());
					} catch (NumberFormatException e) {
						throw new WrongTypeException(name, "Double");
					}
				} else {
					throw new WrongTypeException(name, "Double"); 
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
					throw new WrongTypeException(name, "String");
				}
				JsonPrimitive pr= el.getAsJsonPrimitive();

				if (!pr.isString()) {
					throw new WrongTypeException(name, "String");
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
					throw new WrongTypeException(name, "Boolean");
				}
				JsonPrimitive pr= el.getAsJsonPrimitive();
				if (!pr.isBoolean()) {
					throw new WrongTypeException(name, "Boolean");
				}

				rval= pr.getAsBoolean();
			}
		}
		
		return rval;
	}

	@Override
	public LocalDate getOptionalLocalDate(@NonNull String name) throws WrongTypeException, FormatException {
		LocalDate rval= null;
		
		if (object.has(name)) {
			JsonElement el= object.get(name);
			if (!el.isJsonNull()) {
				if (!el.isJsonPrimitive()) {
					throw new WrongTypeException(name, "Date");
				}
				JsonPrimitive pr= el.getAsJsonPrimitive();
				if (!pr.isString()) {
					throw new WrongTypeException(name, "Date");
				}

				String value= pr.getAsString();
				
				try {
					rval= LocalDate.parse(value);
				} catch (NumberFormatException e) {
					throw new FormatException("Unable to parse " + name + " value '" + value + "' into LocalDate");
				}
			}
		}
		
		return rval;
	}

	@Override
	public Timestamp getOptionalTimestamp(@NonNull String name) throws WrongTypeException, FormatException {
		Timestamp rval= null;
		
		if (object.has(name)) {
			JsonElement el= object.get(name);
			if (!el.isJsonNull()) {
				if (!el.isJsonPrimitive()) {
					throw new WrongTypeException(name, "Timestamp");
				}
				JsonPrimitive pr= el.getAsJsonPrimitive();
				if (!pr.isString()) {
					throw new WrongTypeException(name, "Timestamp");
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
					rval= Timestamp.from(lt.atStartOfDay(zoneId).toInstant());
				}
				} catch (NumberFormatException e) {
					throw new FormatException("Unable to parse " + name + " value '" + value + "' into LocalDate");
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
					throw new WrongTypeException(name, "Object");
				}
				
				@SuppressWarnings("null") @NonNull JsonObject ob= el.getAsJsonObject();
				
				rval= new JsonCompositeImpl(ob, zoneId);
			}
		}
		
		return rval;
	}

	@Override
	public @NonNull Iterable<Map.Entry<@NonNull String, @NonNull Composite>> getObjectMap() throws WrongTypeException {
		return new JsonCompositeMapImpl(object, zoneId);
	}
	
	@Override
	public Iterable<@NonNull Composite> getOptionalObjectArray(@NonNull String name) throws WrongTypeException {
		JsonCompositeArrayImpl rval= null;
		
		JsonElement el= object.get(name);
		if (el != null) {
			if (!el.isJsonNull()) {
				if (!el.isJsonArray()) {
					throw new WrongTypeException(name, "Object[]");
				}
				
				@SuppressWarnings("null") @NonNull JsonArray array= el.getAsJsonArray();
				
				// We can't throw a wrong type exception from the iterator, so walk the
				// array now and make sure everything is an object.
				for (JsonElement testEl : array) {
					if (!testEl.isJsonObject()) {
						throw new WrongTypeException(name, "Object[]");
					}
				}
				
				rval= new JsonCompositeArrayImpl(array, zoneId);
			}
		}
		
		return rval;
	}
	
	@Override
	public Iterable<@NonNull String> getOptionalStringArray(@NonNull String name) throws WrongTypeException {
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
				for (JsonElement itemEl : array) {
					if (!itemEl.isJsonPrimitive()) {
						throw new WrongTypeException(name, "String[]");
					}
					JsonPrimitive itemPr= itemEl.getAsJsonPrimitive();
					if (!itemPr.isString()) {
						throw new WrongTypeException(name, "String[]");
					}
					
					@SuppressWarnings("null") @NonNull String value= itemPr.getAsString();
					
					rval.add(value);
				}
			}
		}
		
		return rval;
	}
	
	@Override
	public Iterable<@NonNull Integer> getOptionalIntegerArray(@NonNull String name) throws WrongTypeException {
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
				for (JsonElement itemEl : array) {
					if (!itemEl.isJsonPrimitive()) {
						throw new WrongTypeException(name, "Integer[]");
					}
					JsonPrimitive itemPr= itemEl.getAsJsonPrimitive();
					if (!itemPr.isNumber()) {
						throw new WrongTypeException(name, "Integer[]");
					}

					rval.add(itemPr.getAsInt());
				}
			}
		}
		
		return rval;
	}

	public String toString() { 
		return object.toString();
	}
	
	public @NonNull <Representation extends Object> Representation serialize(Class<? extends Representation> representationClass) {
		if (!representationClass.isAssignableFrom(JsonObject.class)) {
			throw new UnsupportedSerializationException(representationClass);
		}
		
		@SuppressWarnings("unchecked")
		Representation rval= (Representation)object;
		
		return rval;
	}
}