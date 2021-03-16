/****************************************************************************
 * Copyright (c) 2020 Teaglu, LLC
 * All Rights Reserved
 ****************************************************************************/

package com.teaglu.composite;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNull;

import com.teaglu.composite.exception.MissingValueException;
import com.teaglu.composite.exception.WrongTypeException;

/**
 * Composite
 * 
 * An interface to a dynamically typed key-value object, but where we ask for specific types
 * and throw an exception if we don't get the right thing.
 * 
 * This specified whether the value is required or not as part of the method name, so that
 * the required methods can be decorated with null-check annotations.
 * 
 */
public interface Composite {
	/**
	 * getRequiredInteger
	 * 
	 * Retrieve a required integer by name from the composite.
	 *
	 * @param name					Name of value to retrieve
	 * @return						Integer value
	 * 
	 * @throws MissingValueException
	 * @throws WrongTypeException
	 */
	public int getRequiredInteger(@NonNull String name) throws MissingValueException, WrongTypeException;
	
	/**
	 * getRequiredDouble
	 * 
	 * Retrieve a required double from the composite.
	 *
	 * @param name
	 * @return
	 * @throws MissingValueException
	 * @throws WrongTypeException
	 */
	public double getRequiredDouble(@NonNull String name) throws MissingValueException, WrongTypeException;
	
	/**
	 * getRequiredString
	 * 
	 * Retrieve a required string from the composite.
	 *
	 * @param name					Name of value to retrieve
	 * @return
	 * @throws MissingValueException
	 * @throws WrongTypeException
	 */
	public @NonNull String getRequiredString(@NonNull String name) throws MissingValueException, WrongTypeException;
	
	/**
	 * getRequiredBoolean
	 * 
	 * Retrieve a boolean value by name from the composite.  If the value is not present or does
	 * not have a boolean type, then throw an exception.
	 *
	 * @param name								Name of value to retrieve
	 * @return									boolean value
	 * 
	 * @throws MissingValueException
	 * @throws WrongTypeException
	 */
	public boolean getRequiredBoolean(@NonNull String name) throws MissingValueException, WrongTypeException;
	
	/**
	 * getRequiredLocalDate
	 * 
	 * Retrieve a date without a time from the composite.  If the value is not present, is not a date type,
	 * or cannot be reasonably coerced to one, then throw an exception.
	 *
	 * @param name								Name of value to retrieve
	 * @return
	 * @throws MissingValueException
	 * @throws WrongTypeException
	 */
	public @NonNull LocalDate getRequiredLocalDate(@NonNull String name) throws MissingValueException, WrongTypeException;
	
	/**
	 * getRequiredTimestamp
	 * 
	 * Retrieve a date and time with time zone from the composite.  If the value is not present, is not
	 * a date time, or cannot be reasonably interpreted, then throw an exception.
	 *
	 * @param name								Name of value to retrieve
	 * @return
	 * @throws MissingValueException
	 * @throws WrongTypeException
	 */
	public @NonNull Timestamp getRequiredTimestamp(@NonNull String name) throws MissingValueException, WrongTypeException;
	
	/**
	 * getRequiredObject
	 * 
	 * Retrieve a sub-object as a composite.  If the value is not present or is not an object type then
	 * throw an exception.
	 *
	 * @param name								Name of value
	 * @return									Sub-object as composite
	 * 
	 * @throws MissingValueException
	 * @throws WrongTypeException
	 */
	public @NonNull Composite getRequiredObject(@NonNull String name) throws MissingValueException, WrongTypeException;
	
	/**
	 * getRequiredObjectArray
	 * 
	 * Retreive an array of objects.  If the value is not present or is not an array type then throw an exception
	 *
	 * @param name								Name of array
	 * @return									Iterable of array
	 * 
	 * @throws MissingValueException
	 * @throws WrongTypeException
	 */
	public @NonNull Iterable<@NonNull Composite> getRequiredObjectArray(@NonNull String name) throws MissingValueException, WrongTypeException;
	
	public @NonNull Iterable<@NonNull String> getRequiredStringArray(@NonNull String name) throws MissingValueException, WrongTypeException;
	public @NonNull Iterable<@NonNull Integer> getRequiredIntegerArray(@NonNull String name) throws MissingValueException, WrongTypeException;
	
	public Integer getOptionalInteger(@NonNull String name) throws WrongTypeException;
	public Double getOptionalDouble(@NonNull String name) throws WrongTypeException;
	public String getOptionalString(@NonNull String name) throws WrongTypeException;
	public Boolean getOptionalBoolean(@NonNull String name) throws WrongTypeException;
	public LocalDate getOptionalLocalDate(@NonNull String name) throws WrongTypeException;
	public Timestamp getOptionalTimestamp(@NonNull String name) throws WrongTypeException;
	
	public Composite getOptionalObject(@NonNull String name) throws WrongTypeException;
	
	public Iterable<@NonNull Composite> getOptionalObjectArray(@NonNull String name) throws WrongTypeException;
	
	public Iterable<@NonNull String> getOptionalStringArray(@NonNull String name) throws WrongTypeException;
	public Iterable<@NonNull Integer> getOptionalIntegerArray(@NonNull String name) throws WrongTypeException;
	
	public Iterable<Map.Entry<@NonNull String, @NonNull Composite>> getObjectMap() throws WrongTypeException;
	
	public @NonNull <Representation extends Object> Representation serialize(Class<? extends Representation> representationClass);
}
