package com.rextuz.muteclock;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.content.Context;

public class Rule {
	private Context context;
	private long muteTime;
	private long unmuteTime;
	// private String time;
	private String days;
	private boolean vibrate;
	private int requestCode;
	AlarmManagerBroadcastReceiver alarm;

	public Rule(Context context, long muteTime, long unmuteTime, String days,
			boolean vibrate, int requestCode) {
		this.context = context;
		this.muteTime = muteTime;
		this.unmuteTime = unmuteTime;
		this.days = days;
		this.vibrate = vibrate;
		this.requestCode = requestCode;
		alarm = new AlarmManagerBroadcastReceiver();
	}

	public void setAlarm() {
		alarm.setAlarm(context, requestCode, muteTime, unmuteTime, vibrate,
				days);
	}

	public void removeAlarm() {
		alarm.cancelAlarm(context, requestCode);
		alarm.cancelAlarm(context, requestCode + 1);
	}

	public String getTime() {
		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm", Locale.ROOT);
		String muteTimeString = formatter.format(new Date(muteTime));
		String unmuteTimeString = formatter.format(new Date(unmuteTime));
		String time = muteTimeString + " - " + unmuteTimeString;
		return time;
	}

	public char getDay(int position) {
		return days.charAt(position);
	}

	public String getDays() {
		return days;
	}

	public int getRequestCode() {
		return requestCode;
	}

	public boolean getVibration() {
		return vibrate;
	}

	public String getParcedDays() {
		String newDayRule = new String();
		String days = getDays();
		if (days.equals("-------")) {
			Locale currentLocale = context.getResources().getConfiguration().locale;
			newDayRule = TimeConverter.millisToDate(muteTime, currentLocale);
		} else if (days.equals("+++++++")) {
			newDayRule = context.getString(R.string.everyday);
		} else {
			if (days.charAt(1) == '+')
				newDayRule += context.getString(R.string.mon) + ", ";
			if (days.charAt(2) == '+')
				newDayRule += context.getString(R.string.tue) + ", ";
			if (days.charAt(3) == '+')
				newDayRule += context.getString(R.string.wed) + ", ";
			if (days.charAt(4) == '+')
				newDayRule += context.getString(R.string.thu) + ", ";
			if (days.charAt(5) == '+')
				newDayRule += context.getString(R.string.fri) + ", ";
			if (days.charAt(6) == '+')
				newDayRule += context.getString(R.string.sat) + ", ";
			if (days.charAt(0) == '+')
				newDayRule += context.getString(R.string.sun) + ", ";
			newDayRule = newDayRule.substring(0, newDayRule.length() - 2);
		}
		return newDayRule;
	}

	long getMuteTime() {
		return muteTime;
	}

	long getUnmuteTime() {
		return unmuteTime;
	}

}
