package com.teaglu.composite;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.TreeMap;

import org.eclipse.jdt.annotation.NonNull;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.teaglu.composite.map.MapCompositeImpl;
import com.teaglu.composite.map.MapSerializer;

public class MapInterfaceTest extends CompositeTest {
	private static class DummySerializer implements MapSerializer {
		@Override
		public <Representation> @NonNull Representation serialize(
				@NonNull Map<String, Object> tree,
				@NonNull Class<? extends Representation> representationClass)
		{
			throw new RuntimeException("Serializer not implemented");
		}
	}
	
	@BeforeAll
	public static void createMap() {
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
		reference= new MapCompositeImpl(test, timezone, new DummySerializer(), null);
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
