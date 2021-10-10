package com.raos.fx.controls.skin;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.FormatStyle;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.stream.IntStream;

import com.raos.fx.controls.Scheduler;
import com.raos.fx.controls.models.SubTask;
import com.raos.fx.controls.models.Task;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.util.Pair;
import javafx.util.converter.LocalTimeStringConverter;

/**
 * The Skin for the Scheduler Control
 * 
 * @author Raos
 *
 */
public class SchedulerSkin extends FXRootSkinBase<Scheduler, ScrollPane> {
	@FXML
	private GridPane gridPane;
	@FXML
	private Label date;
	private static final LocalTimeStringConverter converter = new LocalTimeStringConverter(FormatStyle.SHORT,
			Locale.CANADA);
	private Map<LocalTime, Pair<Integer, Integer>> indeces;

	/**
	 * Constructs the SchedulerSkin class
	 * 
	 * @param e - the scheduler that this class is the skin of
	 */
	public SchedulerSkin(Scheduler e) {
		// Filling in super types
		super(e, SchedulerSkin.class.getResource("/com/raos/fx/controls/fxml/scheduler.fxml"), ScrollPane::new);

		this.getSkinnable().currentLocalDate().addListener((obs, oldv, newv) -> {
			this.displayTasks(Scheduler.getTaskFactory().call(newv));
		});
		this.getSkinnable().setCurrentLocalDate(LocalDate.now());
	}

	/**
	 * Gets the row and layout value at the time
	 * 
	 * @param lt - the time/index
	 * @return the pair of row number and layout value
	 */
	public Pair<Integer, Integer> getAtIndex(LocalTime lt) {
		return indeces.get(lt);
	}

	// Displays the tasks
	private void displayTasks(List<Task> tasks) {
		// Removes all tasks
		gridPane.getChildren().removeIf(e -> e.getStyleClass().contains("task"));
		// sets the date
		date.setText(this.getSkinnable().getCurrentLocalDate().toString());
		// row and column are initialized
		IntegerProperty column = new SimpleIntegerProperty(2);
		IntegerProperty row = new SimpleIntegerProperty(0);
		// the tasks are added to the scheduler and those which times conflict are sent
		// to the next row
		List<Task> added = new LinkedList<>();
		tasks.stream().forEach((Task task) -> {
			// Full day tasks are rendered differently
			if (!Objects.requireNonNull(task).getOccurance().isFullDay()) {
				if (task instanceof SubTask) {
					throw new UnsupportedOperationException("Subtasks are not the main tasks");
				}
				if (added.stream().map(Task::getOccurance).anyMatch(task.getOccurance()::intersectsWith)) {
					column.set(column.add(1).get());
				}
				added.add(task);

				// Adds the task to the scheduler
				Node node = task.toNode(this);
				gridPane.add(node, column.get(), indeces.get(task.getOccurance().getStartTime()).getKey(), 1,
						indeces.get(task.getOccurance().getEndTime()).getKey()
								- indeces.get(task.getOccurance().getStartTime()).getKey());
			} else {
				Node node = task.toNode(this);
				gridPane.add(node, 1, row.get(), gridPane.getColumnConstraints().size() - 1, 1);
				row.set(row.add(1).get());
			}
		});
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// creates indeces
		indeces = FXCollections.observableHashMap();
		// the factor is made for testing in the earlier stage of construction of this
		// skin to organize the time difference and length of each minute
		int factor = 60;
		for (int i = 0; i < 24; i++) {
			// converting time to string
			String time = converter.toString(LocalTime.of(i, 0));
			// foreach minute add the index to the indeces
			IntStream.range(0, factor).forEach(e -> {
				indeces.put(converter.fromString(time).plusMinutes(e * (60 / factor)),
						new Pair<>(gridPane.getRowConstraints().size(), Double.valueOf(
								gridPane.getRowConstraints().stream().mapToDouble(RowConstraints::getPrefHeight).sum())
								.intValue()));
				gridPane.getRowConstraints().add(new RowConstraints(240 / factor));
			});
			// create the label for time
			AnchorPane anchorPane = new AnchorPane();
			Label label = new Label(time);
			label.setAlignment(Pos.CENTER);
			AnchorPane.setBottomAnchor(label, Double.valueOf(0));
			AnchorPane.setLeftAnchor(label, Double.valueOf(0));
			AnchorPane.setTopAnchor(label, Double.valueOf(0));
			AnchorPane.setRightAnchor(label, Double.valueOf(0));
			anchorPane.getChildren().add(label);
			gridPane.add(anchorPane, 0, i * (factor + 1) + 4, 1, factor);

			gridPane.getRowConstraints().add(new RowConstraints());

			// Add a separator at the end of the hour in the 
			gridPane.add(new Separator(Orientation.HORIZONTAL), 0, i * (factor + 1) + factor + 4,
					gridPane.getColumnConstraints().size(), 1);

		}

		gridPane.add(new Separator(Orientation.VERTICAL), 1, 0, 1, gridPane.getRowConstraints().size());
	}

}
