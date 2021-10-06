package com.raos.fx.controls.models.occurance;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;

public final class DailyOccurance extends Occurance {
	private LocalDate startDay, endDay;
	private Collection<LocalDate> excludes;

	private DailyOccurance() {
	}

	public static DailyOccurance dailyOccurance(LocalTime startTime, LocalTime endTime) {
		return dailyOccurance(startTime, endTime, LocalDate.now(), LocalDate.MAX);
	}

	public static DailyOccurance dailyOccurance(LocalTime startTime, LocalTime endTime, LocalDate startDay,
			LocalDate endDay) {
		return dailyOccurance(startTime, endTime, startDay, endDay, Collections.emptyList());
	}

	public static DailyOccurance dailyOccurance(LocalTime startTime, LocalTime endTime, LocalDate startDay,
			LocalDate endDay, Collection<LocalDate> excludes) {
		DailyOccurance occurance = new DailyOccurance();
		occurance.setStartTime(startTime);
		occurance.setEndTime(endTime);
		occurance.setStartDay(startDay);
		occurance.setEndDay(endDay);
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(endDay, excludes, startDay);
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
		if (!(obj instanceof DailyOccurance)) {
			return false;
		}
		DailyOccurance other = (DailyOccurance) obj;
		return Objects.equals(endDay, other.endDay) && Objects.equals(excludes, other.excludes)
				&& Objects.equals(startDay, other.startDay);
	}

	@Override
	public Often getOften() {
		return Often.DAILY;
	}

	@Override
	public boolean isAvailable(LocalDate date) {
		return !excludes.contains(date) && !startDay.isAfter(date) && !endDay.isBefore(date);
	}
	
	@Override
	public Map<String, Object> transform(Map<String, Object> t) {
		Map<String, Object> map = super.transform(t);
		map.put("Start Date", startDay);
		map.put("End Date", endDay);
		map.put("Excludes", excludes);
		return map;
	}
}
