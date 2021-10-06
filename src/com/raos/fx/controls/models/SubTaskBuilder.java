package com.raos.fx.controls.models;

public class SubTaskBuilder<T extends SubTask> extends TaskBuilder<T> {
		
	protected SubTaskBuilder(Class<T> clazz) {
		super(clazz);
	}

	public static SubTaskBuilder<SubTask> create() {
		return new SubTaskBuilder<>(SubTask.class);
	}

}
