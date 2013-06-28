/*
 * Copyright (c) 2013 Inkoniq
 * All Rights Reserved.
 * @since Feb 26, 2013 
 * @author achowdhury
 */
package com.inkoniq.calendar;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.http.impl.cookie.DateUtils;

/**
 * 
 * @author Pushpan
 * 
 */
public class CalDate implements Comparable<CalDate> {
	private Calendar cal;

	public CalDate(int day, int month, int year) {
		cal = Calendar.getInstance();
		cal.set(Calendar.DAY_OF_MONTH, day);
		cal.set(Calendar.MONTH, month);
		cal.set(Calendar.YEAR, year);
	}

	public CalDate(long milliseconds) {
		cal = Calendar.getInstance();
		cal.setTimeInMillis(milliseconds);
	}

	/**
	 * @param of
	 *            type null
	 * @return day of type int getter function for day
	 * @since Feb 26, 2013
	 * @author achowdhury
	 */
	public int getDay() {
		return cal.get(Calendar.DAY_OF_MONTH);
	}

	/**
	 * @param day
	 *            of type int
	 * @return of type null setter function for day
	 * @since Feb 26, 2013
	 * @author achowdhury
	 */
	public void setDay(int day) {
		cal.set(Calendar.DAY_OF_MONTH, day);
	}

	/**
	 * @param of
	 *            type null
	 * @return month of type int getter function for month
	 * @since Feb 26, 2013
	 * @author achowdhury
	 */
	public int getmonth() {
		return cal.get(Calendar.MONTH);
	}

	/**
	 * @param month
	 *            of type int
	 * @return of type null setter function for month
	 * @since Feb 26, 2013
	 * @author achowdhury
	 */
	public void setmonth(int month) {
		cal.set(Calendar.MONTH, month);
	}

	/**
	 * @param of
	 *            type null
	 * @return year of type int getter function for year
	 * @since Feb 26, 2013
	 * @author achowdhury
	 */
	public int getYear() {
		return cal.get(Calendar.YEAR);
	}

	/**
	 * @param year
	 *            of type int
	 * @return of type null setter function for year
	 * @since Feb 26, 2013
	 * @author achowdhury
	 */
	public void setYear(int year) {
		cal.set(Calendar.YEAR, year);
	}

	public CalDate nextDay() {
		Calendar local = new GregorianCalendar();
		local.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH));
		local.set(Calendar.MONTH, cal.get(Calendar.MONTH));
		local.set(Calendar.YEAR, cal.get(Calendar.YEAR));
		local.add(Calendar.DAY_OF_MONTH, 1);
		return new CalDate(local.get(Calendar.DAY_OF_MONTH),
				local.get(Calendar.MONTH), local.get(Calendar.YEAR));
	}

	public CalDate previousDay() {
		Calendar local = new GregorianCalendar();
		local.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH));
		local.set(Calendar.MONTH, cal.get(Calendar.MONTH));
		local.set(Calendar.YEAR, cal.get(Calendar.YEAR));
		local.add(Calendar.DAY_OF_MONTH, -1);
		return new CalDate(local.get(Calendar.DAY_OF_MONTH),
				local.get(Calendar.MONTH), local.get(Calendar.YEAR));
	}

	public CalDate nextMonth() {
		Calendar local = new GregorianCalendar();
		local.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH));
		local.set(Calendar.MONTH, cal.get(Calendar.MONTH));
		local.set(Calendar.YEAR, cal.get(Calendar.YEAR));
		local.add(Calendar.MONTH, 1);
		return new CalDate(local.get(Calendar.DAY_OF_MONTH),
				local.get(Calendar.MONTH), local.get(Calendar.YEAR));
	}

	public CalDate previousMonth() {
		Calendar local = new GregorianCalendar();
		local.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH));
		local.set(Calendar.MONTH, cal.get(Calendar.MONTH));
		local.set(Calendar.YEAR, cal.get(Calendar.YEAR));
		local.add(Calendar.MONTH, -1);
		return new CalDate(local.get(Calendar.DAY_OF_MONTH),
				local.get(Calendar.MONTH), local.get(Calendar.YEAR));
	}

	@Override
	public int compareTo(CalDate another) {
		Calendar local = new GregorianCalendar();
		local.set(Calendar.DAY_OF_MONTH, another.getDay());
		local.set(Calendar.MONTH, another.getmonth());
		local.set(Calendar.YEAR, another.getYear());
		if (cal.after(local)) {
			return 1;
		} else if (cal.before(local)) {
			return -1;
		} else {
			return 0;
		}
	}

	public boolean equals(CalDate another) {
		return compareTo(another) == 0;
	}

	public boolean isLessThan(CalDate another) {
		return compareTo(another) < 0;
	}

	public boolean isMoreThan(CalDate another) {
		return compareTo(another) > 0;
	}

	public boolean isSameWith(CalDate another) {
		return compareTo(another) == 0;
	}

	public CalDate clone() {
		return new CalDate(getDay(), getmonth(), getYear());
	}

	public long getTimeMillis() {
		return cal.getTimeInMillis();
	}

	public boolean isSameDay(CalDate another) {
		return (getDay() == another.getDay()
				&& getmonth() == another.getmonth() && getYear() == another
					.getYear());
	}

	public boolean hasSameMonth(CalDate another) {
		return (getmonth() == another.getmonth() && getYear() == another
				.getYear());
	}

	public String dateWithFormat(String pattern) {
		return DateUtils.formatDate(new Date(cal.getTimeInMillis()), pattern);
	}

	public int typeBetween(Calendar startDate, Calendar endDate, int type) {
		Calendar date = (Calendar) startDate.clone();
		int typesBetween = 0;
		while (date.before(endDate)) {
			date.add(type, 1);
			typesBetween++;
		}
		return typesBetween == 0 ? typesBetween : typesBetween - 1;
	}

	public int getDifferenceWith(CalDate laterDate, int type) {
		return typeBetween(this.cal, laterDate.cal, type);
	}

	public int getDifferenceInDaysWith(CalDate laterDate) {
		return typeBetween(this.cal, laterDate.cal, cal.DAY_OF_MONTH);
	}
}