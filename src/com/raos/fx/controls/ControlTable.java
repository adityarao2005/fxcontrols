package com.raos.fx.controls;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

import com.raos.fx.controls.skin.ControlTableSkin;
import com.raos.fx.controls.util.ReflectionUtil;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyListProperty;
import javafx.beans.property.ReadOnlyListWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
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

public class ControlTable<E extends Control, V> extends Control {
	
	public ControlTable() {
		getStyleClass().add("control-table");
	}

	@Override
	protected Skin<?> createDefaultSkin() {
		return new ControlTableSkin<>(this, items);
	}

	private StringProperty title = new SimpleStringProperty();

	private final ReadOnlyListWrapper<V> items = new ReadOnlyListWrapper<>(this, "items");

	public final ReadOnlyListProperty<V> itemsProperty() {
		return this.items.getReadOnlyProperty();
	}

	public final ObservableList<V> getItems() {
		return this.itemsProperty().get();
	}

	private ObjectProperty<BiConsumer<E, V>> controlImplementor = new SimpleObjectProperty<>(this,
			"controlImplementor");
	private ObjectProperty<Supplier<E>> controlGenerator = new SimpleObjectProperty<>(this, "controlGenerator");
	@SuppressWarnings("unchecked")
	private ObjectProperty<Supplier<V>> itemFactory = new SimpleObjectProperty<Supplier<V>>(this, "itemFactory", () -> {
		try {
			return (V) ReflectionUtil.getAllGenericTypes(getClass())[1].newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}

	});

	public static ControlTable<CheckBox, Boolean> newBooleanControlTable() {
		ControlTable<CheckBox, Boolean> controlTable = new ControlTable<>();
		controlTable.setControlGenerator(CheckBox::new);
		controlTable.setControlImplementor(CheckBox::setSelected);
		controlTable.setItemFactory(() -> false);
		return controlTable;
	}

	public static ControlTable<Spinner<Integer>, Integer> newIntegerControlTable() {
		ControlTable<Spinner<Integer>, Integer> controlTable = new ControlTable<>();
		controlTable.setControlGenerator(
				() -> new Spinner<>(new IntegerSpinnerValueFactory(Integer.MIN_VALUE, Integer.MAX_VALUE)));
		controlTable.setControlImplementor((s, o) -> s.getValueFactory().setValue(o));
		controlTable.setItemFactory(() -> 0);
		return controlTable;
	}

	public static ControlTable<Spinner<Double>, Double> newDecimalControlTable() {
		ControlTable<Spinner<Double>, Double> controlTable = new ControlTable<>();
		controlTable.setControlGenerator(
				() -> new Spinner<>(new DoubleSpinnerValueFactory(Double.MIN_VALUE, Double.MAX_VALUE)));
		controlTable.setControlImplementor((s, o) -> s.getValueFactory().setValue(o));
		controlTable.setItemFactory(() -> 0.0);
		return controlTable;
	}

	public static ControlTable<TextField, String> newStringControlTable() {
		ControlTable<TextField, String> controlTable = new ControlTable<>();
		controlTable.setControlGenerator(TextField::new);
		controlTable.setControlImplementor(TextField::setText);
		controlTable.setItemFactory(() -> "");
		return controlTable;
	}

	public static ControlTable<Spinner<LocalTime>, LocalTime> newTimeControlTable() {
		ControlTable<Spinner<LocalTime>, LocalTime> controlTable = new ControlTable<>();
		controlTable.setControlGenerator(() -> new Spinner<>(new TimeSpinnerValueFactory()));
		controlTable.setControlImplementor((s, o) -> s.getValueFactory().setValue(o));
		controlTable.setItemFactory(LocalTime::now);
		return controlTable;
	}

	public static ControlTable<DatePicker, LocalDate> newDateControlTable() {
		ControlTable<DatePicker, LocalDate> controlTable = new ControlTable<>();
		controlTable.setControlGenerator(DatePicker::new);
		controlTable.setControlImplementor(DatePicker::setValue);
		controlTable.setItemFactory(LocalDate::now);
		return controlTable;
	}

	public static ControlTable<ColorPicker, Color> newColorControlTable() {
		ControlTable<ColorPicker, Color> controlTable = new ControlTable<>();
		controlTable.setControlGenerator(ColorPicker::new);
		controlTable.setControlImplementor(ColorPicker::setValue);
		controlTable.setItemFactory(() -> Color.BLACK);
		return controlTable;
	}

	public final StringProperty titleProperty() {
		return this.title;
	}

	public final String getTitle() {
		return this.titleProperty().get();
	}

	public final void setTitle(final String title) {
		this.titleProperty().set(title);
	}

	public final ObjectProperty<BiConsumer<E, V>> controlImplementorProperty() {
		return this.controlImplementor;
	}

	public final BiConsumer<E, V> getControlImplementor() {
		return this.controlImplementorProperty().get();
	}

	public final void setControlImplementor(final BiConsumer<E, V> controlImplementor) {
		this.controlImplementorProperty().set(controlImplementor);
	}

	public final ObjectProperty<Supplier<E>> controlGeneratorProperty() {
		return this.controlGenerator;
	}

	public final Supplier<E> getControlGenerator() {
		return this.controlGeneratorProperty().get();
	}

	public final void setControlGenerator(final Supplier<E> controlGenerator) {
		this.controlGeneratorProperty().set(controlGenerator);
	}

	public final ObjectProperty<Supplier<V>> itemFactoryProperty() {
		return this.itemFactory;
	}

	public final Supplier<V> getItemFactory() {
		return this.itemFactoryProperty().get();
	}

	public final void setItemFactory(final Supplier<V> itemFactory) {
		this.itemFactoryProperty().set(itemFactory);
	}

}
