/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.raos.fx.controls.skin;

import com.raos.fx.controls.Calendar;
import java.net.URL;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.OverrunStyle;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

/**
 * The Default Skin of the Calendar object
 * 
 * @author Raos
 */
public class CalendarSkin extends FXRootSkinBase<Calendar, BorderPane> {

	@FXML
	private GridPane calendarGrid;
	@FXML
	private Button monthLeft;
	@FXML
	private Label month;
	@FXML
	private Button monthRight;
	@FXML
	private Button yearLeft;
	@FXML
	private Label year;
	@FXML
	private Button yearRight;
	private AnchorPane[][] anchors;

	/**
	 * Days of the week from Sunday start
	 */
	private final int SUNDAY = 0, MONDAY = 1, TUESDAY = 2, WEDNESDAY = 3, THURSDAY = 4, FRIDAY = 5, SATURDAY = 6;

	/**
	 * @param calendar - the Calendar object defaulted by the SkinBase class
	 */
	public CalendarSkin(Calendar calendar) {
		super(calendar, CalendarSkin.class.getResource("/com/raos/fx/controls/fxml/CalendarView.fxml"),
				BorderPane::new);
	}

	/**
	 * Initializes the controller class.
	 *
	 * @param url
	 * @param rb
	 */
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		// anchor pane for each button row and column
		anchors = new AnchorPane[7][6];
		// for each of the "button containers" add a reference of it in the
		// multidimensional array
		calendarGrid.getChildren().forEach((Node node) -> {
			if (node instanceof AnchorPane) {
				int column = GridPane.getColumnIndex(node),
						row = Optional.ofNullable(GridPane.getRowIndex(node)).orElse(0) - 2;
				anchors[column][row] = (AnchorPane) node;
			}
		});

		// When the current date property is invalidated update the buttons
		this.getSkinnable().currentDateProperty().addListener(l -> {
			// Get the month of the year from the current date
			YearMonth month0 = YearMonth.from(this.getSkinnable().currentDateProperty().get());
			// Get the month data
			final SimpleObjectProperty<LocalDate> date = new SimpleObjectProperty<>(month0.atDay(1));
			String mon = CalendarSkin.toCamelCase(month0.getMonth().toString());
			// set the label values
			this.month.setText(mon);
			this.month.setEllipsisString(mon);
			this.month.setTextOverrun(OverrunStyle.ELLIPSIS);
			this.year.setText(String.valueOf(date.get().getYear()));

			// Clear the children of all the anchors
			for (int i = 0; i < anchors.length; i++) {
				for (int j = 0; i < anchors[i].length; i++) {
					anchors[i][j].getChildren().clear();
				}
			}

			// foreach anchor map the date with a button
			for (int row = 0; row < calendarGrid.getRowConstraints().size() - 2; row++) {
				for (int column = SUNDAY; column <= SATURDAY; column++) {
					// distribute the rows and columns accordingly
					if (date.isEqualTo(month0.atDay(1)).get()) {
						column = fromDayOfTheWeek(date.get().getDayOfWeek());
					}

					// Create the date button
					ToggleButton button = new ToggleButton(String.valueOf(date.get().getDayOfMonth()));
					button.getStyleClass().add("calButton");
					// set ellipsis string in case of overflow
					button.setEllipsisString(button.getText());
					// set userdata as date
					button.setUserData(date.get());
					button.setOnAction(
							e -> this.getSkinnable().setCurrentDate((LocalDate) ((Node) e.getTarget()).getUserData()));
					AnchorPane.setBottomAnchor(button, 0D);
					AnchorPane.setLeftAnchor(button, 0D);
					AnchorPane.setRightAnchor(button, 0D);
					AnchorPane.setTopAnchor(button, 0D);

					// add the button to the anchorpane at the given coordinates
					anchors[column][row].getChildren().add(button);

					// if the date is this button set it as selected
					if (date.isEqualTo(this.getSkinnable().getCurrentDate()).get()) {
						button.setSelected(true);
					}
					// if the date is at the end of the month end the loop
					if (date.isEqualTo(month0.atEndOfMonth()).get()) {
						row = calendarGrid.getRowConstraints().size() - 2;
						column = SATURDAY;
					}
					// set the date so that it is incremented
					date.set(date.get().plusDays(1));
				}
			}
			// fire the event
			this.getSkinnable().fireEvent(new ActionEvent(this, this.getSkinnable()));
		});
		// default set the date to now
		this.getSkinnable().setCurrentDate(LocalDate.now());
	}

	/**
	 * Utility method, can be used for anything but kept here for it's own purpose
	 * @param init - original string
	 * @return the string in camel case
	 */
	public static String toCamelCase(final String init) {
		if (init == null) {
			return null;
		}

		final StringBuilder ret = new StringBuilder(init.length());

		// make the words camel case by splitting it by every space and set the word in uppercase
		for (final String word : init.split("\\s")) {
			if (!word.trim().isEmpty()) {
				ret.append(Character.toUpperCase(word.charAt(0)));
				ret.append(word.substring(1).toLowerCase());
			}
			if (!(ret.length() == init.length())) {
				ret.append(" ");
			}
		}

		return ret.toString();
	}

	/**
	 * DayOfWeek enum to Canadian/US/European standard
	 * @param week
	 * @return
	 */
	private int fromDayOfTheWeek(DayOfWeek week) {
		switch (week) {
		case SUNDAY:
			return SUNDAY;
		case MONDAY:
			return MONDAY;
		case TUESDAY:
			return TUESDAY;
		case WEDNESDAY:
			return WEDNESDAY;
		case THURSDAY:
			return THURSDAY;
		case FRIDAY:
			return FRIDAY;
		case SATURDAY:
			return SATURDAY;
		}
		return 0;
	}

	@FXML
	private void onMonthPressed(ActionEvent event) {
		// month presses
		if (event.getTarget() == monthLeft) {
			this.getSkinnable().setCurrentDate(this.getSkinnable().getCurrentDate().minusMonths(1));
		} else if (event.getTarget() == monthRight) {
			this.getSkinnable().setCurrentDate(this.getSkinnable().getCurrentDate().plusMonths(1));
		}
	}

	@FXML
	private void onYearPressed(ActionEvent event) {
		// year presses
		if (event.getTarget() == yearLeft) {
			this.getSkinnable().setCurrentDate(this.getSkinnable().getCurrentDate().minusYears(1));
		} else if (event.getTarget() == yearRight) {
			this.getSkinnable().setCurrentDate(this.getSkinnable().getCurrentDate().plusYears(1));
		}
	}

}
