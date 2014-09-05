package com.rextuz.muteclock;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ResetAlarmsBroadcastReciever extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		MainActivity.ruleManager = new RuleManager(context);
		MainActivity.ruleManager.load();
	}

}
