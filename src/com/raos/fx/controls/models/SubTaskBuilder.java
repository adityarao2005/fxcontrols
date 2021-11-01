package com.raos.fx.controls.models;

/**
 * Builds the subtask according
 * 
 * @author Raos
 *
 * @param <T>
 */
public class SubTaskBuilder<T extends SubTask> extends TaskBuilder<T> {

	protected SubTaskBuilder(Class<T> clazz) {
		super(clazz);
	}

	/**
	 * Creates the builder
	 * 
	 * @return - the builder object
	 */
	public static SubTaskBuilder<SubTask> create() {
		return new SubTaskBuilder<>(SubTask.class);
	}

	public static SubTask transformFrom(Task task) {
		return SubTaskBuilder.create().name(task.getName()).description(task.getDescription())
				.occurance(task.getOccurance()).priority(task.getPriority()).subTasks(task.getSubTasks()).build();
	}
}
