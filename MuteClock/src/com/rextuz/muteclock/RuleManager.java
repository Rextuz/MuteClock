package com.rextuz.muteclock;

import java.util.ArrayList;

import android.content.Context;
import android.content.SharedPreferences;

public class RuleManager {

	public ArrayList<Rule> rules;
	private Context context;

	public RuleManager(Context context) {
		this.context = context;
		rules = new ArrayList<Rule>();
	}

	public int size() {
		return rules.size();
	}

	public Rule get(int position) {
		return rules.get(position);
	}

	public String getTime(int position) {
		return rules.get(position).getTime();
	}

	public boolean addRule(Rule newRule) {
		if (isClean(newRule)) {
			newRule.setAlarm();
			rules.add(newRule);
			sort();
			return true;
		}
		return false;
	}

	public void removeRule(int position) {
		Rule rule = get(position);
		rule.removeAlarm();
		rules.remove(rule);
	}

	public void save() {
		SharedPreferences sharedPrefs = context.getSharedPreferences(
				context.getString(R.string.preference_file_key),
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPrefs.edit();
		editor.clear();
		for (int i = 0; i < size(); i++) {
			editor.putLong("mute_time" + i, get(i).getMuteTime());
			editor.putLong("unmute_time" + i, get(i).getUnmuteTime());
			editor.putString("days" + i, get(i).getDays());
			editor.putInt("request_code" + i, get(i).getRequestCode());
			editor.putBoolean("vibrate" + i, get(i).getVibration());
		}
		editor.putInt("amount", size());
		editor.commit();
	}

	public void load() {
		SharedPreferences sharedPrefs = context.getSharedPreferences(
				context.getString(R.string.preference_file_key),
				Context.MODE_PRIVATE);
		rules.clear();
		int amount = sharedPrefs.getInt("amount", 0);
		for (int i = 0; i < amount; i++) {
			long muteTime = sharedPrefs.getLong("mute_time" + i, 0);
			long unmuteTime = sharedPrefs.getLong("unmute_time" + i, 0);
			String days = sharedPrefs.getString("days" + i, "");
			int requestCode = sharedPrefs.getInt("request_code" + i, 0);
			boolean vibrate = sharedPrefs.getBoolean("vibrate" + i, false);
			Rule rule = new Rule(context, muteTime, unmuteTime, days, vibrate, requestCode);
			addRule(rule);
		}
	}

	private void sort() {
		for (int i = 0; i < size() - 1; i++)
			for (int j = i + 1; j < size(); j++)
				if (get(i).getMuteTime()> get(j).getMuteTime()) {
					Rule tmpRule = get(i);
					rules.set(i, get(j));
					rules.set(j, tmpRule);
				}
	}

	private boolean isClean(Rule newRule) {
		for (int i = 0; i < rules.size(); i++) {
			Rule rule = rules.get(i);
			for (int j = 0; j < 7; j++)
				if (rule.getDay(j) == '+' & (newRule.getDay(j) == '+' | newRule.getDays().equals("-------"))) {
					long muteTimeSet = rule.getMuteTime();
					long unmuteTimeSet = rule.getUnmuteTime();

					long muteTime = newRule.getMuteTime();
					long unmuteTime = newRule.getUnmuteTime();

					if (muteTime < unmuteTimeSet) {
						if (unmuteTime > muteTimeSet)
							return false;
					}
					if (muteTime == unmuteTimeSet)
						return false;
				}
		}
		return true;
	}

}
