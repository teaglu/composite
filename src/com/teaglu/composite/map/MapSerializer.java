package com.teaglu.composite.map;

import java.util.Map;

import org.eclipse.jdt.annotation.NonNull;

public interface MapSerializer {
	public <Representation> @NonNull Representation serialize(
			@NonNull Map<String, Object> tree,
			@NonNull Class<? extends Representation> representationClass);
}
