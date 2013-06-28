/*
 * Copyright (c) 2013 Inkoniq
 * All Rights Reserved.
 * @since 24-Feb-2013 
 * @author Anuradha
 */
package com.inkoniq.calendar.views;

import java.util.Calendar;
import java.util.GregorianCalendar;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.inkoniq.calendar.CalDate;
import com.inkoniq.calendar.R;
import com.inkoniq.calendar.utils.DataUtils;

/**
 * 
 * @author Pushpan
 * 
 */
public class CalendarView extends LinearLayout implements
		CalenderCellView.CellClickedListener {
	private LinearLayout dayLayout;
	private LinearLayout dayContentLayout;
	String[] DAYS = { "S", "M", "T", "W", "T", "F", "S" };
	String[] MONTHS = { "January", "February", "March", "April", "May", "June",
			"July", "August", "September", "October", "November", "December" };
	// private GestureDetector gDetector;
	private TextView textCalHeader;
	private Calendar cal;
	private int currentDay;
	private int currentMonth;
	private int currentYear;
	// private int day;
	// private int month;
	// private int year;
	private CalDate calDate;
	// private int selectedDate;
	// private int selectedMonth;
	// private int selectedYear;
	int startDay;
	private CalendarSelectedListener calendarSelectedListener;
	private CalDate startDate;
	private CalDate endDate;
	private CalDate validStartDate;
	private CalDate validEndDate;
	private boolean settingEndDate;

	/**
	 * @param context
	 * @return of type CalenderView Constructor function
	 * @since 24-Feb-2013
	 * @author Anuradha
	 */
	public CalendarView(Context context) {
		super(context);
		initialize();
	}

	/**
	 * @param context
	 * @param attrs
	 * @return of type CalenderView Constructor function
	 * @since 24-Feb-2013
	 * @author Anuradha
	 */
	public CalendarView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initialize();
	}

	/**
	 * of type
	 * 
	 * @since 24-Feb-2013
	 * @author Anuradha
	 */
	private void initialize() {
		inflate(getContext(), R.layout.calendar_view, this);
		dayLayout = (LinearLayout) findViewById(R.id.dayLayout);
		dayContentLayout = (LinearLayout) findViewById(R.id.dayContentLayout);
		textCalHeader = (TextView) findViewById(R.id.textCalHeader);
		initializeCalenderUI();
		// gDetector = new GestureDetector(this);
	}

	/**
	 * of type
	 * 
	 * @since 24-Feb-2013
	 * @author Anuradha
	 */
	public void initializeCalenderUI() {
		cal = new GregorianCalendar();
		LinearLayout rowLayout = null;
		setDayNames();
		for (int i = 0; i < 6; i++) {
			rowLayout = new LinearLayout(getContext());
			for (int j = 0; j < 7; j++) {
				CalenderCellView calenderCellView = new CalenderCellView(
						getContext());
				calenderCellView.setCellClickedListener(this);
				rowLayout.addView(calenderCellView);
				calenderCellView.setState(CalenderCellView.STATE_UNAVAILABLE);
			}
			dayContentLayout.addView(rowLayout);
			rowLayout.setVisibility(VISIBLE);

		}
	}

	public int getWeeksOfMonth(int day, int month, int year) {
		cal.set(Calendar.DATE, day);
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, month);
		String week;
		week = String.format("%d", cal.get(Calendar.WEEK_OF_YEAR));
		return Integer.parseInt(week);
	}

	public void setCalendarDate1(CalDate caldate) {
		this.calDate = caldate;
		setTag(calDate);
		int month = caldate.getmonth();
		int year = caldate.getYear();
		int day = calDate.getDay();
		cal.set(Calendar.DATE, 1);
		cal.set(Calendar.MONTH, month);
		cal.set(Calendar.YEAR, year);
		CalDate today = new CalDate(System.currentTimeMillis());
		boolean hasStartDate = checkIfCurrentDateBelongsToStartDate(new CalDate(
				day, month, year));
		boolean hasEndDate = checkIfCurrentDateBelongsToEndDate(new CalDate(
				day, month, year));
		Log.e("TEST", "Start=" + hasStartDate + " End=" + hasEndDate);
		// cal.set(Calendar.DAY_OF_MONTH, 1);
		startDay = (cal.get(Calendar.DAY_OF_WEEK) - 1);
		Log.e("startDay=", "" + startDay);
		textCalHeader.setText(MONTHS[month] + " " + year);
		int dayCount = 1;
		int totalDaysInMonth = cal.getActualMaximum(Calendar.DATE);

		LinearLayout rowLayout = null;
		boolean hasADate = false;
		CalDate currentDate;
		for (int i = 0; i < 6; i++) {
			rowLayout = (LinearLayout) dayContentLayout.getChildAt(i);
			hasADate = false;
			for (int j = 0; j < 7; j++) {
				CalenderCellView calenderCellView = (CalenderCellView) rowLayout
						.getChildAt(j);
				calenderCellView.setCellClickedListener(this);
				currentDate = new CalDate(dayCount, month, year);
				if ((i == 0 && j < startDay) || (dayCount > totalDaysInMonth)) {
					// have no dates
					calenderCellView
							.setState(CalenderCellView.STATE_UNAVAILABLE);
					calenderCellView.setText("");
				} else {
					// have dates
					calenderCellView.setText("" + dayCount);
					hasADate = true;
					// check for available dates
					if (month == currentMonth && year == currentYear
							&& dayCount < currentDay) {

						calenderCellView
								.setState(CalenderCellView.STATE_UNAVAILABLE);
					} else {
						// available dates
						calenderCellView
								.setState(CalenderCellView.STATE_AVAILABLE);
					}

					// Check for selected days
					if (getStartDate() != null && getEndDate() != null) {
						if (checkIfEntireMouthIsInBetweenStartAndEnd(new CalDate(
								day, month, year))) {
							calenderCellView
									.setState(CalenderCellView.STATE_SELECTED);
						} else {
							if (hasStartDate && hasEndDate) {
								if ((dayCount < getStartDate().getDay())) {
									calenderCellView
											.setState(CalenderCellView.STATE_UNAVAILABLE);
								}

								if ((dayCount >= getStartDate().getDay())
										&& (dayCount <= getEndDate().getDay())) {
									calenderCellView
											.setState(CalenderCellView.STATE_SELECTED);
								}
							} else if (hasStartDate) {
								if ((dayCount < getStartDate().getDay())) {
									calenderCellView
											.setState(CalenderCellView.STATE_UNAVAILABLE);
								}

								if ((dayCount >= getStartDate().getDay())) {
									calenderCellView
											.setState(CalenderCellView.STATE_SELECTED);
								}
							} else if (hasEndDate) {
								if (dayCount <= getEndDate().getDay()) {
									calenderCellView
											.setState(CalenderCellView.STATE_SELECTED);
								}
							}
						}
					} else {
						if (getStartDate() != null) {
							if (getStartDate().getDay() == dayCount
									&& getStartDate().getmonth() == month
									&& getStartDate().getYear() == year) {
								calenderCellView
										.setState(CalenderCellView.STATE_SELECTED);
							}
						} else if (getEndDate() != null) {
							if (getEndDate().getDay() == dayCount
									&& getEndDate().getmonth() == month
									&& getEndDate().getYear() == year) {
								calenderCellView
										.setState(CalenderCellView.STATE_SELECTED);
							}
						}
					}
					if (!isValidDate(currentDate)) {
						calenderCellView
								.setState(CalenderCellView.STATE_UNAVAILABLE);
					}
					if (calenderCellView.getState() != CalenderCellView.STATE_SELECTED
							&& today.isSameDay(currentDate)) {
						calenderCellView.markToday();
					}
					dayCount++;
				}

			}
			rowLayout.setVisibility(hasADate ? VISIBLE : GONE);
		}
	}

	public void setCalendarDate(CalDate caldate) {
		this.calDate = caldate;
		setTag(calDate);
		int month = caldate.getmonth();
		int year = caldate.getYear();
		// int day = calDate.getDay();
		cal.set(Calendar.DATE, 1);
		cal.set(Calendar.MONTH, month);
		cal.set(Calendar.YEAR, year);

		CalDate today = new CalDate(System.currentTimeMillis());

		// cal.set(Calendar.DAY_OF_MONTH, 1);
		startDay = (cal.get(Calendar.DAY_OF_WEEK) - 1);
		textCalHeader.setText(MONTHS[month] + " " + year);
		int dayCount = 1;
		int totalDaysInMonth = cal.getActualMaximum(Calendar.DATE);

		LinearLayout rowLayout = null;
		boolean hasADate = false;
		CalDate currentDate;
		for (int i = 0; i < 6; i++) {
			rowLayout = (LinearLayout) dayContentLayout.getChildAt(i);
			hasADate = false;
			for (int j = 0; j < 7; j++) {
				CalenderCellView calenderCellView = (CalenderCellView) rowLayout
						.getChildAt(j);
				calenderCellView.setCellClickedListener(this);
				currentDate = new CalDate(dayCount, month, year);
				if ((i == 0 && j < startDay) || (dayCount > totalDaysInMonth)) {
					// have no dates
					calenderCellView
							.setState(CalenderCellView.STATE_UNAVAILABLE);
					calenderCellView.setText("");
				} else {
					// have dates
					calenderCellView.setText("" + dayCount);
					hasADate = true;
					// check for available dates
					if (isValidDate(currentDate)) {
						if (isAboveThanStart(currentDate)) {
							if (isLessThanEnd(currentDate)
									|| getStartDate().isSameDay(currentDate)) {
								calenderCellView
										.setState(CalenderCellView.STATE_SELECTED);
							} else {
								calenderCellView
										.setState(CalenderCellView.STATE_AVAILABLE);
							}
						} else {
							calenderCellView
									.setState(CalenderCellView.STATE_AVAILABLE);
						}
					} else {
						calenderCellView
								.setState(CalenderCellView.STATE_UNAVAILABLE);
					}

					if (isSettingEndDate() && !isAboveThanStart(currentDate)) {
						calenderCellView
								.setState(CalenderCellView.STATE_UNAVAILABLE);
					}
					if (calenderCellView.getState() != CalenderCellView.STATE_SELECTED
							&& today.isSameDay(currentDate)) {
						calenderCellView.markToday();
					}
					dayCount++;
				}

			}
			rowLayout.setVisibility(hasADate ? VISIBLE : GONE);
		}
	}

	private boolean isUnderSelection(CalDate currentDate) {
		return isAboveThanStart(currentDate) && isLessThanEnd(currentDate);
	}

	private boolean isAboveThanStart(CalDate currentDate) {
		return (getStartDate() != null && getStartDate()
				.isLessThan(currentDate));
	}

	private boolean isLessThanEnd(CalDate currentDate) {
		return ((getEndDate() != null && (getEndDate().isMoreThan(currentDate) || getEndDate()
				.isSameDay(currentDate))));
	}

	private boolean isValidDate(CalDate currentDate) {
		return isAboveThanValidStart(currentDate)
				&& isLessThanValidEnd(currentDate);
	}

	private boolean isAboveThanValidStart(CalDate currentDate) {
		return (getValidStartDate() == null || getValidStartDate().isLessThan(
				currentDate));
	}

	private boolean isLessThanValidEnd(CalDate currentDate) {
		return (getValidEndDate() == null
				|| getValidEndDate().isMoreThan(currentDate) || getValidEndDate()
				.isSameDay(currentDate));
	}

	/**
	 * of type
	 * 
	 * @since 24-Feb-2013
	 * @author Anuradha
	 */
	private void setDayNames() {
		for (int i = 0; i < 7; i++) {
			CalenderCellView calenderCellView = new CalenderCellView(
					getContext());
			calenderCellView.setState(CalenderCellView.STATE_DAY);
			calenderCellView.setText(DAYS[i]);
			dayLayout.addView(calenderCellView);
		}
	}

	/**
	 * of type
	 * 
	 * @since 24-Feb-2013
	 * @author Anuradha
	 */
	private void resetCalender() {
		dayLayout.removeAllViews();
		dayContentLayout.removeAllViews();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bigmarkenterprise.savvymob.views.CalenderCellView.CellClickedListener
	 * #onCellClicked(android.view.View)
	 * 
	 * @since Feb 25, 2013
	 * 
	 * @author achowdhury
	 */
	@Override
	public void onCellClicked(View v) {
		String day = (String) v.getTag();
		CalDate caldate = new CalDate(DataUtils.getAsInteger(day), getCalDate()
				.getmonth(), getCalDate().getYear());
		if (isSettingEndDate()) {
			if (getCalendarSelectedListener() != null) {
				getCalendarSelectedListener().onSelectEndDate(caldate);
			}
		} else {
			if (getCalendarSelectedListener() != null) {
				getCalendarSelectedListener().onSelectStartDate(caldate);
			}
		}

	}

	public CalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(CalDate startDate) {
		this.startDate = (startDate != null) ? startDate.clone() : null;
	}

	public CalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(CalDate endDate) {
		this.endDate = (endDate != null) ? endDate.clone() : null;
	}

	public interface CalendarSelectedListener {
		public void onSelectStartDate(CalDate caldate);

		public void onSelectEndDate(CalDate caldate);
	}

	public boolean checkIfCurrentDateBelongsToStartDate(CalDate caldate) {
		return (getStartDate() != null && getStartDate().hasSameMonth(caldate));
	}

	public boolean checkIfCurrentDateBelongsToEndDate(CalDate caldate) {
		return (getEndDate() != null && getEndDate().hasSameMonth(caldate));
	}

	public boolean checkIfEntireMouthIsInBetweenStartAndEnd(CalDate caldate) {
		return (caldate.isMoreThan(getStartDate()) && caldate
				.isLessThan(getEndDate()));
	}

	public CalDate getValidStartDate() {
		return validStartDate;
	}

	public void setValidStartDate(CalDate validStartDate) {
		this.validStartDate = validStartDate;
	}

	public CalDate getValidEndDate() {
		return validEndDate;
	}

	public void setValidEndDate(CalDate validEndDate) {
		this.validEndDate = validEndDate;
	}

	public CalDate getCalDate() {
		return calDate;
	}

	public void setCalDate(CalDate calDate) {
		this.calDate = calDate;
	}

	public boolean isSettingEndDate() {
		return settingEndDate;
	}

	public void setSettingEndDate(boolean settingEndDate) {
		this.settingEndDate = settingEndDate;
	}

	public CalendarSelectedListener getCalendarSelectedListener() {
		return calendarSelectedListener;
	}

	public void setCalendarSelectedListener(
			CalendarSelectedListener calendarSelectedListener) {
		this.calendarSelectedListener = calendarSelectedListener;
	}
}
