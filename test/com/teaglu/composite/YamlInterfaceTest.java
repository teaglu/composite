package com.teaglu.composite;

import static org.junit.jupiter.api.Assertions.fail;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.jdt.annotation.NonNull;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.teaglu.composite.exception.SchemaException;
import com.teaglu.composite.yaml.YamlComposite;

public class YamlInterfaceTest extends CompositeTest {
	@BeforeAll
	public static void createReference() {
		List<String> lines= new ArrayList<>(20);
		
		lines.add("intProperty: 3");
		lines.add("longProperty: 3");
		lines.add("stringProperty: stuff");
		lines.add("doubleProperty1: 3.0");
		lines.add("doubleProperty2: 3.4");
		lines.add("localDateProperty: 2023-01-01");
		lines.add("timestampProperty: 2023-01-01T12:00:00Z");
		lines.add("intListProperty:");
		lines.add("  - 3");
		lines.add("stringListProperty:");
		lines.add("  - stuff");
		lines.add("objectListProperty:");
		lines.add("  - stuff: things");
		lines.add("objectProperty:");
		lines.add("  entry1:");
		lines.add("    stuff: things");	// YAML doesn't create object without members
		lines.add("  entry2:");
		lines.add("    stuff: things");

		@SuppressWarnings("null")
		@NonNull String text= String.join("\n", lines);
		
		try {
			reference= YamlComposite.Parse(text);
		} catch (SchemaException e) {
			fail("Failed to parse static YAML example");
		}
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
