package com.teaglu.composite;

import static org.junit.jupiter.api.Assertions.fail;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.TreeMap;

import org.eclipse.jdt.annotation.NonNull;
import org.junit.jupiter.api.Test;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.teaglu.composite.exception.SchemaException;
import com.teaglu.composite.json.JsonCompositeImpl;
import com.teaglu.composite.map.MapCompositeImpl;
import com.teaglu.composite.map.MapSerializer;

public class AccessTests {
	private static class DummySerializer implements MapSerializer {
		@Override
		public <Representation> @NonNull Representation serialize(
				@NonNull Map<String, Object> tree,
				@NonNull Class<? extends Representation> representationClass)
		{
			throw new RuntimeException("Serializer not implemented");
		}
	}
	
	public @NonNull Composite createMap() {
		Map<String, Object> test= new TreeMap<>();
		
		test.put("intProperty", 3);
		test.put("longProperty", 3L);
		test.put("stringProperty", "stuff");
		test.put("doubleProperty1", 3.0F);
		test.put("doubleProperty2", 3.4F);
		test.put("localDateProperty", "2023-01-01");
		test.put("timestampProperty", "2023-01-01T12:00:00Z");
		
		Map<String, Object> object= new TreeMap<>();
		object.put("intProperty", 4);
		
		test.put("objectProperty", object);
		
		List<Integer> intList= new ArrayList<>();
		intList.add(3);
		test.put("intListProperty", intList);
		
		List<String> stringList= new ArrayList<>();
		stringList.add("stuff");
		test.put("stringListProperty", stringList);
		
		List<Map<String, Object>> objectList= new ArrayList<>();
		objectList.add(new TreeMap<String, Object>());
		test.put("objectListProperty", objectList);
		
		TimeZone timezone= TimeZone.getTimeZone("America/New_York");
		if (timezone == null) {
			throw new RuntimeException("Failed to load timezone");
		}
		return new MapCompositeImpl(test, timezone, new DummySerializer(), null);
	}

	public @NonNull Composite createJson() {
		JsonObject test= new JsonObject();
		
		test.addProperty("intProperty", 3);
		test.addProperty("longProperty", 3L);
		test.addProperty("stringProperty", "stuff");
		test.addProperty("doubleProperty1", 3.0F);
		test.addProperty("doubleProperty2", 3.4F);
		test.addProperty("localDateProperty", "2023-01-01");
		test.addProperty("timestampProperty", "2023-01-01T12:00:00Z");
		
		JsonObject object= new JsonObject();
		object.addProperty("intProperty", 4);
		
		test.add("objectProperty", object);
		
		JsonArray intList= new JsonArray();
		intList.add(3);
		test.add("intListProperty", intList);
		
		JsonArray stringList= new JsonArray();
		stringList.add("stuff");
		test.add("stringListProperty", stringList);
		
		JsonArray objectList= new JsonArray();
		objectList.add(new JsonObject());
		test.add("objectListProperty", objectList);
		
		TimeZone timezone= TimeZone.getTimeZone("America/New_York");
		if (timezone == null) {
			throw new RuntimeException("Failed to load timezone");
		}
		ZoneId zoneId= timezone.toZoneId();
		if (zoneId == null) {
			throw new RuntimeException("Failed to load zone ID");
		}
		return new JsonCompositeImpl(test, zoneId);
	}

	private void testInteger(@NonNull Composite c) {
		try {
			if (c.getRequiredInteger("intProperty") != 3) {
				fail("Failed to retrieve value 3");
			}
		} catch (SchemaException e) {
			fail("Failed to retrieve int as int");
		}
		
		try {
			if (c.getRequiredLong("intProperty") != 3L) {
				fail("Failed to retrieve value 3");
			}
		} catch (SchemaException e) {
			fail("Failed to retrieve int as long");
		}
		
		try {
			if (c.getRequiredDouble("intProperty") != 3.0) {
				fail("Failed to retrieve value 3");
			}
		} catch (SchemaException e) {
			fail("Failed to retrieve int as double");
		}
		
		try {
			Integer value= c.getOptionalInteger("intProperty");
			if (value == null) {
				fail("Failed to find optional integer");
			} else if ((int)value != 3) {
				fail("Failed to retrieve value 3");
			}
		} catch (SchemaException e) {
			fail("Failed to retrieve int as int");
		}
		
		try {
			Long value= c.getOptionalLong("intProperty");
			if (value == null) {
				fail("Failed to find optional integer");
			} else if ((long)value != 3L) {
				fail("Failed to retrieve value 3");
			}
		} catch (SchemaException e) {
			fail("Failed to retrieve int as long");
		}
		
		try {
			Double value= c.getOptionalDouble("intProperty");
			if (value == null) {
				fail("Failed to find optional integer");
			} else if ((double)value != 3.0) {
				fail("Failed to retrieve value 3");
			}
		} catch (SchemaException e) {
			fail("Failed to retrieve int as long");
		}
		
		try {
			c.getRequiredInteger("noexist");
			fail("Failed to throw exception on non-existant item");
		} catch (SchemaException e) {
		}
		
		try {
			Integer value= c.getOptionalInteger("noexist");
			if (value != null) {
				fail("Failed to throw exception on non-existant item");
			}
		} catch (SchemaException e) {
			fail("Exception retrieving non-existant optional item");
		}
	}
	
