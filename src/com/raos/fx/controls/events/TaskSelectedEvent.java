package com.raos.fx.controls.events;

import com.raos.fx.controls.Scheduler;
import com.raos.fx.controls.models.Task;

import javafx.event.Event;
import javafx.event.EventType;

/**
 * The selection of a task in the Scheduler
 * @author Raos
 *
 */
public final class TaskSelectedEvent extends Event {
	private static final long serialVersionUID = 1L;
	/**
	 * The Common Task Selection Event
	 */
	public static final EventType<TaskSelectedEvent> ANY = new EventType<TaskSelectedEvent>(Event.ANY);
	private final Task task;
		
	/**
	 * @param task - The task that is selected
	 */
	public TaskSelectedEvent(final Task task) {
		this(null, null, task);
	}

	/**
	 * 
	 * @param source - The source of the event
	 * @param target - The target of the event
	 * @param task - The selected task
	 */
	public TaskSelectedEvent(Object source, Scheduler target, final Task task) {
		super(source, target, ANY);
		this.task = task;
	}
	
	/**
	 * @return the selected task
	 */
	public Task getSelectedTask() {
		return task;
	}
}
