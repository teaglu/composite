package com.teaglu.composite.yaml;

import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.Map;
import java.util.TimeZone;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.postgresql.util.PGobject;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.SafeConstructor;

import com.teaglu.composite.Composite;
import com.teaglu.composite.exception.ParseException;
import com.teaglu.composite.exception.UnsupportedSerializationException;
import com.teaglu.composite.exception.WrongTypeException;
import com.teaglu.composite.map.MapCompositeImpl;
import com.teaglu.composite.map.MapSerializer;

public class YamlComposite {
	private static final @NonNull TimeZone defaultTimezone;
	
	static {
		TimeZone utc= TimeZone.getTimeZone("UTC");
		if (utc == null) {
			throw new RuntimeException("Unable to resolve UTC for default timezone");
		}
		
		defaultTimezone= utc;
	}
	
	private static class Serializer implements MapSerializer {
		@Override
		public <Representation> @NonNull Representation serialize(
				@NonNull Map<String, Object> tree,
				@NonNull Class<? extends Representation> representationClass)
		{
			if (representationClass.isAssignableFrom(String.class)) {
				Yaml yaml= new Yaml(new SafeConstructor());
				StringWriter writer= new StringWriter();
				
				yaml.dump(tree, writer);
				
				String value= writer.toString();
				if (value == null) {
					throw new RuntimeException("StringWriter returned null");
				}
				
				@SuppressWarnings("unchecked")
				Representation rval= (Representation)value;
				
				return rval;
			} else {
				throw new UnsupportedSerializationException(representationClass);
			}
		}
	}
	
	private static class SerializerHolder {
		private static final @NonNull Serializer serializer= new Serializer();
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
	 * @throws YamlParseException 
	 */
	public static @NonNull Composite Parse(
			@NonNull String text,
			@NonNull TimeZone timezone) throws WrongTypeException, ParseException
	{
		Yaml yaml= new Yaml(new SafeConstructor());
		Map<String, Object> tree= null;
		try {
			tree= yaml.load(text);
		} catch (Exception parseException) {
			throw new ParseException("Error parsing YAML text", parseException);
		}
		
		if (tree == null) {
			throw new ParseException("YAML parsing returned null");
		}
		
		return new MapCompositeImpl(tree, timezone, SerializerHolder.serializer, null);
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
	 * @throws YamlParseException 
	 */
	public static @NonNull Composite Parse(
			@NonNull String text) throws WrongTypeException, ParseException
	{
		return Parse(text, defaultTimezone);
	}
	
	public static @NonNull Composite Parse(
			@NonNull InputStreamReader reader,
			@NonNull TimeZone timezone) throws WrongTypeException, ParseException
	{
		Yaml yaml= new Yaml(new SafeConstructor());
		Map<String, Object> tree= null;
		try {
			tree= yaml.load(reader);
		} catch (Exception parseException) {
			throw new ParseException("Error parsing YAML text", parseException);
		}
		
		if (tree == null) {
			throw new ParseException("YAML parsing returned null");
		}
		
		return new MapCompositeImpl(tree, timezone, SerializerHolder.serializer, null);
	}
	
	public static @NonNull Composite Parse(
			@NonNull InputStreamReader reader) throws WrongTypeException, ParseException
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
	 * @throws WrongTypeException
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
	 * @throws WrongTypeException
	 */
	public static @Nullable Composite ParseObject(
			@Nullable PGobject pgObject) throws ParseException, WrongTypeException
	{
		return ParseObject(pgObject, defaultTimezone);
	}
}
