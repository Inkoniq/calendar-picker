package com.inkoniq.calendarexample;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.inkoniq.calendar.views.CalendarScroller;

/**
 * 
 * @author Pushpan
 * 
 */
public class CalendarScrollerActivity extends Activity implements
		OnClickListener {
	private CalendarScroller calendarView;
	private Button btnStartDate;
	private Button btnEndDate;
	private TextView txtSelectingFor;
	private Button btnReset;
	private Button btnSwitch;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_calendar_scroller);
		calendarView = (CalendarScroller) findViewById(R.id.calendarView);
		btnStartDate = (Button) findViewById(R.id.btnStartDate);
		btnStartDate.setOnClickListener(this);
		btnEndDate = (Button) findViewById(R.id.btnEndDate);
		btnEndDate.setOnClickListener(this);
		txtSelectingFor = (TextView) findViewById(R.id.txtSelectingFor);
		btnReset = (Button) findViewById(R.id.btnReset);
		btnReset.setOnClickListener(this);
		btnSwitch = (Button) findViewById(R.id.btnSwitch);
		btnSwitch.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnStartDate:
			calendarView.setSettingEndDate(false);
			txtSelectingFor.setText(R.string.select_start_date);
			break;
		case R.id.btnEndDate:
			calendarView.setSettingEndDate(true);
			txtSelectingFor.setText(R.string.select_end_date);
			break;
		case R.id.btnReset:
			calendarView.setStartDate(null);
			calendarView.setEndDate(null);
			calendarView.setSettingEndDate(false);
			calendarView.validateCalendar();
			break;
		case R.id.btnSwitch:
			Intent intent = new Intent(this, CalendarFlipperActivity.class);
			startActivity(intent);
			finish();
			break;
		}
	}

}
