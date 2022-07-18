# Composite

## Description

Composite is a support library to reduce boilerplate in parsing dynamic data structures.  This
library wraps libraries such as GSON to provide strongly-opinionated method calls that throw
understandable exceptions if the underlying document does not match the expected schema.

## Design

### Strongly Opinionated Method Calls

To give an example, a Composite method call is getRequiredLocalDate.  This function handles
checking for null values, any required type checking on the underlying document, and converting
the data value itself to the requested type.  If anything is wrong, it throws a subclass of
SchemaException to indicate the problem.

### @NonNull Annotations

Our internal code is based on the @NonNull decoration to get rid of bugs.  The Composite library
wraps the underlying libraries to support this practice.

### Multiple Formats

The Composite interface is built to support both JSON and YAML, although YAML isn't implemented
yet.
