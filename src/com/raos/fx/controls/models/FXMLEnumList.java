package com.raos.fx.controls.models;

import javafx.collections.ObservableList;

public class FXMLEnumList {
	public static Class<?> getEnumClass(ObservableList<?> list) {
		return list.isEmpty() ? null : list.get(0).getClass();
	}

	public static <T extends Enum<T>> void setEnumClass(ObservableList<? super T> list, Class<T> enumClass) {
		if (!enumClass.isEnum()) {
			throw new IllegalArgumentException(enumClass.getName() + " is not a enum type");
		}
		list.addAll(enumClass.getEnumConstants());
	}
}
