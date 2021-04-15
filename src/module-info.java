module com.teaglu.composite {
	requires transitive org.eclipse.jdt.annotation;
	requires transitive java.sql;
	requires transitive com.google.gson;
	requires transitive org.postgresql.jdbc;
	
	exports com.teaglu.composite;
	exports com.teaglu.composite.exception;
	exports com.teaglu.composite.json;
}