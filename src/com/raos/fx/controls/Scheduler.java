package com.raos.fx.controls;

import com.raos.fx.controls.skin.SchedulerSkin;
import com.raos.fx.controls.events.TaskSelectedEvent;
import com.raos.fx.controls.models.Task;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javafx.beans.property.ReadOnlyListProperty;
import javafx.beans.property.ReadOnlyListWrapper;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import javafx.util.Callback;
import javafx.util.Pair;

/**
 * A Scheduler that will recieve tasks from a source based on the date
 * 
 * @author Raos
 */
public class Scheduler extends Control {
	/**
	 * The default factory callback to modify tasks
	 */
	public static final Callback<Change<Task>, Boolean> DEFAULT_TASK_MOD_FACTORY = MemoryContainer::taskModFactory;
	/**
	 * The default factory callback to retrieve the tasks
	 */
	public static final Callback<LocalDate, ReadOnlyListProperty<Task>> DEFAULT_TASK_FACTORY = MemoryContainer::taskFactory;

	private static final List<Scheduler> SCHEDULERS = FXCollections.observableArrayList();

	/**
	 * The current local date that is used for the Scheduler
	 */
	public final ObjectProperty<LocalDate> currentLocalDate() {
		return currentLocalDate;
	}

	public final LocalDate getCurrentLocalDate() {
		return currentLocalDate().get();
	}

	public final void setCurrentLocalDate(LocalDate currentLocalDate) {
		this.currentLocalDate().set(currentLocalDate);
	}

	private final ObjectProperty<LocalDate> currentLocalDate = new SimpleObjectProperty<>(this, "currentLocalDate");

	/**
	 * The current factory callback to retrieve the tasks
	 */
	public static ObjectProperty<Callback<LocalDate, ReadOnlyListProperty<Task>>> taskFactoryProperty() {
		return taskFactory;
	}

	public static Callback<LocalDate, ReadOnlyListProperty<Task>> getTaskFactory() {
		return taskFactory.get();
	}

	public static void setTaskFactory(Callback<LocalDate, ReadOnlyListProperty<Task>> value) {
		taskFactory.set(value);
	}

	private final static ObjectProperty<Callback<LocalDate, ReadOnlyListProperty<Task>>> taskFactory = new SimpleObjectProperty<>(
			Scheduler.class, "taskFactory", DEFAULT_TASK_FACTORY);

	/**
	 * The current factory callback to modify tasks
	 */
	public static ObjectProperty<Callback<Change<Task>, Boolean>> taskModFactoryProperty() {
		return taskModFactory;
	}

	public static Callback<Change<Task>, Boolean> getTaskModFactory() {
		return taskModFactory.get();
	}

	public static void setTaskModFactory(Callback<Change<Task>, Boolean> value) {
		// proxy callback
		taskModFactory.set(e -> {
			try {
				// call value then execute change
				return value.call(e);
			} finally {
				// execute ui change
				Scheduler.SCHEDULERS.forEach(e0 -> {
					if (e0.getSkin() != null)
						// refresh skin
						((Invalidateable) e0.getSkin()).invalidate(e);
					else {
						// add listener to skin property to invalidate on skin change
						e0.skinProperty().addListener(new InvalidationListener() {
							@Override
							public void invalidated(Observable o) {
								((Invalidateable) e0.getSkin()).invalidate(e);
								e0.skinProperty().removeListener(this);
							}

						});
					}

				});
			}
		});
	}

	private final static ObjectProperty<Callback<Change<Task>, Boolean>> taskModFactory = new SimpleObjectProperty<>(
			Scheduler.class, "taskModFactory", DEFAULT_TASK_MOD_FACTORY);

	/**
	 * The constructor for the Scheduler object
	 */
	public Scheduler() {
		SCHEDULERS.add(this);
	}

	/**
	 * Creates the default skin as {@link SchedulerSkin}
	 */
	@Override
	protected Skin<?> createDefaultSkin() {
		skinProperty().addListener((obs, oldv, newv) -> {
			if (newv instanceof Invalidateable)
				return;
			throw new RuntimeException("The Skin of the scheduler must be Invalidateable");
		});
		return new SchedulerSkin(this);
	}

	/**
	 * The event handler for the selection of the tasks
	 */
	public final ObjectProperty<EventHandler<TaskSelectedEvent>> onTaskSelectedProperty() {
		return this.onTaskSelected;
	}

