package com.raos.fx.controls;

import com.raos.fx.controls.skin.SchedulerSkin;
import com.raos.fx.controls.events.TaskSelectedEvent;
import com.raos.fx.controls.models.Task;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javafx.beans.property.ReadOnlyListProperty;
import javafx.beans.property.ReadOnlyListWrapper;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import javafx.util.Callback;
import javafx.util.Pair;

/**
 *
 * @author Raos
 */
public class Scheduler extends Control {
	// currentLocalDate
	private final ObjectProperty<LocalDate> currentLocalDate = new SimpleObjectProperty<>(this, "currentLocalDate");
	public static final Callback<LocalDate, ReadOnlyListProperty<Task>> DEFAULT_TASK_FACTORY = MemoryContainer::taskFactory;
	public static final Callback<Change<Task>, Boolean> DEFAULT_TASK_MOD_FACTORY = MemoryContainer::taskModFactory;

	public final ObjectProperty<LocalDate> currentLocalDate() {
		return currentLocalDate;
	}

	public final LocalDate getCurrentLocalDate() {
		return currentLocalDate().get();
	}

	public final void setCurrentLocalDate(LocalDate currentLocalDate) {
		this.currentLocalDate().set(currentLocalDate);
	}

	// taskFactory and taskModFactory
	private final ObjectProperty<Callback<LocalDate, ReadOnlyListProperty<Task>>> taskFactory = new SimpleObjectProperty<>(
			this, "taskFactory", DEFAULT_TASK_FACTORY);

	private final ObjectProperty<Callback<Change<Task>, Boolean>> taskModFactory = new SimpleObjectProperty<>(this,
			"taskModFactory", DEFAULT_TASK_MOD_FACTORY);

	public Callback<LocalDate, ReadOnlyListProperty<Task>> getTaskFactory() {
		return taskFactory.get();
	}

	public void setTaskFactory(Callback<LocalDate, ReadOnlyListProperty<Task>> value) {
		taskFactory.set(value);
	}

	public ObjectProperty<Callback<LocalDate, ReadOnlyListProperty<Task>>> taskFactoryProperty() {
		return taskFactory;
	}

	public Callback<Change<Task>, Boolean> getTaskModFactory() {
		return taskModFactory.get();
	}

	public void setTaskModFactory(Callback<Change<Task>, Boolean> value) {
		taskModFactory.set(value);
	}

	public ObjectProperty<Callback<Change<Task>, Boolean>> taskModFactoryProperty() {
		return taskModFactory;
	}

	// onTaskSelected
	private final ObjectProperty<EventHandler<TaskSelectedEvent>> onTaskSelected = new ObjectPropertyBase<EventHandler<TaskSelectedEvent>>() {

		protected void invalidated() {
			setEventHandler(TaskSelectedEvent.ANY, get());
		};

		@Override
		public Object getBean() {
			return Scheduler.this;
		}

		@Override
		public String getName() {
			return "onTaskSelected";
		}
	};

	public Scheduler() {

	}

	@Override
	protected Skin<?> createDefaultSkin() {
		return new SchedulerSkin(this);
	}

	public final ObjectProperty<EventHandler<TaskSelectedEvent>> onTaskSelectedProperty() {
		return this.onTaskSelected;
	}

	public final EventHandler<TaskSelectedEvent> getOnTaskSelected() {
		return this.onTaskSelectedProperty().get();
	}

	public final void setOnTaskSelected(final EventHandler<TaskSelectedEvent> onTaskSelected) {
		this.onTaskSelectedProperty().set(onTaskSelected);
	}

	private static class MemoryContainer {

		private static final ObservableList<Task> tasks = FXCollections
				.synchronizedObservableList(FXCollections.observableArrayList());

		private static boolean isBetween(LocalDate current, LocalDate start, LocalDate end) {
			return (!current.isBefore(start)) && (!current.isAfter(end));
		}

		private static ReadOnlyListProperty<Task> taskFactory(LocalDate date) {
			System.out.println(date);
			return new ReadOnlyListWrapper<Task>(MemoryContainer.class, "tasks", tasks.stream().filter(e -> {
				System.out.println(isBetween(date, e.getStart(), e.getEnd()));
				return isBetween(date, e.getStart(), e.getEnd());
			}).collect(FXCollections::observableArrayList, Collection::add, Collection::addAll)).getReadOnlyProperty();
		}

		private static boolean taskModFactory(Change<Task> change) {
			switch (change.getChanged()) {
			case ADDED:
				return tasks.addAll(change.added().stream().collect(Collectors.toList()));
			case REMOVED:
				return tasks.removeAll(change.removed());
			case UPDATED:
				boolean b = true;
				try {
					tasks.set(tasks.indexOf(change.updated().getKey()), change.updated().getValue());
				} catch (Exception e) {
					b = false;
				}
				return b;
			}
			return false;
		}
	}

	public static abstract class Change<E> {

		private List<E> added;
		private List<E> removed;
		private Pair<E, E> updated;

		public static <E> Change<E> listAddedChange(List<E> added) {
			Change<E> change = new Change<E>() {

				@Override
				public ChangeEvent getChanged() {
					return ChangeEvent.ADDED;
				}

			};
			change.added = Collections.unmodifiableList(added);
			return change;
		}

		public static <E> Change<E> listRemovedChange(List<E> removed) {
			Change<E> change = new Change<E>() {

				@Override
				public ChangeEvent getChanged() {
					return ChangeEvent.REMOVED;
				}

			};
			change.removed = Collections.unmodifiableList(removed);
			return change;
		}

		public static <E> Change<E> listUpdatedChange(Pair<E, E> updated) {
			Change<E> change = new Change<E>() {

				@Override
				public ChangeEvent getChanged() {
					return ChangeEvent.UPDATED;
				}

			};
			change.updated = updated;
			return change;
		}

		public List<E> added() {
			if (added == null) {
				return added = Collections.emptyList();
			}
			return added;
		}

		public List<E> removed() {
			if (removed == null) {
				return removed = Collections.emptyList();
			}
			return removed;
		}

		public Pair<E, E> updated() {
			return updated;
		}

		public abstract ChangeEvent getChanged();

		public static enum ChangeEvent {
			ADDED, REMOVED, UPDATED
		}
	}
}
