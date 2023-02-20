package com.teaglu.composite;

import java.util.TimeZone;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.teaglu.composite.json.JsonCompositeImpl;

public class JsonInterfaceTest extends CompositeTest {
	@BeforeAll
	public static void createJson() {
		JsonObject object= new JsonObject();
		
		object.addProperty("intProperty", 3);
		object.addProperty("longProperty", 3L);
		object.addProperty("stringProperty", "stuff");
		object.addProperty("doubleProperty1", 3.0F);
		object.addProperty("doubleProperty2", 3.4F);
		object.addProperty("localDateProperty", "2023-01-01");
		object.addProperty("timestampProperty", "2023-01-01T12:00:00Z");
		
		JsonArray intList= new JsonArray();
		intList.add(3);
		object.add("intListProperty", intList);
		
		JsonArray stringList= new JsonArray();
		stringList.add("stuff");
		object.add("stringListProperty", stringList);
		
		JsonArray objectList= new JsonArray();
		objectList.add(new JsonObject());
		object.add("objectListProperty", objectList);
		
		JsonObject mapObject= new JsonObject();
		mapObject.add("entry1", new JsonObject());
		mapObject.add("entry2", new JsonObject());
		object.add("objectProperty", mapObject);
		
		TimeZone timezone= TimeZone.getTimeZone("America/New_York");
		if (timezone == null) {
			throw new RuntimeException("Failed to load timezone");
		}

		reference= new JsonCompositeImpl(object, timezone, null);
	}
	
	private static Composite reference;

	@Test
	public void testInteger() {
		testInteger(reference);
	}
	
	@Test
	public void testLong() {
		testLong(reference);
	}
	
	@Test
	public void testDouble() {
		testLong(reference);
	}
	
	@Test
	public void testString() {
		testString(reference);
	}
	
	@Test
	public void testObject() {
		testObject(reference);
	}
	
	@Test
	public void testObjectList() {
		testObjectList(reference);
	}
	
	@Test
	public void testIntList() {
		testIntList(reference);
	}
	
	@Test
	public void testStringList() {
		testStringList(reference);
	}
}
