/**
 * 
 */
package com.sysc3303.project.utils;

import java.io.Serializable;

/**
 * Represents the time that a request was made at in the format hh:mm:ss.mmm
 * 
 * @author Group 9
 *
 */
public class Time implements Serializable {
	private final String hours;
	private final String minutes;
	private final String seconds;
	private final String milliseconds;

	/**
	 * Constructor
	 * 
	 * @param hours        number of hours (24 hour format)
	 * @param minutes      number of minutes
	 * @param seconds      number of seconds
	 * @param milliseconds number of milliseconds
	 */
	public Time(String hours, String minutes, String seconds, String milliseconds) {
		if (!isTimeValid(hours, minutes, seconds, milliseconds)) {
			throw new IllegalArgumentException("Cannot create a Time object with these values, it is invalid");
		}

		this.hours = hours;
		this.minutes = minutes;
		this.seconds = seconds;
		this.milliseconds = milliseconds;
	}

	/**
	 * Gets whether this Time object occurs before another Time object
	 * 
	 * @param otherTime the other Time object that is being compared to
	 * @return true if this Time object occurs before the other Time object, and
	 *         false otherwise
	 */
	public boolean isTimeBefore(Time otherTime) {
		int intHours = Integer.parseInt(hours);
		int intMins = Integer.parseInt(minutes);
		int intSecs = Integer.parseInt(seconds);
		int intMiliSecs = Integer.parseInt(milliseconds);
		int intOtherHours = Integer.parseInt(otherTime.hours);
		int intOtherMins = Integer.parseInt(otherTime.minutes);
		int intOtherSecs = Integer.parseInt(otherTime.seconds);
		int intOtherMiliSecs = Integer.parseInt(otherTime.milliseconds);

		if (intHours != intOtherHours)
			return intHours < intOtherHours;
		else if (intMins != intOtherMins)
			return intMins < intOtherMins;
		else if (intSecs != intOtherSecs)
			return intSecs < intOtherSecs;
		return intMiliSecs < intOtherMiliSecs;
	}
	
	/**
	 * 
	 * @param otherTime
	 * @return time in milliseconds between time and otherTime
	 */
	public Integer getTimeDifferenceInMS(Time otherTime) {
		final int HOUR_TO_MS = 3600000;
		final int MIN_TO_MS = 60000; 
		final int SEC_TO_MS = 1000; 
		
		int intHours = Integer.parseInt(hours);
		int intMins = Integer.parseInt(minutes);
		int intSecs = Integer.parseInt(seconds);
		int intMiliSecs = Integer.parseInt(milliseconds);
		int intOtherHours = Integer.parseInt(otherTime.hours);
		int intOtherMins = Integer.parseInt(otherTime.minutes);
		int intOtherSecs = Integer.parseInt(otherTime.seconds);
		int intOtherMiliSecs = Integer.parseInt(otherTime.milliseconds);
		
		int resHours = (intOtherHours - intHours) * HOUR_TO_MS; 
		int resMins = (intOtherMins - intMins) * MIN_TO_MS; 
		int resSecs = (intOtherSecs - intSecs) * SEC_TO_MS; 
		int resMiliSecs = intOtherMiliSecs - intMiliSecs;
		
		return resHours + resMins + resSecs + resMiliSecs; 
	} 
	
	/**
	 * String representation of this object
	 */
	@Override
	public String toString() {
		return String.format("%s:%s:%s.%s", hours, minutes, seconds, milliseconds);
	}

	/**
	 * Helper method to check whether the Time object is valid
	 * 
	 * @param hours        number of hours (24 hour format)
	 * @param minutes      number of minutes
	 * @param seconds      number of seconds
	 * @param milliseconds number of seconds
	 */
	private boolean isTimeValid(String hours, String minutes, String seconds, String milliseconds) {
		int intHours = Integer.parseInt(hours);
		int intMins = Integer.parseInt(minutes);
		int intSecs = Integer.parseInt(seconds);
		int intMiliSecs = Integer.parseInt(milliseconds);

		return (intHours >= 0 && intHours < 24) && (intMins >= 0 && intMins < 60) && (intSecs >= 0 && intSecs < 60)
				&& (intMiliSecs >= 0 && intMiliSecs < 1000);
	}

	/**
	 * Creates a Time object from a time String
	 * 
	 * @param timeString a String in the format hh:mm:ss.mmm
	 * @return the corresponding Time object
	 */
	public static Time createFromTimeString(String timeString) {
		try {
			String[] time = timeString.split(":");
			String hours = time[0];
			String minutes = time[1];
			String[] secondsList = time[2].split("\\.");
			String seconds = secondsList[0];
			String milliseconds = secondsList[1];
			return new Time(hours, minutes, seconds, milliseconds);
		} catch (Exception e) {
			throw new IllegalArgumentException("Could not parse time string");
		}

	}
	
	/**
	 * Getter for the hours attribute
	 * @return hours
	 */
	public String getHours() {
		return this.hours;
	}
	
	/**
	 * Getter for the minutes attribute
	 * @return minutes
	 */
	public String getMinutes() {
		return this.minutes;
	}
	
	/**
	 * Getter for the seconds attribute
	 * @return seconds
	 */
	public String getSeconds() {
		return this.seconds;
	}
	
	/**
	 * Getter for the milliseconds attribute
	 * @return milliseconds
	 */
	public String getMilliseconds() {
		return this.hours;
	}
	
}
