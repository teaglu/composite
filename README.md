# Composite

## Description

Composite is a support library to reduce boilerplate in parsing dynamic data structures.  This
library wraps libraries such as GSON and SnakeYaml to provide strongly-opinionated method calls
that throw understandable exceptions if the underlying document does not match the expected schema.

## Design

### Strongly Opinionated Method Calls

A strongly opinionated method call implies requirements such as the presence or format of the
value, to prevent your code from having to check these things on a generic object.  To give an
example, consider the following Composite method:

    public @NonNull Timestamp getTimestamp(
        @NonNull String name) throws SchemaException;

The method performs the following steps to keep you from having to write boilerplate code:

* Verifies that the named member exists, and if not throws a MissingValueException
* Verifies that the value is not null, and if it is throws a MissingValueException
* Verifies that the value is a string, and if not throws a WrongTypeException
* Verifies that the value is a correctly encoded RFC8601 date, and if not throws a
  WrongTypeException.
* If the time portion of the value is not included, converts it to start of day using the
  timezone that was passed in during creation.
* Returns the value as a SQL timestamp object

All exceptions thrown by the method are guaranteed to be a subset of SchemaException, so you
can place all of your accesses in a single try block.  The exception will include the full path
of the problem within the original tree structure as a dot-separated string such as the
following:

    car.doors[3].latch

### @NonNull Annotations

Our internal code is based on the @NonNull annotation to avoid null pointer exceptions.  The
Composite library wraps the underlying libraries to support this practice.

## Multiple Formats

The Composite interface supports both JSON and YAML syntax, and presents both as the same
interface.  Creation always encapsulates a TimeZone object, which is used to correctly handle
dates and timestamps encoded as strings.  Most creation calls have a version with a timezone
and one without - the one without assumed GMT.

### Creating a JSON Instance

A JSON-based Composite is created using static members of the YamlComposite class.

A Composite may be created by passing in a member of the underlying GSON library:

    public static @NonNull Composite Create(
        @NonNull JsonElement element) throws SchemaException;
        
    public static @NonNull Composite Create(
        @NonNull JsonElement element,
        @NonNull TimeZone timezone) throws SchemaException;

It can be created by parsing a JSON source, passed in as a literal or read from a stream:

    public static @NonNull Composite Parse(
        @NonNull String json) throws SchemaException;
        
    public static @NonNull Composite Parse(
        @NonNull String json,
        @NonNull TimeZone timezone) throws SchemaException;
        
    public static @NonNull Composite Parse(
        @NonNull InputStreamReader reader) throws SchemaException;
        
    public static @NonNull Composite Parse(
        @NonNull InputStreamReader reader,
        @NonNull TimeZone timezone) throws SchemaException;

Finally, it can be directly passed a PGobject to parse a Postgres JSONB column:

    public static @Nullable Composite ParseObject(
        @Nullable PGobject pgObject) throws SchemaException;
			
    public static @Nullable Composite ParseObject(
        @Nullable PGobject pgObject,
        @NonNull TimeZone timezone) throws SchemaException;
			
    public static @NonNull List<@Nullable Composite> ParseArray(
        @Nullable PGobject pgObject) throws SchemaException;
			
    public static @NonNull List<@Nullable Composite> ParseArray(
        @Nullable PGobject pgObject,
        @NonNull TimeZone timezone) throws SchemaException;
        
### Creating a YAML Instance

A YAML-based Composite is created using static members of the YamlComposite class.  You
can supply the YAML source using either a literal string or a stream reader:

    public static @NonNull Composite Parse(
        @NonNull String yaml) throws SchemaException;
        
    public static @NonNull Composite Parse(
        @NonNull String yaml,
        @NonNull TimeZone timezone) throws SchemaException;
        
    public static @NonNull Composite Parse(
        @NonNull InputStreamReader reader) throws SchemaException;
        
    public static @NonNull Composite Parse(
        @NonNull InputStreamReader reader,
        @NonNull TimeZone timezone) throws SchemaException;