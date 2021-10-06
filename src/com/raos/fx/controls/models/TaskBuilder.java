package com.raos.fx.controls.models;

import java.util.Arrays;
import java.util.List;

import com.raos.fx.controls.models.occurance.Occurance;

public class TaskBuilder<T extends Task> implements Builder<T> {
	private String name, description;
	private Occurance occurance;
	private Priority priority;
	private List<SubTask> subTasks;
	protected final Class<T> clazz;
	
	protected TaskBuilder(Class<T> clazz) {
		this.clazz = clazz;
	}
	
	public TaskBuilder<T> name(String name) { this.name = name; return this; }
	public TaskBuilder<T> description(String description) {this.description = description;return this;}
	public TaskBuilder<T> occurance(Occurance occurance) {this.occurance = occurance; return this;}
	public TaskBuilder<T> priority(Priority priority) { this.priority = priority; return this;}
	public TaskBuilder<T> subTasks(SubTask...subTasks) { this.subTasks = Arrays.asList(subTasks); return this;}

	@Override
	public T build() {
		T task = this.construct(clazz);
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
	
	public static TaskBuilder<? extends Task> create() {
		return new TaskBuilder<>(Task.class);
	}

}
