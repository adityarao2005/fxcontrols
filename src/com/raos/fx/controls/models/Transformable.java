package com.raos.fx.controls.models;

@FunctionalInterface
public interface Transformable<T> {
	T transform(T t);
	
	public static <T> Transformable<T> indentity() { return t -> t; }
}
