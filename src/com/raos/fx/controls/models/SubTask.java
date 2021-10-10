package com.raos.fx.controls.models;

/**
 * Subtask extra features
 * <ul>
 * <li>Parent task</li>
 * </ul>
 * 
 * Other details
 * <ul>
 * <li>To node</li>
 * <li>intersects with other tasks</li>
 * </ul>
 *
 * @author Raos
 *
 */
public class SubTask extends Task {
	private Task parentTask;

	public final Task getParentTask() {
		return parentTask;
	}

	final void setParentTask(Task parentTask) {
		this.parentTask = parentTask;
	}
}