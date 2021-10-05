package com.raos.fx.controls.models.occurance;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Objects;

public final class WeeklyOccurance extends Occurance {
	private LocalDate startDay, endDay;
	private EnumSet<DayOfWeek> daysOfWeek;
	private Collection<LocalDate> excludes;

	private WeeklyOccurance() {
	}

	public static WeeklyOccurance weeklyOccurance(LocalTime startTime, LocalTime endTime,
			EnumSet<DayOfWeek> daysOfWeek) {
		return weeklyOccurance(startTime, endTime, daysOfWeek, LocalDate.now(), LocalDate.MAX);
	}

	public static WeeklyOccurance weeklyOccurance(LocalTime startTime, LocalTime endTime, EnumSet<DayOfWeek> daysOfWeek,
			LocalDate start, LocalDate end) {
		return weeklyOccurance(startTime, endTime, daysOfWeek, start, end, Collections.emptyList());
	}

	public static WeeklyOccurance weeklyOccurance(LocalTime startTime, LocalTime endTime, EnumSet<DayOfWeek> daysOfWeek,
			LocalDate start, LocalDate end, Collection<LocalDate> excludes) {
		WeeklyOccurance occurance = new WeeklyOccurance();
		occurance.setStartTime(startTime);
		occurance.setEndTime(endTime);
		occurance.setStartDay(start);
		occurance.setEndDay(end);
		occurance.setExcludes(excludes);
		occurance.setDaysOfWeek(daysOfWeek);
		return occurance;
	}

	public final LocalDate getStartDay() {
		return startDay;
	}

	public final void setStartDay(LocalDate startDay) {
		this.startDay = startDay;
	}

	public final LocalDate getEndDay() {
		return endDay;
	}

	public final void setEndDay(LocalDate endDay) {
		this.endDay = endDay;
	}

	public final EnumSet<DayOfWeek> getDaysOfWeek() {
		return daysOfWeek;
	}

	public final void setDaysOfWeek(EnumSet<DayOfWeek> daysOfWeek) {
		this.daysOfWeek = daysOfWeek;
	}

	public final Collection<LocalDate> getExcludes() {
		return excludes;
	}

	public final void setExcludes(Collection<LocalDate> excludes) {
		this.excludes = excludes;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(daysOfWeek, endDay, excludes, startDay);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
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
}
