package com.raos.fx.controls.models.occurance;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

public abstract class Occurance {
	public static final LocalTime MIN = LocalTime.MIN, MAX = LocalTime.MAX.truncatedTo(ChronoUnit.MINUTES);
	private LocalTime startTime, endTime;
	private LocalTime reminderTime;
	
	public final LocalTime getStartTime() {
		return startTime;
	}

	public final void setStartTime(LocalTime startTime) {
		this.startTime = startTime;
	}

	public final LocalTime getEndTime() {
		return endTime;
	}

	public final void setEndTime(LocalTime endTime) {
		this.endTime = endTime;
	}

	public abstract Often getOften();

	public static enum Often {
		ONCE, DAILY, WEEKLY, MONTHLY, YEARLY	
	}
	
	public final Duration getTimeDuration() {
		return Duration.between(startTime, endTime);
	}

	@Override
	public int hashCode() {
		return Objects.hash(endTime, startTime);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Occurance)) {
			return false;
		}
		Occurance other = (Occurance) obj;
		return Objects.equals(endTime, other.endTime) && Objects.equals(startTime, other.startTime);
	}
	
	public final boolean intersectsWith(Occurance other) {
		if (other == null) {
			throw new NullPointerException("other cannot be null");
		}
		if (this.equals(other) || this == other) {
			return false;
		}
		return (this.startTime.isBefore(other.endTime)) && (other.startTime.isAfter(this.endTime));
	}

	public final boolean isFullDay() {
		return startTime.equals(MIN) && endTime.equals(MAX);
	}

	public final LocalTime getReminderTime() {
		return reminderTime;
	}

	public final void setReminderTime(LocalTime reminderTime) {
		this.reminderTime = reminderTime;
	}

	public abstract boolean isAvailable(LocalDate date);
}


