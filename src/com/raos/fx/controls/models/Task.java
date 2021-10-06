package com.raos.fx.controls.models;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.raos.fx.controls.models.occurance.Occurance;
import com.raos.fx.controls.skin.SchedulerSkin;

import javafx.beans.property.ListPropertyBase;
import javafx.collections.FXCollections;

/**
 * <h1>Task Design</h1>
 * 
 * Design of the class includes
 * <ul>
 * <li>Name</li>
 * <li>Description</li>
 * <li>Occurrence</li>
 * <li>Priority</li>
 * <li>Subtasks</li>
 * </ul>
 * <br>
 * 
 * Subtask design
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
 *
 * @author Raos
 */
public class Task implements Transformable<Map<String, Object>> {
	private String name, description;
	private Occurance occurance;
	private Priority priority;
	private final List<SubTask> subTasks = new ListPropertyBase<SubTask>(FXCollections.observableArrayList()) {

		protected void invalidated() {
			subTasks.stream().filter(e -> e.getParentTask() == null || !e.getParentTask().equals(Task.this))
					.mapToInt(subTasks::indexOf).forEach(i -> {
						if (subTasks.get(i).getParentTask() != null) {
							subTasks.get(i).getParentTask().getSubTasks().remove(subTasks.get(i));
						}
						subTasks.get(i).setParentTask(Task.this);
					});
		}

		@Override
		public Object getBean() {
			return Task.this;
		}

		@Override
		public String getName() {
			return "subTasks";
		}
	};

	public Task() {
	}

	public final String getName() {
		return name;
	}

	public final void setName(String name) {
		this.name = name;
	}

	public final String getDescription() {
		return description;
	}

	public final void setDescription(String description) {
		this.description = description;
	}

	public final Occurance getOccurance() {
		return occurance;
	}

	public final void setOccurance(Occurance occurance) {
		this.occurance = occurance;
	}

	public final Priority getPriority() {
		return priority;
	}

	public final void setPriority(Priority priority) {
		this.priority = priority;
	}

	public final List<SubTask> getSubTasks() {
		return subTasks;
	}

	public final TaskNode toNode(SchedulerSkin scheduler) {
		return new TaskNode(scheduler, this);
	}

	@Override
	public Map<String, Object> transform(
			Map<String, Object> list) {
		Map<String, Object> map = Optional.ofNullable(list).orElseGet(HashMap::new);
		map.put("Name", name);
		map.put("Description", description);
		map.put("Priority", priority);
		map.put("Occurance", occurance.transform(null));
		map.put("Sub Tasks", subTasks.stream().map(e -> e.transform(null)).collect(Collectors.toList()));
		return map;
	}
}
