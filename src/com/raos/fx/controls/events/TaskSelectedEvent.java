package com.raos.fx.controls.events;

import com.raos.fx.controls.Scheduler;
import com.raos.fx.controls.models.Task;

import javafx.event.Event;
import javafx.event.EventType;

public final class TaskSelectedEvent extends Event {
	private static final long serialVersionUID = 1L;
	public static final EventType<TaskSelectedEvent> ANY = new EventType<TaskSelectedEvent>(Event.ANY);
	private final Task task;
	
	public TaskSelectedEvent(final Task task) {
		this(null, null, task);
	}

	public TaskSelectedEvent(Object source, Scheduler target, final Task task) {
		super(source, target, ANY);
		this.task = task;
	}
	
	public Task getSelectedTask() {
		return task;
	}
}
