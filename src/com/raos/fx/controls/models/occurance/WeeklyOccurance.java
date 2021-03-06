package com.raos.fx.controls.models.occurance;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Map;
import java.util.Objects;

import com.raos.fx.controls.models.Transformable;

/**
 * A class representing a weekly occurance of a task
 * 
 * @author Raos
 *
 */
public final class WeeklyOccurance extends Occurance {
	private LocalDate startDay, endDay;
	private EnumSet<DayOfWeek> daysOfWeek;
	private Collection<LocalDate> excludes;

	private WeeklyOccurance() {
	}

	/**
	 * 
	 * @param startTime  - the start time
	 * @param endTime    - the end time
	 * @param daysOfWeek - the days of the week
	 * @return an instance of WeeklyOccurance
	 */
	public static WeeklyOccurance weeklyOccurance(LocalTime startTime, LocalTime endTime,
			EnumSet<DayOfWeek> daysOfWeek) {
		return weeklyOccurance(startTime, endTime, daysOfWeek, LocalDate.now(), LocalDate.MAX);
	}

	/**
	 * 
	 * @param startTime  - the start time
	 * @param endTime    - the end time
	 * @param daysOfWeek - the days of the week
	 * @param start      - the start day
	 * @param end        - the end day
	 * @return an instance of WeeklyOccurance
	 */
	public static WeeklyOccurance weeklyOccurance(LocalTime startTime, LocalTime endTime, EnumSet<DayOfWeek> daysOfWeek,
			LocalDate start, LocalDate end) {
		return weeklyOccurance(startTime, endTime, daysOfWeek, start, end, Collections.emptyList(), LocalTime.MIN);
	}

	/**
	 * 
	 * @param startTime    - the start time
	 * @param endTime      - the end time
	 * @param daysOfWeek   - the days of the week
	 * @param start        - the start day
	 * @param end          - the end day
	 * @param excludes     - the excluded days
	 * @param reminderTime - the reminder time
	 * @return an instance of WeeklyOccurance
	 */
	public static WeeklyOccurance weeklyOccurance(LocalTime startTime, LocalTime endTime, EnumSet<DayOfWeek> daysOfWeek,
			LocalDate start, LocalDate end, Collection<LocalDate> excludes, LocalTime reminderTime) {
		WeeklyOccurance occurance = new WeeklyOccurance();
		occurance.setStartTime(startTime);
		occurance.setEndTime(endTime);
		occurance.setStartDay(start);
		occurance.setEndDay(end);
		occurance.setExcludes(excludes);
		occurance.setDaysOfWeek(daysOfWeek);
		occurance.setReminderTime(reminderTime);
		return occurance;
	}

	/**
	 * @return startDay
	 */
	public final LocalDate getStartDay() {
		return startDay;
	}

	/**
	 * @param startDay - to be set
	 */
	public final void setStartDay(LocalDate startDay) {
		this.startDay = startDay;
	}

	/**
	 * @return endDay
	 */
	public final LocalDate getEndDay() {
		return endDay;
	}

	/**
	 * 
	 * @param endDay - to be set
	 */
	public final void setEndDay(LocalDate endDay) {
		this.endDay = endDay;
	}

	/**
	 * 
	 * @return daysOfWeek
	 */
	public final EnumSet<DayOfWeek> getDaysOfWeek() {
		return daysOfWeek;
	}

	/**
	 * 
	 * @param daysOfWeek - to be set
	 */
	public final void setDaysOfWeek(EnumSet<DayOfWeek> daysOfWeek) {
		this.daysOfWeek = daysOfWeek;
	}

	/**
	 * @return excludes
	 */
	public final Collection<LocalDate> getExcludes() {
		return excludes;
	}

	/**
	 * 
	 * @param excludes - to be set
	 */
	public final void setExcludes(Collection<LocalDate> excludes) {
		this.excludes = excludes;
	}

	@Override
	public int hashCode() {
		// auto generated by eclipse
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(daysOfWeek, endDay, excludes, startDay);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		// auto generated by eclipse
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		if (!(obj instanceof WeeklyOccurance)) {
			return false;
		}
		WeeklyOccurance other = (WeeklyOccurance) obj;
		return Objects.equals(daysOfWeek, other.daysOfWeek) && Objects.equals(endDay, other.endDay)
				&& Objects.equals(excludes, other.excludes) && Objects.equals(startDay, other.startDay);
	}

	@Override
	public Often getOften() {
		return Often.WEEKLY;
	}

	@Override
	public boolean isAvailable(LocalDate date) {
		return !excludes.contains(date) && !startDay.isAfter(date) && !endDay.isBefore(date)
				&& daysOfWeek.contains(date.getDayOfWeek());
	}

	@Override
	public Map<String, Object> transformTo(Map<String, Object> t) {
		Map<String, Object> map = super.transformTo(t);
		map.put("Start Date", startDay);
		map.put("End Date", endDay);
		map.put("Excludes", excludes);
		map.put("Days of the Week", daysOfWeek);
		return map;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Transformable<Map<String, Object>> transformFrom(Map<String, Object> t) {
		startDay = (LocalDate) t.get("Start Date");
		endDay = (LocalDate) t.get("End Date");
		excludes = (Collection<LocalDate>) t.get("Excludes");
		daysOfWeek = (EnumSet<DayOfWeek>) t.get("Days of the Week");
		return super.transformFrom(t);
	}
}
