package com.raos.fx.controls.models;

/**
 * Allows object relationship and transformation
 * @author Raos
 *
 * @param <T> - Transforms this object to an object of T
 */
public interface Transformable<T> {
	/**
	 * @param t - An existing value of T, which can be null
	 * @return
	 */
	T transformTo(T t);
	
	Transformable<T> transformFrom(T t);
}
