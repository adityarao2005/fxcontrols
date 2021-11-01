package com.raos.fx.controls.models;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.raos.fx.controls.events.TaskSelectedEvent;
import com.raos.fx.controls.skin.SchedulerSkin;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

/**
 * The node representation of a Task
 * 
 * @author Raos
 *
 */
public class TaskNode extends AnchorPane implements Initializable {
	@FXML
	private Label taskName;
	@FXML
	private AnchorPane subTasks;
	private final Task task;

	/**
	 * @param scheduler - The skin of the Scheduler
	 * @param task      - The task to be represented
	 */
	public TaskNode(SchedulerSkin scheduler, Task task) {
		// setting the objects
		this.scheduler = scheduler;
		this.task = task;
		try {
			// Construct the object from the TaskNode.fxml file
			FXMLLoader fxmlLoader = new FXMLLoader(
					this.getClass().getResource("/com/raos/fx/controls/fxml/TaskNode.fxml"));
			// setting the controller and root
			fxmlLoader.setController(this);
			fxmlLoader.setRoot(this);
			fxmlLoader.load();
		} catch (IOException ex) {
			Logger.getLogger(TaskNode.class.getName()).log(Level.SEVERE, null, ex);
		}
		// The style class of this node
		this.getStyleClass().add("task");
		// action onclick
		this.setOnMouseClicked(e -> {
			scheduler.getSkinnable().fireEvent(new TaskSelectedEvent(scheduler, scheduler.getSkinnable(), task));
			e.consume();
		});
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// Name of the task
		taskName.setText(getTask().getName());

		// Add all the tasks to this
		subTasks.getChildren().addAll(getTask().getSubTasks().stream().map(e -> {
			// Get the node version of the subtask
			TaskNode node = e.toNode(getSchedulerSkin());
			// set the layout according to the scheduler skin based on the index of the time
			node.setLayoutY(getSchedulerSkin().getAtIndex(node.getTask().getOccurance().getStartTime()).getValue()
					- getSchedulerSkin().getAtIndex(getTask().getOccurance().getStartTime()).getValue());
			// set the height according to the scheduler skin based on the index of the time
			node.setPrefHeight(getSchedulerSkin().getAtIndex(node.getTask().getOccurance().getEndTime()).getValue()
					- getSchedulerSkin().getAtIndex(node.getTask().getOccurance().getStartTime()).getValue());
			// Set the anchor the node
			AnchorPane.setLeftAnchor(node, 0.0);
			AnchorPane.setRightAnchor(node, 0.0);

			// returns the node
			return node;
			// transform it to an array
		}).toArray(TaskNode[]::new));
	}

	private final SchedulerSkin scheduler;

	/**
	 * @return the Scheduler skin
	 */
	public SchedulerSkin getSchedulerSkin() {
		return scheduler;
	}

	/**
	 * @return the task being represented
	 */
	public Task getTask() {
		return task;
	}

}