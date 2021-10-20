package com.raos.fx.controls;

import java.time.LocalDate;
import java.time.LocalTime;

import com.raos.fx.controls.skin.ControlTableSkin;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyListProperty;
import javafx.beans.property.ReadOnlyListWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Control;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Skin;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory.DoubleSpinnerValueFactory;
import javafx.scene.control.SpinnerValueFactory.IntegerSpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.util.Callback;
import javafx.util.Pair;

public class ControlTable<E extends Control, V> extends Control {

	public ControlTable() {
	}

	@Override
	protected Skin<?> createDefaultSkin() {
		return new ControlTableSkin<>(this, items);
	}

	private final ReadOnlyListWrapper<V> items = new ReadOnlyListWrapper<>(this, "items");

	public final ReadOnlyListProperty<V> itemsProperty() {
		return this.items.getReadOnlyProperty();
	}

	public final ObservableList<V> getItems() {
		return this.itemsProperty().get();
	}

	private final ObjectProperty<Callback<V, E>> itemFactory = new SimpleObjectProperty<>(this, "itemFactory");

	public final ObjectProperty<Callback<V, E>> itemFactoryProperty() {
		return this.itemFactory;
	}

	public final Callback<V, E> getItemFactory() {
		return this.itemFactoryProperty().get();
	}

	public final void setItemFactory(final Callback<V, E> itemFactory) {
		this.itemFactoryProperty().set(itemFactory);
	}

	public static ControlTable<CheckBox, Pair<String, Boolean>> newBooleanControlTable() {
		ControlTable<CheckBox, Pair<String, Boolean>> controlTable = new ControlTable<>();
		controlTable.setItemFactory(obj -> {
			CheckBox checkBox = new CheckBox(obj.getKey());
			checkBox.setSelected(obj.getValue());
			return checkBox;
		});
		return controlTable;
	}

	public static ControlTable<Spinner<Integer>, Integer> newIntegerControlTable() {
		ControlTable<Spinner<Integer>, Integer> controlTable = new ControlTable<>();
		controlTable.setItemFactory(obj -> {
			Spinner<Integer> spinner = new Spinner<>();
			spinner.setValueFactory(new IntegerSpinnerValueFactory(Integer.MIN_VALUE, Integer.MAX_VALUE, obj));
			return spinner;
		});
		return controlTable;
	}

	public static ControlTable<Spinner<Double>, Double> newDecimalControlTable() {
		ControlTable<Spinner<Double>, Double> controlTable = new ControlTable<>();
		controlTable.setItemFactory(obj -> {
			Spinner<Double> spinner = new Spinner<>();
			spinner.setValueFactory(new DoubleSpinnerValueFactory(Double.MIN_VALUE, Double.MAX_VALUE, obj));
			return spinner;
		});
		return controlTable;
	}

	public static ControlTable<TextField, String> newStringControlTable() {
		ControlTable<TextField, String> controlTable = new ControlTable<>();
		controlTable.setItemFactory(obj -> new TextField(obj));
		return controlTable;
	}

	public static ControlTable<Spinner<LocalTime>, LocalTime> newTimeControlTable() {
		ControlTable<Spinner<LocalTime>, LocalTime> controlTable = new ControlTable<>();
		controlTable.setItemFactory(obj -> {
			Spinner<LocalTime> spinner = new Spinner<>();
			spinner.setValueFactory(new TimeSpinnerValueFactory());
			spinner.getValueFactory().setValue(obj);
			return spinner;
		});
		return controlTable;
	}

	public static ControlTable<DatePicker, LocalDate> newDateControlTable() {
		ControlTable<DatePicker, LocalDate> controlTable = new ControlTable<>();
		controlTable.setItemFactory(obj -> new DatePicker(obj));
		return controlTable;
	}

	public static ControlTable<ColorPicker, Color> newColorControlTable() {
		ControlTable<ColorPicker, Color> controlTable = new ControlTable<>();
		controlTable.setItemFactory(obj -> new ColorPicker(obj));
		return controlTable;
	}

}
