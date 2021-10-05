package com.raos.fx.controls.models;

public class SubTask extends Task {
	private Task parentTask;

	public final Task getParentTask() {
		return parentTask;
	}

	public final void setParentTask(Task parentTask) {
		this.parentTask = parentTask;
	}
}