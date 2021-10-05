package com.raos.fx.controls.models.occurance;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

public final class MonthlyOccurance extends Occurance {
	private LocalDate startDay, endDay;
	private Collection<LocalDate> excludes;
	private Collection<Integer> days;

	private MonthlyOccurance() {
	}

	public static MonthlyOccurance monthlyOccurance(LocalTime startTime, LocalTime endTime, Collection<Integer> days) {
		return monthlyOccurance(startTime, endTime, days, LocalDate.now(), LocalDate.MAX);
	}

	public static MonthlyOccurance monthlyOccurance(LocalTime startTime, LocalTime endTime, Collection<Integer> days,
			LocalDate start, LocalDate end) {
		return monthlyOccurance(startTime, endTime, days, start, end, Collections.emptyList());
	}

	public static MonthlyOccurance monthlyOccurance(LocalTime startTime, LocalTime endTime, Collection<Integer> days,
			LocalDate start, LocalDate end, Collection<LocalDate> excludes) {
		MonthlyOccurance occurance = new MonthlyOccurance();
		occurance.setStartTime(startTime);
		occurance.setEndTime(endTime);
		occurance.setStartDay(start);
		occurance.setEndDay(end);
		occurance.setDays(days);
		occurance.setExcludes(excludes);
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

	public final Collection<LocalDate> getExcludes() {
		return excludes;
	}

	public final void setExcludes(Collection<LocalDate> excludes) {
		this.excludes = excludes;
	}

	public final Collection<Integer> getDays() {
		return days;
	}

	public final void setDays(Collection<Integer> days) {
		this.days = days;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(days, endDay, excludes, startDay);
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
		if (!(obj instanceof MonthlyOccurance)) {
			return false;
		}
		MonthlyOccurance other = (MonthlyOccurance) obj;
		return Objects.equals(days, other.days) && Objects.equals(endDay, other.endDay)
				&& Objects.equals(excludes, other.excludes) && Objects.equals(startDay, other.startDay);
	}

	@Override
	public Often getOften() {
		return Often.MONTHLY;
	}

	@Override
	public boolean isAvailable(LocalDate date) {
		return !excludes.contains(date) && !startDay.isAfter(date) && !endDay.isBefore(date)
				&& days.contains(date.getDayOfMonth());
	}
}