	private void testLong(@NonNull Composite c) {
		try {
			if (c.getRequiredInteger("longProperty") != 3) {
				fail("Failed to retrieve value 3");
			}
		} catch (SchemaException e) {
			fail("Failed to retrieve int as int");
		}
		
		try {
			if (c.getRequiredLong("longProperty") != 3L) {
				fail("Failed to retrieve value 3");
			}
		} catch (SchemaException e) {
			fail("Failed to retrieve int as long");
		}
		
		try {
			if (c.getRequiredDouble("longProperty") != 3.0) {
				fail("Failed to retrieve value 3");
			}
		} catch (SchemaException e) {
			fail("Failed to retrieve int as double");
		}
		
		try {
			Integer value= c.getOptionalInteger("longProperty");
			if (value == null) {
				fail("Failed to find optional integer");
			} else if ((int)value != 3) {
				fail("Failed to retrieve value 3");
			}
		} catch (SchemaException e) {
			fail("Failed to retrieve int as int");
		}
		
		try {
			Long value= c.getOptionalLong("longProperty");
			if (value == null) {
				fail("Failed to find optional integer");
			} else if ((long)value != 3L) {
				fail("Failed to retrieve value 3");
			}
		} catch (SchemaException e) {
			fail("Failed to retrieve int as long");
		}
		
		try {
			Double value= c.getOptionalDouble("longProperty");
			if (value == null) {
				fail("Failed to find optional integer");
			} else if ((double)value != 3.0) {
				fail("Failed to retrieve value 3");
			}
		} catch (SchemaException e) {
			fail("Failed to retrieve int as long");
		}
		
		try {
			c.getRequiredLong("noexist");
			fail("Failed to throw exception on non-existant item");
		} catch (SchemaException e) {
		}
		
		try {
			Long value= c.getOptionalLong("noexist");
			if (value != null) {
				fail("Failed to throw exception on non-existant item");
			}
		} catch (SchemaException e) {
			fail("Exception retrieving non-existant optional item");
		}
	}
	

	
	private void testDouble(@NonNull Composite c) {
		try {
			if (c.getRequiredInteger("doubleProperty1") != 3) {
				fail("Failed to retrieve value 3");
			}
		} catch (SchemaException e) {
			fail("Failed to retrieve int as int", e);
		}
		
		try {
			c.getRequiredInteger("doubleProperty2");
			fail("Failed to catch incorrect rounding to integer");
		} catch (SchemaException e) {
		}
		
		try {
			if (c.getRequiredLong("doubleProperty1") != 3L) {
				fail("Failed to retrieve value 3");
			}
		} catch (SchemaException e) {
			fail("Failed to retrieve int as long");
		}
		
		try {
			c.getRequiredLong("doubleProperty2");
			fail("Failed to catch incorrect rounding to long");
		} catch (SchemaException e) {
		}
		
		try {
			double d= c.getRequiredDouble("doubleProperty2");
			if (Math.abs(d - 3.4) > 0.0001) {
				fail("Failed to retrieve value 3.4 - got " + d);
			}
		} catch (SchemaException e) {
			fail("Failed to retrieve int as double");
		}
		
		try {
			Integer value= c.getOptionalInteger("doubleProperty1");
			if (value == null) {
				fail("Failed to find optional integer");
			} else if ((int)value != 3) {
				fail("Failed to retrieve value 3");
			}
		} catch (SchemaException e) {
			fail("Failed to retrieve int as int");
		}
		
		try {
			Long value= c.getOptionalLong("doubleProperty1");
			if (value == null) {
				fail("Failed to find optional integer");
			} else if ((long)value != 3L) {
				fail("Failed to retrieve value 3");
			}
		} catch (SchemaException e) {
			fail("Failed to retrieve int as long");
		}
		
		try {
			Double value= c.getOptionalDouble("doubleProperty1");
			if (value == null) {
				fail("Failed to find optional double");
			} else if ((double)value != 3.0) {
				fail("Failed to retrieve value 3");
			}
		} catch (SchemaException e) {
			fail("Failed to retrieve int as long");
		}
		
		try {
			c.getRequiredDouble("noexist");
			fail("Failed to throw exception on non-existant item");
		} catch (SchemaException e) {
		}
		
		try {
			Double value= c.getOptionalDouble("noexist");
			if (value != null) {
				fail("Failed to throw exception on non-existant item");
			}
		} catch (SchemaException e) {
			fail("Exception retrieving non-existant optional item");
		}
	}
	
	private void testString(@NonNull Composite c) {
		try {
			if (!c.getRequiredString("stringProperty").equals("stuff")) {
				fail("Failed to retrieve value stuff");
			}
		} catch (SchemaException e) {
			fail("Failed to retrieve required string");
		}
			
		try {
			String value= c.getOptionalString("stringProperty");
			if (value == null) {
				fail("Failed to find optional integer");
			} else if (!value.equals("stuff")) {
				fail("Failed to retrieve value stuff");
			}
		} catch (SchemaException e) {
			fail("Failed to retrieve optional string");
		}
		
		try {
			c.getRequiredString("noexist");
			fail("Failed to throw exception on non-existant string");
		} catch (SchemaException e) {
		}
		
		try {
			String value= c.getOptionalString("noexist");
			if (value != null) {
				fail("Failed to throw exception on non-existant string");
			}
		} catch (SchemaException e) {
			fail("Exception retrieving non-existant optional string");
		}
	}

	@Test
	public void testMap() {
		Composite c= createMap();
		testInteger(c);
		testLong(c);
		testDouble(c);
		testString(c);
	}
	
	@Test
	public void testJson() {
		Composite c= createJson();
		testInteger(c);
		testLong(c);
		testDouble(c);
		testString(c);
	}
}
