package com.rextuz.muteclock;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;

public class AddActivity extends Activity {

	private TimePicker timePicker;
	private EditText editTextUnmute;
	private CheckBox vibrateBox, repeatBox;
	private ArrayList<CheckBox> dayBoxes;
	private TextView hideShow;
	private DatePicker datePicker;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_rule);

		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

		hideShow = (TextView) findViewById(R.id.hide_show);

		timePicker = (TimePicker) findViewById(R.id.timePicker1);
		timePicker.setIs24HourView(true);
		timePicker.setCurrentHour(12);
		timePicker.setCurrentMinute(0);
		editTextUnmute = (EditText) findViewById(R.id.editTextUnmute);
		editTextUnmute.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				LinearLayout layout = (LinearLayout) findViewById(R.id.timePickerLayout);
				if (hasFocus) {
					layout.setVisibility(View.GONE);
					hideShow.setText(R.string.show);
				}
			}
		});

		vibrateBox = (CheckBox) findViewById(R.id.vibrateBox);

		datePicker = (DatePicker) findViewById(R.id.datePicker);
		datePicker.setVisibility(View.GONE);

		repeatBox = (CheckBox) findViewById(R.id.repeatBox);
		repeatBox.setChecked(true);
		repeatBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
					datePicker.setVisibility(View.GONE);
					for (CheckBox box : dayBoxes)
						box.setVisibility(View.VISIBLE);
				} else {
					datePicker.setVisibility(View.VISIBLE);
					for (CheckBox box : dayBoxes)
						box.setVisibility(View.GONE);
				}
			}
		});
		dayBoxes = new ArrayList<CheckBox>();
		dayBoxes.add((CheckBox) findViewById(R.id.checkBoxSunday));
		dayBoxes.add((CheckBox) findViewById(R.id.checkBoxMonday));
		dayBoxes.add((CheckBox) findViewById(R.id.checkBoxTuesday));
		dayBoxes.add((CheckBox) findViewById(R.id.checkBoxWednesday));
		dayBoxes.add((CheckBox) findViewById(R.id.checkBoxThursday));
		dayBoxes.add((CheckBox) findViewById(R.id.checkBoxFriday));
		dayBoxes.add((CheckBox) findViewById(R.id.checkBoxSaturday));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.add_rule, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			Intent intent = new Intent(this, MainActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			break;
		case R.id.applyRule:
			createRule();
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		setResult(RESULT_CANCELED);
	}

	private void createRule() {
		long timeToday = TimeConverter.todayInMillis();
		long muteTime = timeToday
				+ (timePicker.getCurrentMinute() * 60 + timePicker
						.getCurrentHour() * 3600) * 1000;
		if (!editTextUnmute.getText().toString().isEmpty()) {
			long unmuteTime = muteTime
					+ (Integer.parseInt(editTextUnmute.getText().toString()) * 60)
					* 1000;
			int requestCode = (int) System.currentTimeMillis();
			boolean vibrate = vibrateBox.isChecked();
			StringBuilder daysBuilder = new StringBuilder("-------");
			String days;
			if (repeatBox.isChecked()) {
				for (int i = 0; i < 7; i++)
					if (dayBoxes.get(i).isChecked())
						daysBuilder.setCharAt(i, '+');
				days = daysBuilder.toString();
				if (days.equals("-------"))
					days = "+++++++";
			} else {
				days = "-------";
				muteTime -= timeToday;
				muteTime += TimeConverter.datePickerToMillis(datePicker);
			}
			if (muteTime > System.currentTimeMillis() || !repeatBox.isChecked()) {
				Rule newRule = new Rule(getApplicationContext(), muteTime,
						unmuteTime, days, vibrate, requestCode);
				if (MainActivity.ruleManager.addRule(newRule))
					setResult(RESULT_OK);
				else
					setResult(1);
				finish();
			} else
				Toast.makeText(getApplicationContext(), R.string.wrong_date,
						Toast.LENGTH_SHORT).show();
		} else
			Toast.makeText(getApplicationContext(), R.string.when_unmute,
					Toast.LENGTH_LONG).show();
	}

	public void onHideShowClick(View v) {
		LinearLayout layout = (LinearLayout) findViewById(R.id.timePickerLayout);
		if (hideShow.getText() == getString(R.string.hide)) {
			hideShow.setText(R.string.show);
			layout.setVisibility(View.GONE);
		} else {
			hideShow.setText(R.string.hide);
			layout.setVisibility(View.VISIBLE);
		}
	}

}
