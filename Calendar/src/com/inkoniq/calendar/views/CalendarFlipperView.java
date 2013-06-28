/**
 * 
 */
package com.inkoniq.calendar.views;

import java.util.ArrayList;
import java.util.Calendar;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.inkoniq.calendar.CalDate;
import com.inkoniq.calendar.R;
import com.inkoniq.calendar.views.CalendarView.CalendarSelectedListener;

/**
 * 
 * @author Pushpan
 * 
 */
public class CalendarFlipperView extends RelativeLayout implements
		OnTouchListener, CalendarSelectedListener, OnClickListener {
	public static final int ANIMATION_NONE = 0;
	public static final int ANIMATION_APPEAR_FROM_LEFT = 1;
	public static final int ANIMATION_APPEAR_FROM_RIGHT = 2;

	public static final int SWIPE_MIN_DISTANCE = 120;
	public static final int SWIPE_MAX_OFF_PATH = 250;
	public static final int SWIPE_THRESHOLD_VELOCITY = 200;
	private ImageView imgDummy;
	private CalendarView calendarView;
	private ArrayList<CalDate> calDates = new ArrayList<CalDate>();
	private int currentIndex;
	private GestureDetector gestureDetector;
	private ScrollView scrollView;
	private CalDate validStartDate;
	private CalDate validEndDate;
	private boolean settingEndDate;
	private CalendarSelectedListener calendarSelectedListener;
	private ImageView imgLeft;
	private ImageView imgRight;

	public int getCurrentIndex() {
		return currentIndex;
	}

	public void setCurrentIndex(int currentIndex) {
		this.currentIndex = currentIndex;
	}

	public CalendarFlipperView(Context context) {
		super(context);
		initialize();
	}

	public CalendarFlipperView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initialize();
	}

	private void initialize() {
		inflate(getContext(), R.layout.calendar_filpper, this);
		scrollView = (ScrollView) findViewById(R.id.scrollView);
		imgLeft = (ImageView) findViewById(R.id.imgLeft);
		imgLeft.setOnClickListener(this);
		imgRight = (ImageView) findViewById(R.id.imgRight);
		imgRight.setOnClickListener(this);
		scrollView.setOnTouchListener(this);
		calendarView = (CalendarView) scrollView
				.findViewById(R.id.calendarView);
		calendarView.setCalendarSelectedListener(this);
		imgDummy = (ImageView) findViewById(R.id.imgDummy);
		gestureDetector = new GestureDetector(getContext(),
				new PMGestureDetector());
		validateCalendar();
	}

	class PMGestureDetector extends SimpleOnGestureListener {
		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			try {
				if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
					return false;
				// right to left swipe
				if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE
						&& Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
					next();
				} else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE
						&& Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
					previous();
				}
			} catch (Exception e) {
				// nothing
			}
			return true;
		}

	}

	private boolean isPreviousAvailable() {
		if ((currentIndex - 1) >= 0) {
			return true;
		} else {
			return isPreviousMonthAvailable();
		}
	}

	private boolean isNextAvailable() {
		if ((currentIndex + 1) < calDates.size()) {
			return true;
		} else {
			return isNextMonthAvailable();
		}
	}

	public boolean previous() {
		// if not already editing at top
		boolean isAvailable = false;
		if ((currentIndex - 1) >= 0) {
			setCurrentIndex(currentIndex - 1);
			isAvailable = true;
		} else {
			if (isPreviousMonthAvailable()) {
				addMonthToTop();
				setCurrentIndex(currentIndex);
				isAvailable = true;
			}
		}
		if (isAvailable) {
			loadCurrentMonth(ANIMATION_APPEAR_FROM_LEFT);
		}
		return isAvailable;
	}

	public boolean next() {
		// if not already editing at top
		boolean isAvailable = false;
		if ((currentIndex + 1) < calDates.size()) {
			setCurrentIndex(currentIndex + 1);
			isAvailable = true;
		} else {
			if (isNextMonthAvailable()) {
				addMonthToBottom();
				setCurrentIndex(currentIndex + 1);
				isAvailable = true;
			}
		}
		if (isAvailable) {
			loadCurrentMonth(ANIMATION_APPEAR_FROM_RIGHT);
		}
		return isAvailable;
	}

	private void loadCurrentMonth(int anim) {
		if (anim == ANIMATION_NONE) {
			loadCalendar();
		} else {
			calendarView.setDrawingCacheEnabled(true);
			Bitmap bitmap = calendarView.getDrawingCache();
			Bitmap newBmp = Bitmap.createScaledBitmap(bitmap,
					bitmap.getWidth(), bitmap.getHeight(), false);
			imgDummy.setImageBitmap(newBmp);
			calendarView.setDrawingCacheEnabled(false);
			imgDummy.setVisibility(View.VISIBLE);
			Animation animationInLeft = AnimationUtils.loadAnimation(
					getContext(), R.anim.fragment_slide_in_left);
			Animation animationOutRight = AnimationUtils.loadAnimation(
					getContext(), R.anim.fragment_slide_out_right);
			Animation animationInRight = AnimationUtils.loadAnimation(
					getContext(), R.anim.fragment_slide_in_right);
			Animation animationOutLeft = AnimationUtils.loadAnimation(
					getContext(), R.anim.fragment_slide_out_left);

			AnimationListener animationListener = new AnimationListener() {

				@Override
				public void onAnimationEnd(Animation arg0) {
					imgDummy.setVisibility(View.GONE);
				}

				@Override
				public void onAnimationRepeat(Animation arg0) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onAnimationStart(Animation arg0) {
					loadCalendar();
				}
			};
			switch (anim) {
			case ANIMATION_APPEAR_FROM_LEFT:
				imgDummy.setAnimation(animationOutRight);
				calendarView.setAnimation(animationInLeft);
				animationInLeft.setAnimationListener(animationListener);
				break;
			case ANIMATION_APPEAR_FROM_RIGHT:
				imgDummy.setAnimation(animationOutLeft);
				calendarView.setAnimation(animationInRight);
				animationInRight.setAnimationListener(animationListener);
				break;
			}

			AnimationSet animationSet = new AnimationSet(true);
			animationSet.addAnimation(imgDummy.getAnimation());
			animationSet.addAnimation(calendarView.getAnimation());
			animationSet.startNow();
		}
	}

	public void loadCalendar() {
		calendarView.setSettingEndDate(isSettingEndDate());
		calendarView.setStartDate(getStartDate());
		calendarView.setEndDate(getEndDate());
		calendarView.setValidStartDate(getValidStartDate());
		calendarView.setValidEndDate(getValidEndDate());
		calendarView.setCalendarDate(calDates.get(currentIndex));
		calendarView.requestLayout();
		calendarView.refreshDrawableState();
		calendarView.invalidate();
		validateOptions();
	}

	private void validateOptions() {
		imgLeft.setImageResource(isPreviousAvailable() ? R.drawable.left_arrow
				: R.drawable.arrow_left_light_color);
		imgRight.setImageResource(isNextAvailable() ? R.drawable.right_arrow
				: R.drawable.arrow_right_light_color);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View.OnTouchListener#onTouch(android.view.View,
	 * android.view.MotionEvent)
	 * 
	 * @since 23-May-2013
	 * 
	 * @author Pushpan
	 */
	@Override
	public boolean onTouch(View arg0, MotionEvent event) {
		return gestureDetector.onTouchEvent(event);
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

	public void setStartDate(long millies) {
		calendarView.setStartDate(new CalDate(millies));
		loadCalendar();
	}

	public void setStartDate(CalDate calDate) {
		calendarView.setStartDate(calDate);
		loadCalendar();
	}

	public CalDate getStartDate() {
		return calendarView.getStartDate();
	}

	public void setEndDate(long millis) {
		calendarView.setEndDate(new CalDate(millis));
		loadCalendar();
	}

	public void setEndDate(CalDate calDate) {
		calendarView.setEndDate(calDate);
		loadCalendar();
	}

	public CalDate getEndDate() {
		return calendarView.getEndDate();
	}

	public CalDate getValidStartDate() {
		return validStartDate;
	}

	public void setValidStartDate(long millis) {
		this.validStartDate = new CalDate(millis);
		validateCalendar();
	}

	public void setValidStartDate(CalDate validStartDate) {
		this.validStartDate = validStartDate;
		validateCalendar();
	}

	public CalDate getValidEndDate() {
		return validEndDate;
	}

	public void setValidEndDate(long millis) {
		this.validEndDate = new CalDate(millis);
		validateCalendar();
	}

	public void setValidEndDate(CalDate validEndDate) {
		this.validEndDate = validEndDate;
		validateCalendar();
	}

	public void validateCalendar() {
		Calendar cal = Calendar.getInstance();
		// cal.add(Calendar.MONTH, 1);
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH);
		int day = cal.get(Calendar.DAY_OF_MONTH);
		CalDate currentMonth = new CalDate(day, month, year);

		calDates.clear();
		if (validStartDate != null) {
			calDates.add(validStartDate);
		} else {
			// validStartDate = currentMonth;
			// will be endless at the top
			// it should be scrolled to the 2nd item that will be current date
			// so that on scrolling up it will add another date dynamically
			calDates.add(currentMonth);
		}
		currentIndex = 0;
		loadCalendar();
	}

	@Override
	public void onSelectStartDate(CalDate caldate) {
		setStartDate(caldate);
		// setSettingEndDate(true);
		loadCalendar();
		// invalidate();
		if (getCalendarSelectedListener() != null) {
			getCalendarSelectedListener().onSelectStartDate(caldate);
		}

	}

	@Override
	public void onSelectEndDate(CalDate caldate) {
		setEndDate(caldate);
		loadCalendar();
		if (getCalendarSelectedListener() != null) {
			getCalendarSelectedListener().onSelectEndDate(caldate);
		}
	}

	public boolean isSettingEndDate() {
		return settingEndDate;
	}

	public void setSettingEndDate(boolean settingEndDate) {
		this.settingEndDate = settingEndDate;
		setEndDate(settingEndDate ? getEndDate() : null);
	}

	public CalendarSelectedListener getCalendarSelectedListener() {
		return calendarSelectedListener;
	}

	public void setCalendarSelectedListener(
			CalendarSelectedListener calendarSelectedListener) {
		this.calendarSelectedListener = calendarSelectedListener;
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.imgLeft) {
			previous();
		} else if (v.getId() == R.id.imgRight) {
			next();
		}
	}
}
