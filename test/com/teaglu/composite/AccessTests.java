package com.teaglu.composite;

import static org.junit.jupiter.api.Assertions.fail;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.TreeMap;

import org.eclipse.jdt.annotation.NonNull;
import org.junit.jupiter.api.Test;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.teaglu.composite.exception.MissingValueException;
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
		
		List<Integer> intList= new ArrayList<>();
		intList.add(3);
		test.put("intListProperty", intList);
		
		List<String> stringList= new ArrayList<>();
		stringList.add("stuff");
		test.put("stringListProperty", stringList);
		
		List<Map<String, Object>> objectList= new ArrayList<>();
		objectList.add(new TreeMap<String, Object>());
		test.put("objectListProperty", objectList);
		
		Map<String, Object> mapObject= new TreeMap<>();
		mapObject.put("entry1", new TreeMap<String, Object>());
		mapObject.put("entry2", new TreeMap<String, Object>());
		
		test.put("objectProperty", mapObject);
		
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
		
		JsonArray intList= new JsonArray();
		intList.add(3);
		test.add("intListProperty", intList);
		
		JsonArray stringList= new JsonArray();
		stringList.add("stuff");
		test.add("stringListProperty", stringList);
		
		JsonArray objectList= new JsonArray();
		objectList.add(new JsonObject());
		test.add("objectListProperty", objectList);
		
		JsonObject mapObject= new JsonObject();
		mapObject.add("entry1", new JsonObject());
		mapObject.add("entry2", new JsonObject());
		test.add("objectProperty", mapObject);
		
		TimeZone timezone= TimeZone.getTimeZone("America/New_York");
		if (timezone == null) {
			throw new RuntimeException("Failed to load timezone");
		}

		return new JsonCompositeImpl(test, timezone, null);
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
	
	
	private void testMapIteration(@NonNull Composite map) throws SchemaException {
		int i= 0;
		boolean foundEntry1= false;
		boolean foundEntry2= false;
		
		for (Map.Entry<@NonNull String, @NonNull Composite> e : map.getObjectMap()) {
			@SuppressWarnings("null") String key= e.getKey();
			
			if (key.equals("entry1")) {
				if (foundEntry1) {
					fail("Found duplicate entry1");
				} else {
					foundEntry1= true;
				}
			} else if (key.equals("entry2")) {
				if (foundEntry2) {
					fail("Found duplicate entry2");
				} else {
					foundEntry2= true;
				}
			}
			
			i++;
		}
		
		if (i != 2) {
			fail("Wrong number of keys checking object iteration");
		}
		if (!foundEntry1) {
			fail("Entry 1 never found during object iteration");
		}
		if (!foundEntry2) {
			fail("Entry 2 never found during object iteration");
		}
	}
	
	private void testObject(@NonNull Composite c) {
		try {
			Composite map= c.getRequiredObject("objectProperty");
			testMapIteration(map);
		} catch (SchemaException e) {
			fail("Exception testing object iteration", e);
		}
		
		try {
			Composite map= c.getOptionalObject("objectProperty");
			if (map == null) {
				fail("Unable to find known object entry");
			} else {
				testMapIteration(map);
			}
		} catch (SchemaException e) {
			fail("Exception looking for optional object", e);
		}
		
		try {
			c.getRequiredObject("noexist");
			fail("Found non-existant map entry");
		} catch (MissingValueException e) {
		} catch (SchemaException e) {
			fail("Wrong exception looking for non-existant map key", e);
		}
		
		try {
			Composite map= c.getOptionalObject("noexist");
			if (map != null) {
				fail("Found non-existant optional map entry");
			}
		} catch (SchemaException e) {
			fail("Exception checking failure on non-existant object key", e);
		}
	}
	
	private void testObjectList(@NonNull Composite c) {
		try {
			Iterable<@NonNull Composite> list= c.getRequiredObjectArray("objectListProperty");
			int count= 0;
			for (@SuppressWarnings("unused") Composite s : list) {
				count++;
			}
			if (count != 1) {
				fail("Object list does not have length 1");
			}
		} catch (SchemaException e) {
			fail("Exception testing object list", e);
		}
		
		try {
			Iterable<@NonNull Composite> list= c.getOptionalObjectArray("objectListProperty");
			if (list == null) {
				fail("Failed to find object list");
			} else {
				int count= 0;
				for (@SuppressWarnings("unused") Composite s : list) {
					count++;
				}
				if (count != 1) {
					fail("Object list does not have length 1");
				}
			}
		} catch (SchemaException e) {
			fail("Exception testing object list", e);
		}
		
		try {
			c.getRequiredObjectArray("noexist");
			fail("Found object array property under non-existant name");
		} catch (MissingValueException e) {
		} catch (SchemaException e) {
			fail("Wrong exception for missing object list property");
		}
		
		try {
			Iterable<@NonNull Composite> list= c.getOptionalObjectArray("noexist");
			if (list != null) {
				fail("Found optional object list property under non-existant name");
			}
		} catch (SchemaException e) {
			fail("Exception testing optional object list");
		}
	}
	
	private void testIntList(@NonNull Composite c) {
		try {
			Iterable<@NonNull Integer> list= c.getRequiredIntegerArray("intListProperty");
			int count= 0;
			for (Integer i : list) {
				if (i != 3) {
					fail("Integer from list is not 3");
				}
				count++;
			}
			if (count != 1) {
				fail("Integer list does not have length 1");
			}
		} catch (SchemaException e) {
			fail("Exception testing integer list", e);
		}
		
		try {
			Iterable<@NonNull Integer> list= c.getOptionalIntegerArray("intListProperty");
			if (list == null) {
				fail("Failed to find integer list");
			} else {
				int count= 0;
				for (Integer i : list) {
					if (i != 3) {
						fail("Integer from list is not 3");
					}
					count++;
				}
				if (count != 1) {
					fail("Integer list does not have length 1");
				}
			}
		} catch (SchemaException e) {
			fail("Exception testing integer list", e);
		}
		
		try {
			c.getRequiredIntegerArray("noexist");
			fail("Found integer property under non-existant name");
		} catch (MissingValueException e) {
		} catch (SchemaException e) {
			fail("Wrong exception for missing list property");
		}
		
		try {
			Iterable<@NonNull Integer> list= c.getOptionalIntegerArray("noexist");
			if (list != null) {
				fail("Found optional integer property under non-existant name");
			}
		} catch (SchemaException e) {
			fail("Exception testing optional integer list");
		}
	}
	
	private void testStringList(@NonNull Composite c) {
		try {
			Iterable<@NonNull String> list= c.getRequiredStringArray("stringListProperty");
			int count= 0;
			for (String s : list) {
				if (!s.equals("stuff")) {
					fail("String from list is not stuff");
				}
				count++;
			}
			if (count != 1) {
				fail("String list does not have length 1");
			}
		} catch (SchemaException e) {
			fail("Exception testing string list", e);
		}
		
		try {
			Iterable<@NonNull String> list= c.getOptionalStringArray("stringListProperty");
			if (list == null) {
				fail("Failed to find integer list");
			} else {
				int count= 0;
				for (String s : list) {
					if (!s.equals("stuff")) {
						fail("String from list is not stuff");
					}
					count++;
				}
				if (count != 1) {
					fail("String list does not have length 1");
				}
			}
		} catch (SchemaException e) {
			fail("Exception testing string list", e);
		}
		
		try {
			c.getRequiredStringArray("noexist");
			fail("Found string property under non-existant name");
		} catch (MissingValueException e) {
		} catch (SchemaException e) {
			fail("Wrong exception for missing string list property");
		}
		
		try {
			Iterable<@NonNull String> list= c.getOptionalStringArray("noexist");
			if (list != null) {
				fail("Found optional string property under non-existant name");
			}
		} catch (SchemaException e) {
			fail("Exception testing optional string list");
		}
	}

	@Test
	public void testMap() {
		Composite c= createMap();
		testInteger(c);
		testLong(c);
		testDouble(c);
		testString(c);
		
		testObject(c);
		
		testObjectList(c);
		testIntList(c);
		testStringList(c);
	}
	
	@Test
	public void testJson() {
		Composite c= createJson();
		testInteger(c);
		testLong(c);
		testDouble(c);
		testString(c);
		
		testObject(c);
		
		testObjectList(c);
		testIntList(c);
		testStringList(c);
	}
}
