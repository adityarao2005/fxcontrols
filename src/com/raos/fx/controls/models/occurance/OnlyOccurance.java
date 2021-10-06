package com.raos.fx.controls.models.occurance;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Map;
import java.util.Objects;

public final class OnlyOccurance extends Occurance {
	private LocalDate date;
	
	private OnlyOccurance() {
	}

	public static OnlyOccurance onlyOccurance(LocalTime startTime, LocalTime endTime) {
		return onlyOccurance(startTime, endTime, LocalDate.now());
	}

	public static OnlyOccurance onlyOccurance(LocalTime startTime, LocalTime endTime, LocalDate date) {
		OnlyOccurance occurance = new OnlyOccurance();
		occurance.setDate(date);
		occurance.setEndTime(endTime);
		occurance.setStartTime(startTime);
		return occurance;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	@Override
	public Often getOften() {
		return Often.ONCE;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(date);
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
		if (!(obj instanceof OnlyOccurance)) {
			return false;
		}
		OnlyOccurance other = (OnlyOccurance) obj;
		return Objects.equals(date, other.date);
	}

	@Override
	public boolean isAvailable(LocalDate date) {
		return this.date.equals(date);
	}

	@Override
	public Map<String, Object> transform(Map<String, Object> t) {
		Map<String, Object> map = super.transform(t);
		map.put("Date", date);
		return map;
	}
}
