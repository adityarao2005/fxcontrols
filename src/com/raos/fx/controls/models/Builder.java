package com.raos.fx.controls.models;

/**
 * The extended builder interface to assist with the construct of the object
 * @author Raos
 *
 * @param <T> The object to be built
 */
public interface Builder<T> extends javafx.util.Builder<T> {
	
	default T construct(Class<T> clazz) {
		try {
			return (T) clazz.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}
}
