package com.inkoniq.calendar.utils;

public class DataUtils {

	public static long getAsLong(String stringValue) {
		long value = 0;
		try {
			value = (long) Double.parseDouble(stringValue);
		} catch (Exception e) {
		}
		return value;
	}

	public static int getAsInteger(String stringValue) {
		int value = 0;
		try {
			value = Integer.parseInt(stringValue);
		} catch (Exception e) {

		}
		return value;
	}

	public static float getAsFloat(String stringValue) {
		float value = 0;
		try {
			value = Float.parseFloat(stringValue);
		} catch (Exception e) {

		}
		return value;
	}

	public static boolean getAsBoolean(String stringValue) {
		boolean value = false;
		try {
			value = Boolean.parseBoolean(stringValue);
		} catch (Exception e) {

		}
		return value;
	}
}
