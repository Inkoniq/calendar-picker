/*
 * Copyright (c) 2013 Inkoniq
 * All Rights Reserved.
 * @since Feb 26, 2013 
 * @author achowdhury
 */
package com.inkoniq.calendar.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.inkoniq.calendar.CalDate;
import com.inkoniq.calendar.R;
import com.inkoniq.calendar.views.CalendarView;
import com.inkoniq.calendar.views.CalendarView.CalendarSelectedListener;

/**
 * 
 * @author Pushpan
 * 
 */
public class CalanderAdapter extends BaseAdapter {
	private Context context;
	ArrayList<CalDate> list = new ArrayList<CalDate>();
	private CalendarSelectedListener calendarSelectedListener;

	private CalDate startDate;
	private CalDate endDate;

	private CalDate validStartDate;
	private CalDate validEndDate;
	private boolean settingEndDate;

	/**
	 * 
	 * @return of type CalanderAdapter Constructor function
	 * @since Feb 26, 2013
	 * @author achowdhury
	 */
	public CalanderAdapter(Context context, ArrayList<CalDate> list) {
		this.context = context;
		this.list = list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getCount()
	 * 
	 * @since Feb 26, 2013
	 * 
	 * @author achowdhury
	 */
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		Log.e("TAG", list.size() + "");
		return list.size();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getItem(int)
	 * 
	 * @since Feb 26, 2013
	 * 
	 * @author achowdhury
	 */
	@Override
	public Object getItem(int index) {
		// TODO Auto-generated method stub
		return list.get(index);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getItemId(int)
	 * 
	 * @since Feb 26, 2013
	 * 
	 * @author achowdhury
	 */
	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getView(int, android.view.View,
	 * android.view.ViewGroup)
	 * 
	 * @since Feb 26, 2013
	 * 
	 * @author achowdhury
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		CalDate date = (CalDate) getItem(position);
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.calandar_listview, null);
			((CalendarView) convertView)
					.setCalendarSelectedListener(getCalendarSelectedListener());
		}
		CalendarView calView = (CalendarView) convertView;
		calView.setSettingEndDate(isSettingEndDate());
		calView.setStartDate(getStartDate());
		calView.setEndDate(getEndDate());
		calView.setValidStartDate(getValidStartDate());
		calView.setValidEndDate(getValidEndDate());
		calView.setCalendarDate(date);
		calView.requestLayout();
		return convertView;
	}

	public CalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(CalDate startDate) {
		this.startDate = startDate != null ? startDate.clone() : null;
	}

	public CalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(CalDate endDate) {
		this.endDate = endDate != null ? endDate.clone() : null;
	}

	public CalDate getValidStartDate() {
		return validStartDate;
	}

	public void setValidStartDate(CalDate validStartDate) {
		this.validStartDate = validStartDate != null ? validStartDate.clone()
				: null;
	}

	public CalDate getValidEndDate() {
		return validEndDate;
	}

	public void setValidEndDate(CalDate validEndDate) {
		this.validEndDate = validEndDate != null ? validEndDate.clone() : null;
	}

	public CalendarSelectedListener getCalendarSelectedListener() {
		return calendarSelectedListener;
	}

	public void setCalendarSelectedListener(
			CalendarSelectedListener calendarSelectedListener) {
		this.calendarSelectedListener = calendarSelectedListener;
	}

	public boolean isSettingEndDate() {
		return settingEndDate;
	}

	public void setSettingEndDate(boolean settingEndDate) {
		this.settingEndDate = settingEndDate;
	}
}
