package com.rextuz.muteclock.broadcast_receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.rextuz.muteclock.rules.RuleManager;
import com.rextuz.muteclock.activities.MainActivity;

public class ResetAlarmsBroadcastReciever extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        MainActivity.ruleManager = new RuleManager(context);
        MainActivity.ruleManager.load();
    }

}
