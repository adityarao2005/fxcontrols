package com.raos.fx.controls.models;

import java.util.Arrays;
import java.util.List;

import com.raos.fx.controls.models.occurance.Occurance;

import javafx.util.Builder;

public class SubTaskBuilder implements Builder<Task> {
	private String name, description;
	private Occurance occurance;
	private Priority priority;
	private List<SubTask> subTasks;
	
	private SubTaskBuilder() {
	}
	
	public SubTaskBuilder name(String name) { this.name = name; return this; }
	public SubTaskBuilder description(String description) {this.description = description;return this;}
	public SubTaskBuilder occurance(Occurance occurance) {this.occurance = occurance; return this;}
	public SubTaskBuilder priority(Priority priority) { this.priority = priority; return this;}
	public SubTaskBuilder subTasks(SubTask...subTasks) { this.subTasks = Arrays.asList(subTasks); return this;}

	@Override
	public SubTask build() {
		SubTask task = new SubTask();
		if (name != null) {
			task.setName(name);
		}
		if (description != null) {
			task.setDescription(description);
		}
		if (occurance != null) {
			task.setOccurance(occurance);
		}
		if (priority != null) {
			task.setPriority(priority);
		}
		if (subTasks != null) {
			task.getSubTasks().addAll(subTasks);
		}
		return task;
	}
	
	public static SubTaskBuilder create() {
		return new SubTaskBuilder();
	}

}
