/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.raos.fx.controls.models;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.URL;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.raos.fx.controls.events.TaskSelectedEvent;
import com.raos.fx.controls.skin.SchedulerSkin;

import javafx.beans.InvalidationListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

/**
 *
 * @author Raos
 */
public class Task implements Serializable {

	public class TaskNode extends AnchorPane implements Initializable {
		@FXML
		private Label taskName;
		@FXML
		private AnchorPane subTasks;

		public TaskNode(SchedulerSkin scheduler) {
			this.scheduler = scheduler;
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
					.fireEvent(new TaskSelectedEvent(scheduler, scheduler.getSkinnable(), Task.this)));
		}

		@Override
		public void initialize(URL location, ResourceBundle resources) {
			taskName.setText(getTask().getName());

			subTasks.getChildren().addAll(getTask().getSubTasks().stream().map(e -> {
				TaskNode node = e.toNode(getSchedulerSkin());
				node.setLayoutY(getSchedulerSkin().getAtIndex(node.getTask().getFrom()).getValue()
						- getSchedulerSkin().getAtIndex(getTask().getFrom()).getValue());
				node.setPrefHeight(getSchedulerSkin().getAtIndex(node.getTask().getTo()).getValue()
						- getSchedulerSkin().getAtIndex(node.getTask().getFrom()).getValue());
				AnchorPane.setLeftAnchor(node, 0.0);
				AnchorPane.setRightAnchor(node, 0.0);

				return node;
			}).toArray(TaskNode[]::new));
		}

		private final SchedulerSkin scheduler;

		public SchedulerSkin getSchedulerSkin() {
			return scheduler;
		}

		public Task getTask() {
			return Task.this;
		}

	}

	public static final long serialVersionUID = 1L;

	private Serializable id;
	private Priority priority = Priority.LOW;
	private LocalTime from, to, reminderTime;
	private LocalDate start, end;
	private EnumSet<DayOfWeek> days = EnumSet.noneOf(DayOfWeek.class);
	private String name, description;
	private transient final ObservableList<SubTask> subTasks;
	private final List<LocalDate> excludedDays = new LinkedList<>();
	private boolean fullDay;

	public static final Task newFullDayTask(String name, String description, LocalDate day) {
		Task task = new Task(LocalTime.MIN, LocalTime.MAX, null, day, day, name, description,
				FXCollections.emptyObservableList());
		task.fullDay = true;
		return task;
	}

	public Task() {
		this(FXCollections.observableArrayList());
	}

	public Task(ObservableList<SubTask> subTasks) {
		this.subTasks = subTasks;
		this.subTasks.addListener((InvalidationListener) l -> {
			this.subTasks.stream().filter(e -> e.getParentTask() == null || !e.getParentTask().equals(this))
					.mapToInt(subTasks::indexOf).forEach(i -> {
						if (subTasks.get(i).getParentTask() != null) {
							subTasks.get(i).getParentTask().getSubTasks().remove(subTasks.get(i));
						}
						subTasks.get(i).setParentTask(this);
					});
		});
	}

	public Task(LocalTime from, LocalTime to, LocalTime reminderTime, LocalDate start, LocalDate end, String name,
			String description, List<SubTask> subTasks) {
		this(FXCollections.observableArrayList(subTasks));
		this.from = from;
		this.to = to;
		this.reminderTime = reminderTime;
		this.start = start;
		this.end = end;
		this.name = name;
		this.description = description;
		this.subTasks.addAll(subTasks);
	}

	public List<LocalDate> getExcludedDays() {
		return excludedDays;
	}

	public Serializable getId() {
		return id;
	}

	public void setId(Serializable id) {
		this.id = id;
	}

	public Priority getPriority() {
		return priority;
	}

	public void setPriority(Priority priority) {
		this.priority = priority;
	}

	public LocalTime getFrom() {
		return from;
	}

	public void setFrom(LocalTime from) {
		this.from = from;
	}

	public LocalTime getTo() {
		return to;
	}

	public void setTo(LocalTime to) {
		this.to = to;
	}

	public LocalTime getReminderTime() {
		return reminderTime;
	}

	public void setReminderTime(LocalTime reminderTime) {
		this.reminderTime = reminderTime;
	}

	public LocalDate getStart() {
		return start;
	}

	public void setStart(LocalDate start) {
		this.start = start;
	}

	public LocalDate getEnd() {
		return end;
	}

	public void setEnd(LocalDate end) {
		this.end = end;
	}

	public EnumSet<DayOfWeek> getDays() {
		return days;
	}

	public void setDays(EnumSet<DayOfWeek> days) {
		this.days = days;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<SubTask> getSubTasks() {
		return subTasks;
	}

	public void writeObject(ObjectOutputStream out) throws IOException {
		out.defaultWriteObject();
		out.writeObject(new ArrayList<>(this.getExcludedDays()));
	}

	@SuppressWarnings("unchecked")
	public void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
		in.defaultReadObject();
		excludedDays.addAll((ArrayList<LocalDate>) in.readObject());
	}

	public boolean intersectsWith(Task other) {
		if (other == null) {
			throw new NullPointerException("other cannot be null");
		}
		if (this.equals(other) || this == other) {
			return false;
		}
		if (other instanceof SubTask) {
			throw new UnsupportedOperationException("other cannot be a subtask");
		}
		return (this.from.isBefore(other.to)) && (other.to.isAfter(this.from));
	}

	public TaskNode toNode(SchedulerSkin scheduler) {
		return new TaskNode(scheduler);
	}

	public boolean isFullday() {
		return fullDay;
	}

	public static enum Priority {
		HIGH, MEDIUM, LOW, URGENT
	}
}
