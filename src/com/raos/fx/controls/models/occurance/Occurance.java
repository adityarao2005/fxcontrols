package com.raos.fx.controls.models.occurance;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.FormatStyle;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import com.raos.fx.controls.models.Transformable;

import javafx.util.converter.LocalTimeStringConverter;

public abstract class Occurance implements Transformable<Map<String, Object>> {
	public static final LocalTime MIN = LocalTime.MIN, MAX = LocalTime.MAX.truncatedTo(ChronoUnit.MINUTES);
	private static final LocalTimeStringConverter converter = new LocalTimeStringConverter(FormatStyle.SHORT,
			Locale.CANADA);
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
	
	@Override
	public Map<String, Object> transform(Map<String, Object> t) {
		Map<String, Object> map = Optional.ofNullable(t).orElseGet(HashMap::new);
		map.put("Name", "Occurance");
		map.put("Start Time", converter.toString(startTime));
		map.put("End Time", converter.toString(endTime));
		map.put("Reminder Time", converter.toString(reminderTime));
		return map;
	}
}


