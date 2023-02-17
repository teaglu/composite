package com.teaglu.composite.map;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TimeZone;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import com.teaglu.composite.Composite;
import com.teaglu.composite.exception.FormatException;
import com.teaglu.composite.exception.MissingValueException;
import com.teaglu.composite.exception.WrongTypeException;

public class MapCompositeImpl implements Composite {
	private @NonNull String prefix;
	private @NonNull Map<String, Object> members;
	private @NonNull TimeZone timezone;
	private @NonNull MapSerializer serializer;
	
	public MapCompositeImpl(
			@NonNull Map<String, Object> members,
			@NonNull TimeZone timezone,
			@NonNull MapSerializer serializer,
			@Nullable String path)
	{
		this.members= members;
		this.timezone= timezone;
		this.serializer= serializer;
		if (path == null) {
			this.prefix= "";
		} else {
			this.prefix= path + ".";
		}
	}
	
	@Override
	public int getRequiredInteger(
			@NonNull String name) throws MissingValueException, WrongTypeException
	{
		Integer value= getOptionalInteger(name);
		if (value == null) {
			throw new MissingValueException(prefix + name);
		}
		return value;
	}

	@Override
	public long getRequiredLong(
			@NonNull String name) throws MissingValueException, WrongTypeException
	{
		Long value= getOptionalLong(name);
		if (value == null) {
			throw new MissingValueException(prefix + name);
		}
		
		return value;
	}

	@Override
	public double getRequiredDouble(
			@NonNull String name) throws MissingValueException, WrongTypeException
	{
		Double value= getOptionalDouble(name);
		if (value == null) {
			throw new MissingValueException(prefix + name);
		}
		
		return value;
	}

	@Override
	public @NonNull String getRequiredString(
			@NonNull String name) throws MissingValueException, WrongTypeException
	{
		String value= getOptionalString(name);
		if (value == null) {
			throw new MissingValueException(prefix + name);
		}
		
		return value;
	}

	@Override
	public boolean getRequiredBoolean(
			@NonNull String name) throws MissingValueException, WrongTypeException
	{
		Boolean value= getOptionalBoolean(name);
		if (value == null) {
			throw new MissingValueException(prefix + name);
		}
		
		return value;
	}

	@Override
	public @NonNull LocalDate getRequiredLocalDate(
			@NonNull String name) throws MissingValueException, WrongTypeException, FormatException
	{
		LocalDate value= getOptionalLocalDate(name);
		if (value == null) {
			throw new MissingValueException(prefix + name);
		}
		
		return value;
	}

	@Override
	public @NonNull Timestamp getRequiredTimestamp(
			@NonNull String name) throws MissingValueException, WrongTypeException, FormatException
	{
		Timestamp value= getOptionalTimestamp(name);
		if (value == null) {
			throw new MissingValueException(prefix + name);
		}
		
		return value;
	}

	@Override
	public @NonNull Composite getRequiredObject(
			@NonNull String name) throws MissingValueException, WrongTypeException
	{
		Composite value= getOptionalObject(name);
		if (value == null) {
			throw new MissingValueException(prefix + name);
		}
		
		return value;
	}

	@Override
	public @NonNull Iterable<@NonNull Composite> getRequiredObjectArray(
			@NonNull String name) throws MissingValueException, WrongTypeException
	{
		Iterable<@NonNull Composite> value= getOptionalObjectArray(name);
		if (value == null) {
			throw new MissingValueException(prefix + name);
		}
		
		return value;
	}

	@Override
	public @NonNull Iterable<@NonNull String> getRequiredStringArray(
			@NonNull String name) throws MissingValueException, WrongTypeException
	{
		Iterable<@NonNull String> value= getOptionalStringArray(name);
		if (value == null) {
			throw new MissingValueException(prefix + name);
		}
		
		return value;
	}

	@Override
	public @NonNull Iterable<@NonNull Integer> getRequiredIntegerArray(
			@NonNull String name) throws MissingValueException, WrongTypeException
	{
		Iterable<@NonNull Integer> value= getOptionalIntegerArray(name);
		if (value == null) {
			throw new MissingValueException(prefix + name);
		}
		
		return value;
	}

