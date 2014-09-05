package com.rextuz.muteclock;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.PowerManager;
import android.widget.Toast;

public class AlarmManagerBroadcastReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		PowerManager pm;
		pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
		PowerManager.WakeLock wl;
		wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "WL");
		wl.acquire();
		try {
			AudioManager am;
			am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
			Bundle extras = intent.getExtras();
			boolean mute = extras.getBoolean("mute");
			boolean vibrate = extras.getBoolean("vibrate");
			String dayRule = extras.getString("day_rule");
			Calendar calendar = Calendar.getInstance();
			int today = calendar.get(Calendar.DAY_OF_WEEK);
			boolean repeat = true;
			if (dayRule.equals("-------"))
				repeat = false;
			if (!repeat | dayRule.charAt(today - 1) == '+') {
				if (mute) {
					if (vibrate) {
						Toast.makeText(context, R.string.set_vibrate,
								Toast.LENGTH_SHORT).show();
						am.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
					} else {
						Toast.makeText(context, R.string.set_mute,
								Toast.LENGTH_SHORT).show();
						am.setRingerMode(AudioManager.RINGER_MODE_SILENT);
					}
				} else {
					Toast.makeText(context, R.string.set_unmute,
							Toast.LENGTH_SHORT).show();
					am.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
				}
			}
		} catch (Exception e) {
			wl.release();
		}
		wl.release();
	}

	public void setAlarm(Context context, int requestCode, long muteTime,
			long unmuteTime, boolean vibrate, String days) {
		AlarmManager am;
		Intent intent;
		PendingIntent pi1, pi2;
		am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		intent = new Intent(context, AlarmManagerBroadcastReceiver.class);
		intent.putExtra("mute", true);
		intent.putExtra("vibrate", vibrate);
		intent.putExtra("day_rule", days);
		pi1 = PendingIntent.getBroadcast(context, requestCode, intent, 0);
		if (muteTime < System.currentTimeMillis())
			muteTime += 24 * 60 * 60 * 1000;
		if (days.equals("-------")) {
			am.set(AlarmManager.RTC_WAKEUP, muteTime, pi1);
		} else
			am.setRepeating(AlarmManager.RTC_WAKEUP, muteTime,
					AlarmManager.INTERVAL_DAY, pi1);
		intent.putExtra("mute", false);
		pi2 = PendingIntent.getBroadcast(context, requestCode + 1, intent, 0);
		if (unmuteTime < System.currentTimeMillis())
			unmuteTime += 24 * 60 * 60 * 1000;
		if (days.equals("-------")) {
			am.set(AlarmManager.RTC_WAKEUP, unmuteTime, pi2);
		} else
			am.setRepeating(AlarmManager.RTC_WAKEUP, unmuteTime,
					AlarmManager.INTERVAL_DAY, pi2);
	}

	public void cancelAlarm(Context context, int requestCode) {
		Intent intent = new Intent(context, AlarmManagerBroadcastReceiver.class);
		PendingIntent sender = PendingIntent.getBroadcast(context, requestCode,
				intent, 0);
		AlarmManager alarmManager = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		alarmManager.cancel(sender);
	}

}