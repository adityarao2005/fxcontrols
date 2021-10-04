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

	private final int SUNDAY = 0, MONDAY = 1, TUESDAY = 2, WEDNESDAY = 3, THURSDAY = 4, FRIDAY = 5, SATURDAY = 6;

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
		anchors = new AnchorPane[7][6];
		calendarGrid.getChildren().forEach((Node node) -> {
			if (node instanceof AnchorPane) {
				int column = GridPane.getColumnIndex(node),
						row = Optional.ofNullable(GridPane.getRowIndex(node)).orElse(0) - 2;
				anchors[column][row] = (AnchorPane) node;
			}
		});

		this.getSkinnable().currentDateProperty().addListener(l -> {
			YearMonth month0 = YearMonth.from(this.getSkinnable().currentDateProperty().get());
			final SimpleObjectProperty<LocalDate> date = new SimpleObjectProperty<>(month0.atDay(1));
			String mon = CalendarSkin.toCamelCase(month0.getMonth().toString());
			this.month.setText(mon);
			this.month.setEllipsisString(mon);
			this.month.setTextOverrun(OverrunStyle.ELLIPSIS);
			this.year.setText(String.valueOf(date.get().getYear()));

			for (int i = 0; i < anchors.length; i++) {
				for (int j = 0; i < anchors[i].length; i++) {
					anchors[i][j].getChildren().clear();
				}
			}

			for (int row = 0; row < calendarGrid.getRowConstraints().size() - 2; row++) {
				for (int column = SUNDAY; column <= SATURDAY; column++) {
					if (date.isEqualTo(month0.atDay(1)).get()) {
						column = fromDayOfTheWeek(date.get().getDayOfWeek());
					}
					ToggleButton button = new ToggleButton(String.valueOf(date.get().getDayOfMonth()));
					button.getStyleClass().add("calButton");
					button.setEllipsisString(button.getText());
					button.setUserData(date.get());
					button.setOnAction(
							e -> this.getSkinnable().setCurrentDate((LocalDate) ((Node) e.getTarget()).getUserData()));
					AnchorPane.setBottomAnchor(button, 0D);
					AnchorPane.setLeftAnchor(button, 0D);
					AnchorPane.setRightAnchor(button, 0D);
					AnchorPane.setTopAnchor(button, 0D);

					anchors[column][row].getChildren().add(button);

					if (date.isEqualTo(this.getSkinnable().getCurrentDate()).get()) {
						button.setSelected(true);
					}
					if (date.isEqualTo(month0.atEndOfMonth()).get()) {
						row = calendarGrid.getRowConstraints().size() - 2;
						column = SATURDAY;
					}
					date.set(date.get().plusDays(1));
				}
			}
			this.getSkinnable().fireEvent(new ActionEvent(this, this.getSkinnable()));
		});
		this.getSkinnable().setCurrentDate(LocalDate.now());
	}

	public static String toCamelCase(final String init) {
		if (init == null) {
			return null;
		}

		final StringBuilder ret = new StringBuilder(init.length());

		for (final String word : init.split(" ")) {
			if (!word.isEmpty()) {
				ret.append(Character.toUpperCase(word.charAt(0)));
				ret.append(word.substring(1).toLowerCase());
			}
			if (!(ret.length() == init.length())) {
				ret.append(" ");
			}
		}

		return ret.toString();
	}

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
	void onMonthPressed(ActionEvent event) {
		if (event.getTarget() == monthLeft) {
			this.getSkinnable().setCurrentDate(this.getSkinnable().getCurrentDate().minusMonths(1));
		} else if (event.getTarget() == monthRight) {
			this.getSkinnable().setCurrentDate(this.getSkinnable().getCurrentDate().plusMonths(1));
		}
	}

	@FXML
	void onYearPressed(ActionEvent event) {
		if (event.getTarget() == yearLeft) {
			this.getSkinnable().setCurrentDate(this.getSkinnable().getCurrentDate().minusYears(1));
		} else if (event.getTarget() == yearRight) {
			this.getSkinnable().setCurrentDate(this.getSkinnable().getCurrentDate().plusYears(1));
		}
	}

}
