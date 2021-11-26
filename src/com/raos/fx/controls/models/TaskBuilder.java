package com.raos.fx.controls.models;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import com.raos.fx.controls.models.occurance.Occurance;

import javafx.collections.FXCollections;

/**
 * Builds the task according
 * 
 * @author Raos
 *
 * @param <T> - The task or any thing that extends it
 */
public class TaskBuilder<T extends Task> implements Builder<T> {
	private String name, description;
	private Occurance occurance;
	private Priority priority;
	private List<SubTask> subTasks;
	protected final Class<T> clazz;

	protected TaskBuilder(Class<T> clazz) {
		this.clazz = clazz;
	}

	/**
	 * @param name - The name to be set
	 * @return - this object
	 */
	public TaskBuilder<T> name(String name) {
		this.name = name;
		return this;
	}

	/**
	 * @param description - The description to be set
	 * @return - this object
	 */
	public TaskBuilder<T> description(String description) {
		this.description = description;
		return this;
	}

	/**
	 * @param occurance - The occurance to be set
	 * @return - this object
	 */
	public TaskBuilder<T> occurance(Occurance occurance) {
		this.occurance = occurance;
		return this;
	}

	/**
	 * @param priority - The priority to be set
	 * @return - this object
	 */
	public TaskBuilder<T> priority(Priority priority) {
		this.priority = priority;
		return this;
	}

	/**
	 * @param subtasks - The subtasks to be set
	 * @return - this object
	 */
	public TaskBuilder<T> subTasks(SubTask... subTasks) {
		this.subTasks = Arrays.asList(subTasks);
		return this;
	}
	
	public TaskBuilder<T> subTasks(Collection<SubTask> subTasks) {
		this.subTasks = FXCollections.observableArrayList(subTasks);
		return this;
	}

	@Override
	public T build() {
		T task = this.construct(clazz);
		applyTo(task);
		return task;
	}
	
	public void applyTo(Task task) {
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
	}

	public static TaskBuilder<? extends Task> create() {
		return new TaskBuilder<>(Task.class);
	}

}
