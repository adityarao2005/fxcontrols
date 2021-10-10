package com.raos.fx.controls.models;

import javafx.collections.ObservableList;

/**
 * Gets and Sets the list of all Enumeration with in the Enum
 * @author Raos
 *
 */
public class FXMLEnumList {

	/**
	 * Gets the list of enums to {@link Class}
	 * @param list - the list of the enums
	 * @return the class
	 */
	public static Class<?> getEnumClass(ObservableList<?> list) {
		return list.isEmpty() ? null : list.get(0).getClass();
	}

	/**
	 * Sets the the list of enums from the class
	 * @param <T> - The Enum Class
	 * @param list - The List of enums
	 * @param enumClass - The class of the enum
	 */
	public static <T extends Enum<T>> void setEnumClass(ObservableList<? super T> list, Class<T> enumClass) {
		if (!enumClass.isEnum()) {
			throw new IllegalArgumentException(enumClass.getName() + " is not a enum type");
		}
		list.addAll(enumClass.getEnumConstants());
	}
}
