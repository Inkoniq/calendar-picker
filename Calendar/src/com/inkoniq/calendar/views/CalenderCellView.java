/*
 * Copyright (c) 2013 Inkoniq
 * All Rights Reserved.
 * @since 24-Feb-2013 
 * @author Anuradha
 */
package com.inkoniq.calendar.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.inkoniq.calendar.R;

/**
 * 
 * @author Pushpan
 * 
 */
public class CalenderCellView extends LinearLayout implements OnClickListener {
	public static final int STATE_UNAVAILABLE = 0;
	public static final int STATE_AVAILABLE = 1;
	public static final int STATE_SELECTED = 2;
	public static final int STATE_DAY = 3;
	private TextView cellText;
	private int state;
	private CellClickedListener cellClickedListener;

	/**
	 * @param context
	 * @return of type CalenderCellView Constructor function
	 * @since 24-Feb-2013
	 * @author Anuradha
	 */
	public CalenderCellView(Context context) {
		super(context);
		initialize();
	}

	/**
	 * @param context
	 * @param attrs
	 * @return of type CalenderCellView Constructor function
	 * @since 24-Feb-2013
	 * @author Anuradha
	 */
	public CalenderCellView(Context context, AttributeSet attrs) {
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
		inflate(getContext(), R.layout.calendar_cell, this);
		cellText = (TextView) findViewById(R.id.cellText);
		setOnClickListener(this);
		setClickable(true);
		setFocusable(true);
	}

	public void setText(String text) {
		cellText.setText(text);
	}

	/**
	 * @param of
	 *            type null
	 * @return state of type int getter function for state
	 * @since 24-Feb-2013
	 * @author Anuradha
	 */
	public int getState() {
		return state;
	}

	/**
	 * @param state
	 *            of type int
	 * @return of type null setter function for state
	 * @since 24-Feb-2013
	 * @author Anuradha
	 */
	public void setState(int state) {
		cellText.setBackgroundDrawable(null);
		this.state = state;
		switch (state) {
		case STATE_UNAVAILABLE:
			cellText.setTextColor(getResources().getColor(R.color.grey));
			break;
		case STATE_AVAILABLE:
			cellText.setBackgroundResource(R.drawable.calendar_rounded_rect_trans_black);
			cellText.setTextColor(getResources().getColor(R.color.white));
			break;
		case STATE_SELECTED:
			cellText.setBackgroundResource(R.drawable.calendar_button_red_rounded_corner);
			cellText.setTextColor(getResources().getColor(R.color.white));
			break;
		case STATE_DAY:
			cellText.setTextColor(getResources().getColor(R.color.white));
			break;
		}
		invalidate();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 * 
	 * @since 24-Feb-2013
	 * 
	 * @author Anuradha
	 */
	@Override
	public void onClick(View v) {
		v.setTag(cellText.getText().toString());
		if (getState() == STATE_AVAILABLE || getState() == STATE_SELECTED) {
			setState(STATE_SELECTED);
			if (getCellClickedListener() != null) {
				getCellClickedListener().onCellClicked(v);
			}
		}
	}

	/**
	 * @param of
	 *            type null
	 * @return cellClickedListener of type CellClickedListener getter function
	 *         for cellClickedListener
	 * @since 24-Feb-2013
	 * @author Anuradha
	 */
	public CellClickedListener getCellClickedListener() {
		return cellClickedListener;
	}

	/**
	 * @param cellClickedListener
	 *            of type CellClickedListener
	 * @return of type null setter function for cellClickedListener
	 * @since 24-Feb-2013
	 * @author Anuradha
	 */
	public void setCellClickedListener(CellClickedListener cellClickedListener) {
		this.cellClickedListener = cellClickedListener;
	}

	public interface CellClickedListener {
		public void onCellClicked(View v);
	}

	public void markToday() {
		cellText.setBackgroundResource(R.drawable.calendar_rounded_rect_trans_grey);
		cellText.setTextColor(getResources().getColor(R.color.white));
	}

}
