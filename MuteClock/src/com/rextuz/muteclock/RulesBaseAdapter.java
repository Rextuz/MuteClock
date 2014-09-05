package com.rextuz.muteclock;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.FrameLayout;
import android.widget.TextView;

public class RulesBaseAdapter extends BaseAdapter {

	private static LayoutInflater inflater;
	private ArrayList<Rule> rules;
	public static ArrayList<Boolean> rulesToDelete;

	public RulesBaseAdapter(Context context, ArrayList<Rule> rules) {
		this.rules = rules;
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return rules.size();
	}

	@Override
	public Object getItem(int position) {
		return rules.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		rulesToDelete = new ArrayList<Boolean>();
		for (int i = 0; i < MainActivity.ruleManager.size(); i++)
			rulesToDelete.add(false);
		View view = convertView;
		if (view == null)
			view = inflater.inflate(R.layout.list_view_item, parent, false);
		Rule rule = (Rule) getItem(position);

		((TextView) view.findViewById(R.id.textRuleTime)).setText(rule
				.getTime());
		((TextView) view.findViewById(R.id.textRuleDays)).setText(rule
				.getParcedDays());
		CheckBox toDelete = (CheckBox) view.findViewById(R.id.toDelete);
		if (MainActivity.ruleManager.get(position).getVibration())
			((FrameLayout) view.findViewById(R.id.frameLayout1))
					.setBackgroundResource(R.drawable.vibrate);
		else
			((FrameLayout) view.findViewById(R.id.frameLayout1))
					.setBackgroundResource(R.drawable.mute);
		toDelete.setChecked(false);
		toDelete.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked)
					rulesToDelete.set(position, true);
				else
					rulesToDelete.set(position, false);
			}

		});
		return view;
	}

}
