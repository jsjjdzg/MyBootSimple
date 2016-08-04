package com.bocloud.emaas.common.model;

public enum Week {
	SUNDAY("Sunday"), // 星期日
	MONDAY("Monday"), // 星期一
	TUESDAY("Tuesday"), // 星期二
	WEDNESDAY("Wednesday"), // 星期三
	THURSDAY("Thursday"), // 星期四
	FRIDAY("Friday"), // 星期五
	SATURDAY("Saturday"); // 星期六
	
	private String week;
	
	private Week(String week) {
		this.week=week;
	}

	/**
	 * @return the week
	 */
	public String getWeek() {
		return week;
	}

	/**
	 * @param week the week to set
	 */
	public void setWeek(String week) {
		this.week = week;
	}
}
