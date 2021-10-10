package com.raos.fx.controls.models;

/**
 * Allows object relationship and transformation
 * @author Raos
 *
 * @param <T> - Transforms this object to an object of T
 */
@FunctionalInterface
public interface Transformable<T> {
	/**
	 * @param t - An existing value of T, which can be null
	 * @return
	 */
	T transform(T t);
	
	/**
	 * @param <T> - Object
	 * @return - The identity function
	 */
	public static <T> Transformable<T> identity() { return t -> t; }
}
