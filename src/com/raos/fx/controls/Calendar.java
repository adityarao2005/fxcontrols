package com.raos.fx.controls;

import com.raos.fx.controls.skin.CalendarSkin;
import java.time.LocalDate;
import javafx.beans.NamedArg;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;

public final class Calendar extends Control {
	
	private final ObjectProperty<LocalDate> currentDate = new SimpleObjectProperty<>(this, "currentDate");

	@Override
	protected Skin<?> createDefaultSkin() {
		return new CalendarSkin(this);
	}

	public Calendar() {
		this(LocalDate.now());
		getStyleClass().add("calendar");
	}

	public Calendar(@NamedArg("date") LocalDate date) {
		setCurrentDate(date);
	}

	public final ObjectProperty<EventHandler<ActionEvent>> onActionProperty() {
		return onAction;
	}

	public final void setOnAction(EventHandler<ActionEvent> value) {
		onActionProperty().set(value);
	}

	public final EventHandler<ActionEvent> getOnAction() {
		return onActionProperty().get();
	}

	private final ObjectProperty<EventHandler<ActionEvent>> onAction = new ObjectPropertyBase<EventHandler<ActionEvent>>() {
		@Override
		protected void invalidated() {
			setEventHandler(ActionEvent.ACTION, get());
		}

		@Override
		public Object getBean() {
			return Calendar.this;
		}

		@Override
		public String getName() {
			return "onAction";
		}
	};

	public final ObjectProperty<LocalDate> currentDateProperty() {
		return this.currentDate;
	}

	public final LocalDate getCurrentDate() {
		return this.currentDateProperty().get();
	}

	public final void setCurrentDate(final LocalDate currentDate) {
		this.currentDateProperty().set(currentDate);
	}


}
