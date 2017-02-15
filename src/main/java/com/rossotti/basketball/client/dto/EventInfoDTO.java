package com.rossotti.basketball.client.dto;

import org.joda.time.DateTime;

public class EventInfoDTO {
	private DateTime start_date_time;
	private String status;
	private String season_type;
	private int capacity;
	private int attendance;
	private String duration;

	public DateTime getStart_date_time() {
		return start_date_time;
	}
	public void setStart_date_time(DateTime start_date_time) {
		this.start_date_time = start_date_time;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getSeason_type() {
		return season_type;
	}
	public void setSeason_type(String season_type) {
		this.season_type = season_type;
	}
	public int getCapacity() {
		return capacity;
	}
	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}
	public int getAttendance() {
		return attendance;
	}
	public void setAttendance(int attendance) {
		this.attendance = attendance;
	}
	public String getDuration() {
		return duration;
	}
	public void setDuration(String duration) {
		this.duration = duration;
	}
}