	@Override
	public Integer getOptionalInteger(
			@NonNull String name) throws WrongTypeException
	{
		Object value= members.get(name);
		if (value == null) {
			return null;
		} else {
			if (value instanceof Integer) {
				return (Integer)value;
			} else if (value instanceof Long) {
				return (int)(long)(Long)value;
			} else if (value instanceof Float) {
				double d= (double)(Float)value;
				int i= (int)Math.floor(d);
				if (d != (double)i) {
					throw new WrongTypeException(prefix + name, "integer");
				}
				
				return i;
			} else if (value instanceof Double) {
				double d= (double)(Double)value;
				int i= (int)Math.floor(d);
				if (d != (double)i) {
					throw new WrongTypeException(prefix + name, "integer");
				}
				
				return i;
			} else {
				throw new WrongTypeException(prefix + name, "number");
			}
		}
	}

	@Override
	public Long getOptionalLong(
			@NonNull String name) throws WrongTypeException
	{
		Object value= members.get(name);
		if (value == null) {
			return null;
		} else {		
			if (value instanceof Integer) {
				return (long)(Integer)value;
			} else if (value instanceof Long) {
				return (long)(Long)value;
			} else if (value instanceof Float) {
				double d= (double)(Float)value;
				long i= (int)Math.floor(d);
				if (d != (double)i) {
					throw new WrongTypeException(prefix + name, "integer");
				}
				
				return i;
			} else if (value instanceof Double) {
				double d= (double)(Double)value;
				long i= (int)Math.floor(d);
				if (d != (double)i) {
					throw new WrongTypeException(prefix + name, "integer");
				}
				
				return i;
			} else {
				throw new WrongTypeException(prefix, "number");
			}
		}
	}

	@Override
	public Double getOptionalDouble(
			@NonNull String name) throws WrongTypeException
	{
		Object value= members.get(name);
		if (value == null) {
			return null;
		} else {
			if (value instanceof Integer) {
				return Double.valueOf((double)(int)(Integer)value);
			} else if (value instanceof Long) {
				return Double.valueOf((double)(long)(Long)value);
			} else if (value instanceof Float) {
				return Double.valueOf((float)(Float)value);
			} else if (value instanceof Double) {
				return (Double)value;
			} else {
				throw new WrongTypeException(prefix + name, "number");
			}
		}
	}

	@Override
	public String getOptionalString(@NonNull String name) throws WrongTypeException {
		Object value= members.get(name);
		if (value == null) {
			return null;
		} else {
			if (value instanceof String) {
				return (String)value;
			} else {
				throw new WrongTypeException(prefix + name, "string");
			}
		}
	}

	@Override
	public Boolean getOptionalBoolean(@NonNull String name) throws WrongTypeException {
		Object value= members.get(name);
		if (value == null) {
			return null;
		} else {
			if (value instanceof Boolean) {
				return (Boolean)value;
			} else if (value instanceof String) {
				switch ((String)value) {
				case "0":
				case "false":
				case "no":
					return false;
					
				case "1":
				case "true":
				case "yes":
					return true;
					
				default:
					throw new WrongTypeException(prefix + name, "boolean");
				}
			} else {
				throw new WrongTypeException(prefix + name, "string");
			}
		}
	}

	@Override
	public boolean getOptionalBoolean(
			@NonNull String name,
			boolean defaultVal) throws WrongTypeException
	{
		Boolean value= getOptionalBoolean(name);
		if (value == null) {
			return defaultVal;
		} else {
			return value;
		}
	}

