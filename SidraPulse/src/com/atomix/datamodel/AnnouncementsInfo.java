package com.atomix.datamodel;

public class AnnouncementsInfo {
	private String title;
	private String type;
	private String dayAndDate;
	private String date;
	private String time;
	private String description;
	private int readStatus;
	private int id;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getReadStatus() {
		return readStatus;
	}

	public void setReadStatus(int readStatus) {
		this.readStatus = readStatus;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDayAndDate() {
		return dayAndDate;
	}

	public void setDayAndDate(String dayAndDate) {
		this.dayAndDate = dayAndDate;
	}

}
