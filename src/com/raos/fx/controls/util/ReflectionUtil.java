package com.raos.fx.controls.util;

import java.lang.reflect.ParameterizedType;

public final class ReflectionUtil {
	public static Class<?>[] getAllGenericTypes(Class<?> obj) {
		return (Class<?>[]) ((ParameterizedType) obj.getGenericSuperclass()).getActualTypeArguments();
	}
}