	@Override
	public LocalDate getOptionalLocalDate(
			@NonNull String name) throws WrongTypeException, FormatException
	{
		Object value= members.get(name);
		if (value == null) {
			return null;
		} else {
			if (value instanceof String) {
				try {
					LocalDate rval= LocalDate.parse((String)value);
					
					if (rval == null) {
						throw new RuntimeException(
								"LocalDate parse returned null, which is disallowed by spec");
					}
					
					return rval;
				} catch (NumberFormatException e) {
					throw new FormatException("Unable to parse " +
							prefix + name + " value '" + value + "' to a LocalDate");
				}
			} else {
				throw new WrongTypeException(prefix + name, "LocalDate");
			}
		}
	}

	@Override
	public Timestamp getOptionalTimestamp(
			@NonNull String name) throws WrongTypeException, FormatException
	{
		Object value= members.get(name);
		if (value == null) {
			return null;
		} else {
			if (!(value instanceof String)) {
				throw new WrongTypeException(prefix + name, "Timestamp");
			}
			
			String string= (String)value;
			
			try {
				int timePart= string.indexOf('T');
				if (timePart != -1) {
					OffsetDateTime odt= OffsetDateTime.parse(string);
					return Timestamp.from(odt.toInstant());
				} else {
					// If we get a string without a time part, use 00:00:00 in timezone passed on creation
					LocalDate lt= LocalDate.parse(string);
					return Timestamp.from(lt.atStartOfDay(timezone.toZoneId()).toInstant());
				}
			} catch (NumberFormatException e) {
				throw new FormatException("Unable to parse " +
						prefix + name + " value '" + value + "' to a Timestamp");
			}
		}
	}

	@Override
	public Composite getOptionalObject(
			@NonNull String name) throws WrongTypeException
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterable<@NonNull Composite> getOptionalObjectArray(
			@NonNull String name) throws WrongTypeException
	{
		Object value= members.get(name);
		if (value == null) {
			return null;
		} else {
			if (!(value instanceof Iterable<?>)) {
				throw new WrongTypeException(prefix + name, "iterable");
			}
			
			Iterable<?> iterable= (Iterable<?>)value;
			
			return new MapCompositeArrayImpl(prefix + name, iterable, timezone, serializer);
		}
	}

	@Override
	public Iterable<@NonNull String> getOptionalStringArray(
			@NonNull String name) throws WrongTypeException
	{
		Object value= members.get(name);
		if (value == null) {
			return null;
		} else {
			if (!(value instanceof Iterable<?>)) {
				throw new WrongTypeException(prefix + name, "iterable");
			}
			
			List<@NonNull String> rval= new ArrayList<>();
			
			int index= 0;
			for (Object object : (Iterable<?>)value) {
				if (object == null) {
					throw new WrongTypeException(prefix + name + "[" + index + "]", "string");
				} else if (object instanceof String) {
					rval.add((String)object);
				} else {
					throw new WrongTypeException(prefix + name + "[" + index + "]", "string");
				}
				index++;
			}
			
			return rval;
		}
	}

	@Override
	public Iterable<@NonNull Integer> getOptionalIntegerArray(
			@NonNull String name) throws WrongTypeException
	{
		Object value= members.get(name);
		if (value == null) {
			return null;
		} else {
			if (!(value instanceof Iterable<?>)) {
				throw new WrongTypeException(prefix + name, "iterable");
			}
			
			List<@NonNull Integer> rval= new ArrayList<>();
			
			int index= 0;
			for (Object object : (Iterable<?>)value) {
				if (object == null) {
					throw new WrongTypeException(prefix + name + "[" + index + "]", "integer");
				} else if (object instanceof Integer) {
					rval.add((Integer)object);
				} else {
					throw new WrongTypeException(prefix + name + "[" + index + "]", "integer");
				}
				index++;
			}
			
			return rval;
		}
	}

	@Override
	public @NonNull Iterable<Entry<@NonNull String, @NonNull Composite>> getObjectMap(
			) throws WrongTypeException
	{
		// TODO Auto-generated method stub
		return new MapCompositeMapImpl(members, timezone, serializer, prefix);
	}

	@Override
	public <Representation> @NonNull Representation serialize(
			@NonNull Class<? extends Representation> representationClass)
	{
		return serializer.serialize(members, representationClass);
	}
}
