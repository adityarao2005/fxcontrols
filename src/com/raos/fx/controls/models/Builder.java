package com.raos.fx.controls.models;

public interface Builder<T> extends javafx.util.Builder<T> {
	
	default T construct(Class<T> clazz) {
		try {
			return (T) clazz.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}
}
