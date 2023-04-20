package com.teaglu.composite.yaml;

import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.Map;
import java.util.TimeZone;

import org.eclipse.jdt.annotation.NonNull;
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
	 * Parse
	 * 
	 * Parse a text string as YAML and return a composite
	 *
	 * @param text						Text
	 * @param timezone					Timezone for interpretation
	 * 
	 * @return							New Composite
	 * 
	 * @throws WrongTypeException		Text is not a YAML object
	 * @throws ParseException 			Unable to parse YAML
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
	 * Parse
	 * 
	 * Parse a text string to YAML and return a composite, using the default timezone
	 *
	 * @param text						Text
	 * 
	 * @return							New Composite
	 * 
	 * @throws WrongTypeException		Text is not a YAML object
	 * @throws ParseException 			Unable to parse YAML
	 */
	public static @NonNull Composite Parse(
			@NonNull String text) throws WrongTypeException, ParseException
	{
		return Parse(text, defaultTimezone);
	}
	
	/**
	 * Parse
	 * 
	 * Parse an input stream as YAML and return a composite
	 *
	 * @param reader					Input stream reader
	 * @param timezone					Timezone for interpretation
	 * 
	 * @return							New Composite
	 * 
	 * @throws WrongTypeException		Input is not a YAML object
	 * @throws ParseException 			Unable to parse YAML
	 */
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
	
	/**
	 * Parse
	 * 
	 * Parse an input stream as YAML and return a composite, using the default timezone
	 *
	 * @param reader					Input stream reader
	 * 
	 * @return							New Composite
	 * 
	 * @throws WrongTypeException		Input is not a YAML object
	 * @throws ParseException 			Unable to parse YAML
	 */
	public static @NonNull Composite Parse(
			@NonNull InputStreamReader reader) throws WrongTypeException, ParseException
	{
		return Parse(reader, defaultTimezone);
	}
}
