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

public class TaskNode extends AnchorPane implements Initializable {
	@FXML
	private Label taskName;
	@FXML
	private AnchorPane subTasks;
	private final Task task;

	public TaskNode(SchedulerSkin scheduler, Task task) {
		this.scheduler = scheduler;
		this.task = task;
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(
					this.getClass().getResource("/com/raos/fx/controls/fxml/TaskNode.fxml"));
			fxmlLoader.setController(this);
			fxmlLoader.setRoot(this);
			fxmlLoader.load();
		} catch (IOException ex) {
			Logger.getLogger(TaskNode.class.getName()).log(Level.SEVERE, null, ex);
		}
		this.getStyleClass().add("task");
		this.setOnMouseClicked(e -> scheduler.getSkinnable()
				.fireEvent(new TaskSelectedEvent(scheduler, scheduler.getSkinnable(), task)));
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		taskName.setText(getTask().getName());

		subTasks.getChildren().addAll(getTask().getSubTasks().stream().map(e -> {
			TaskNode node = e.toNode(getSchedulerSkin());
//			System.out.println(getSchedulerSkin().getAtIndex());
			node.setLayoutY(getSchedulerSkin().getAtIndex(node.getTask().getOccurance().getStartTime()).getValue()
					- getSchedulerSkin().getAtIndex(getTask().getOccurance().getStartTime()).getValue());
			node.setPrefHeight(getSchedulerSkin().getAtIndex(node.getTask().getOccurance().getEndTime()).getValue()
					- getSchedulerSkin().getAtIndex(node.getTask().getOccurance().getStartTime()).getValue());
			AnchorPane.setLeftAnchor(node, 0.0);
			AnchorPane.setRightAnchor(node, 0.0);

			return node;
		}).toArray(TaskNode[]::new));
//		this.setLayoutY(getSchedulerSkin().getAtIndex(task.getOccurance().getStartTime()).getValue()
//				- getSchedulerSkin().getAtIndex(task.getOccurance().getStartTime()).getValue());
//		this.setPrefHeight(getSchedulerSkin().getAtIndex(task.getOccurance().getEndTime()).getValue()
//				- getSchedulerSkin().getAtIndex(task.getOccurance().getStartTime()).getValue());
	}

	private final SchedulerSkin scheduler;

	public SchedulerSkin getSchedulerSkin() {
		return scheduler;
	}

	public Task getTask() {
		return task;
	}

}