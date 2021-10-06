package com.raos.fx.controls.models.occurance;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.MonthDay;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;

public final class YearlyOccurance extends Occurance {
	private LocalDate startDay, endDay;
	private Collection<LocalDate> excludes;
	private Collection<MonthDay> days;

	private YearlyOccurance() {
	}

	public static YearlyOccurance yearlyOccurance(LocalTime startTime, LocalTime endTime, Collection<MonthDay> days) {
		return yearlyOccurance(startTime, endTime, days, LocalDate.now(), LocalDate.MAX);
	}

	public static YearlyOccurance yearlyOccurance(LocalTime startTime, LocalTime endTime, Collection<MonthDay> days,
			LocalDate start, LocalDate end) {
		return yearlyOccurance(startTime, endTime, days, start, end, Collections.emptyList());
	}

	public static YearlyOccurance yearlyOccurance(LocalTime startTime, LocalTime endTime, Collection<MonthDay> days,
			LocalDate start, LocalDate end, Collection<LocalDate> exclude) {
		YearlyOccurance occurance = new YearlyOccurance();
		occurance.setDays(days);
		occurance.setEndDay(end);
		occurance.setEndTime(endTime);
		occurance.setExcludes(exclude);
		occurance.setStartDay(start);
		occurance.setStartTime(startTime);
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

	public final Collection<MonthDay> getDays() {
		return days;
	}

	public final void setDays(Collection<MonthDay> days) {
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
		if (!(obj instanceof YearlyOccurance)) {
			return false;
		}
		YearlyOccurance other = (YearlyOccurance) obj;
		return Objects.equals(days, other.days) && Objects.equals(endDay, other.endDay)
				&& Objects.equals(excludes, other.excludes) && Objects.equals(startDay, other.startDay);
	}

	@Override
	public Often getOften() {
		return Often.YEARLY;
	}

	@Override
	public boolean isAvailable(LocalDate date) {
		return !excludes.contains(date) && !startDay.isAfter(date) && !endDay.isBefore(date)
				&& days.contains(MonthDay.of(date.getMonth(), date.getDayOfMonth()));
	}
	
	@Override
	public Map<String, Object> transform(Map<String, Object> t) {
		Map<String, Object> map = super.transform(t);
		map.put("Start Date", startDay);
		map.put("End Date", endDay);
		map.put("Excludes", excludes);
		map.put("Days", days);
		return map;
	}
}
