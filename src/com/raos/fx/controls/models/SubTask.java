/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.raos.fx.controls.models;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author Raos
 */
public final class SubTask extends Task {
	public static final long serialVersionUID = 1L;
	private Task parentTask;

	public SubTask() {
		super();
	}

	public SubTask(LocalTime from, LocalTime to, LocalTime reminderTime, LocalDate start, LocalDate end, String name, String description, List<SubTask> subTasks) {
		super(from, to, reminderTime, start, end, name, description, subTasks);
	}
	
	public Task getParentTask() {
		return parentTask;
	}

	void setParentTask(Task parentTask) {
		this.parentTask = parentTask;
	}

	@Override
	public int hashCode() {
		int hash = super.hashCode();
		hash = 13 * hash + Objects.hashCode(this.parentTask);
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (!super.equals(obj)) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final SubTask other = (SubTask) obj;
		return Objects.equals(this.parentTask, other.parentTask);
	}
	
	public Task getAncestor() {
		Task task = this.getParentTask();
		while (task != null && task instanceof SubTask) {
			task = ((SubTask) task).getParentTask();
		}
		return task;
	}
}