	public final EventHandler<TaskSelectedEvent> getOnTaskSelected() {
		return this.onTaskSelectedProperty().get();
	}

	public final void setOnTaskSelected(final EventHandler<TaskSelectedEvent> onTaskSelected) {
		this.onTaskSelectedProperty().set(e -> {
			onTaskSelected.handle(e);
		});
	}

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

	private static class MemoryContainer {

		// Total amount tasks
		private static final ObservableList<Task> tasks = FXCollections.observableArrayList();
		private static int index = 0;

		static {
			tasks.addListener((ListChangeListener<Task>) c -> {
				if (c.wasAdded()) {
					for (Task task : c.getAddedSubList()) {
						task.persist(index);
						index++;
					}
				}
			});
		}

		// The default task factory
		private static ReadOnlyListProperty<Task> taskFactory(LocalDate date) {
			// Readonly wrapper of the tasks
			return new ReadOnlyListWrapper<Task>(MemoryContainer.class, "tasks",
					// filtering the tasks to the date
					tasks.stream().filter(e -> e.getOccurance().isAvailable(date))
							// collecting the stream to an observable array list from FXCollections
							.collect(FXCollections::observableArrayList, Collection::add, Collection::addAll))
									// getting the readonly property from the wrapper
									.getReadOnlyProperty();
		}

		// The default task modification factory
		private static boolean taskModFactory(Change<Task> change) {
			// switch case of the change event

			switch (change.getChanged()) {
			// adds the added list to the total tasks
			case ADDED:
				return tasks.addAll(change.added());
			// removes the removed list from the change event from the total tasks
			case REMOVED:
				return tasks.removeAll(change.removed());
			// updates the old value to the new value
			case UPDATED:
				boolean b = true;
				try {
					change.updated().getValue().persist(change.updated().getKey().getId());
					tasks.set(tasks.indexOf(change.updated().getKey()), change.updated().getValue());
				} catch (Exception e) {
					b = false;
				}
				return b;
			}
			return false;
		}
	}

	/**
	 * The change class that can hold changed items for the scheduler
	 * 
	 * @author Raos
	 *
	 * @param <E> - Changed class
	 */
	public static abstract class Change<E> {

		private List<E> added;
		private List<E> removed;
		private Pair<E, E> updated;

		/**
		 * 
		 * @param <E>   - Changed class
		 * @param added - List of Elements to be added to Scheduler tasks
		 * @return the change object
		 */
		public static <E> Change<E> listAddedChange(List<E> added) {
			// Anonymously extend the Change class
			Change<E> change = new Change<E>() {

				@Override
				public ChangeEvent getChanged() {
					return ChangeEvent.ADDED;
				}

			};
			// set the added as an unmodifiable list
			change.added = Collections.unmodifiableList(added);
			return change;
		}

		public static <E> Change<E> listRemovedChange(List<E> removed) {
			// Anonymously extend the Change class
			Change<E> change = new Change<E>() {

				@Override
				public ChangeEvent getChanged() {
					return ChangeEvent.REMOVED;
				}

			};
			// set the removed as an unmodifiable list
			change.removed = Collections.unmodifiableList(removed);
			return change;
		}

		public static <E> Change<E> listUpdatedChange(Pair<E, E> updated) {
			// Anonymously extend the Change class
			Change<E> change = new Change<E>() {

				@Override
				public ChangeEvent getChanged() {
					return ChangeEvent.UPDATED;
				}

			};
			// set the updated
			change.updated = updated;
			return change;
		}

		/**
		 * @return the list of all the added tasks
		 */
		public List<E> added() {
			if (added == null) {
				return added = Collections.emptyList();
			}
			return added;
		}

		/**
		 * @return the list of all the remove tasks
		 */
		public List<E> removed() {
			if (removed == null) {
				return removed = Collections.emptyList();
			}
			return removed;
		}

		/**
		 * @return updated tasks
		 */
		public Pair<E, E> updated() {
			return updated;
		}

		/**
		 * @return The Change Event: Updated, Added, or Removed
		 */
		public abstract ChangeEvent getChanged();

		/**
		 * The Change event
		 * 
		 * @author Raos
		 *
		 */
		public static enum ChangeEvent {
			ADDED, REMOVED, UPDATED
		}
	}
}
