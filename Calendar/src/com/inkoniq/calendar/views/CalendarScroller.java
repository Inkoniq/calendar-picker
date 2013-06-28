/**
 * 
 */
package com.inkoniq.calendar.views;

import java.util.ArrayList;
import java.util.Calendar;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;

import com.inkoniq.calendar.CalDate;
import com.inkoniq.calendar.adapter.CalanderAdapter;
import com.inkoniq.calendar.views.CalendarView.CalendarSelectedListener;

/**
 * 
 * @author Pushpan
 * 
 */
public class CalendarScroller extends ListView implements OnScrollListener,
		CalendarSelectedListener {
	private static final int MAX_VISIBLE_MONTHS = 3;
	private CalendarSelectedListener calendarSelectedListener;
	ArrayList<CalDate> calDates = new ArrayList<CalDate>();
	private CalanderAdapter calanderAdapter;
	private CalDate startDate;
	private CalDate endDate;
	private CalDate validStartDate;
	private CalDate validEndDate;
	private boolean settingEndDate;

	/**
	 * @param context
	 */
	public CalendarScroller(Context context) {
		super(context);
		initialize();
	}

	/**
	 * @param context
	 * @param attrs
	 */
	public CalendarScroller(Context context, AttributeSet attrs) {
		super(context, attrs);
		initialize();
	}

	/**
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public CalendarScroller(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initialize();
	}

	private void initialize() {
		setOnScrollListener(this);
		validateCalendar();
		postDelayed(new Runnable() {

			@Override
			public void run() {
				// focus to the second item
				if (validStartDate == null) {
					if (calDates.size() >= 2) {
						setSelection(1);
					}
				}
			}
		}, 100);
	}

	public void validateCalendar() {
		Calendar cal = Calendar.getInstance();
		// cal.add(Calendar.MONTH, 1);
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH);
		int day = cal.get(Calendar.DAY_OF_MONTH);
		CalDate currentMonth = new CalDate(day, month, year);

		calDates.clear();
		int visibleMonth = 0;

		if (validStartDate != null) {
			calDates.add(validStartDate);
			visibleMonth++;
		} else {
			// validStartDate = currentMonth;
			// will be endless at the top
			// it should be scrolled to the 2nd item that will be current date
			// so that on scrolling up it will add another date dynamically
			// calDates.add(currentMonth.previousMonth());
			calDates.add(currentMonth);
			visibleMonth += 1;
		}

		while (visibleMonth < MAX_VISIBLE_MONTHS && isNextMonthAvailable()) {
			addMonthToBottom();
			visibleMonth++;
		}

		//
		// calDates.add(new CalDate(day, month++, year));
		// calDates.add(new CalDate(1, month++, year));
		// calDates.add(new CalDate(1, month++, year));
		// calDates.add(new CalDate(1, month++, year));
		// calDates.add(new CalDate(1, month++, year));
		calanderAdapter = new CalanderAdapter(getContext(), calDates);
		calanderAdapter.setStartDate(startDate);
		calanderAdapter.setEndDate(endDate);
		calanderAdapter.setValidStartDate(getValidStartDate());
		calanderAdapter.setValidEndDate(getValidEndDate());
		calanderAdapter.setCalendarSelectedListener(this);
		setAdapter(calanderAdapter);
		refresh();
	}

	public boolean isNextMonthAvailable() {
		return (calDates.size() > 0 && (validEndDate == null || validEndDate
				.isMoreThan(calDates.get(calDates.size() - 1))));
	}

	public boolean addMonthToBottom() {
		if (calDates.size() > 0
				&& (validEndDate == null || validEndDate.isMoreThan(calDates
						.get(calDates.size() - 1)))) {
			calDates.add(calDates.get(calDates.size() - 1).nextMonth());
			return true;
		}
		return false;
	}

	public boolean isPreviousMonthAvailable() {
		return (calDates.size() > 0 && (validStartDate == null || validStartDate
				.isLessThan(calDates.get(0))));
	}

	public boolean addMonthToTop() {
		if (calDates.size() > 0
				&& (validStartDate == null || validStartDate
						.isLessThan(calDates.get(0)))) {
			calDates.add(0, calDates.get(0).previousMonth());
			return true;
		}
		return false;
	}

	public CalanderAdapter getCalanderAdapter() {
		return calanderAdapter;
	}

	public void setCalanderAdapter(CalanderAdapter calanderAdapter) {
		this.calanderAdapter = calanderAdapter;
	}

	private void addMoreItemsOnTop() {
		if (isPreviousMonthAvailable()) {
			addMonthToTop();
			calanderAdapter.notifyDataSetChanged();
			requestLayout();
		}
	}

	private void addMoreItemsOnBottom() {
		if (isNextMonthAvailable()) {
			addMonthToBottom();
			calanderAdapter.notifyDataSetChanged();
			requestLayout();
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		//
		// if (firstVisibleItem == 0 && shouldAddToTop()) {
		// postDelayed(new Runnable() {
		//
		// @Override
		// public void run() {
		// addMoreItemsOnTop();
		// }
		// }, 500);
		// }

		if (totalItemCount != 0
				&& firstVisibleItem + visibleItemCount >= totalItemCount) {
			postDelayed(new Runnable() {
				@Override
				public void run() {
					addMoreItemsOnBottom();
				}
			}, 500);
		}
	}

	private boolean shouldAddToTop() {
		if (calDates.size() > 0) {
			// check if already the top item
			if (getChildAt(0).getTag() == calDates.get(0)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {

	}

	public void setStartDate(CalDate calDate) {
		this.startDate = calDate;
		calanderAdapter.setStartDate(calDate);
		refresh();
	}

	public CalDate getStartDate() {
		return calanderAdapter.getStartDate();
	}

	public void setStartDate(long millis) {
		setStartDate(new CalDate(millis));
	}

	public void setEndDate(CalDate calDate) {
		this.endDate = calDate;
		calanderAdapter.setEndDate(calDate);
		refresh();
	}

	public void setEndDate(long millis) {
		setEndDate(new CalDate(millis));
	}

	public CalDate getEndDate() {
		return calanderAdapter.getEndDate();
	}

	public CalDate getValidStartDate() {
		return validStartDate;
	}

	public void setValidStartDate(CalDate validStartDate) {
		this.validStartDate = validStartDate;
		validateCalendar();
	}

	public void setValidStartDate(long millis) {
		setValidStartDate(new CalDate(millis));
	}

	public CalDate getValidEndDate() {
		return validEndDate;
	}

	public void setValidEndDate(CalDate validEndDate) {
		this.validEndDate = validEndDate;
		validateCalendar();
	}

	public void setValidEndDate(long millis) {
		setValidEndDate(new CalDate(millis));
	}

	public CalendarSelectedListener getCalendarSelectedListener() {
		return calendarSelectedListener;
	}

	public void setCalendarSelectedListener(
			CalendarSelectedListener calendarSelectedListener) {
		this.calendarSelectedListener = calendarSelectedListener;
	}

	@Override
	public void onSelectStartDate(CalDate caldate) {
		setStartDate(caldate);
		if (getCalendarSelectedListener() != null) {
			getCalendarSelectedListener().onSelectStartDate(caldate);
		}

	}

	@Override
	public void onSelectEndDate(CalDate caldate) {
		setEndDate(caldate);
		if (getCalendarSelectedListener() != null) {
			getCalendarSelectedListener().onSelectEndDate(caldate);
		}
	}

	public void refresh() {
		calanderAdapter.notifyDataSetChanged();
		refreshDrawableState();
		invalidate();
		invalidateViews();
	}

	public boolean isSettingEndDate() {
		return settingEndDate;
	}

	public void setSettingEndDate(boolean settingEndDate) {
		this.settingEndDate = settingEndDate;
		calanderAdapter.setSettingEndDate(settingEndDate);
		setEndDate(settingEndDate ? getEndDate() : null);
	}
}
