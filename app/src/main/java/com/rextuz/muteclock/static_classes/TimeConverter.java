package com.rextuz.muteclock.static_classes;

import android.widget.DatePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class TimeConverter {

    public static long todayInMillis() {
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        c.clear();
        c.set(year, month, day);
        return c.getTimeInMillis();
    }

    public static String millisToDate(long timeInMillis, Locale locale) {
        SimpleDateFormat formatter = new SimpleDateFormat("d MMM yyyy", locale);
        return formatter.format(timeInMillis);
    }

    public static long datePickerToMillis(DatePicker datePicker) {
        Calendar c = Calendar.getInstance();
        c.clear();
        int year = datePicker.getYear();
        int month = datePicker.getMonth();
        int day = datePicker.getDayOfMonth();
        c.set(year, month, day);
        return c.getTimeInMillis();
    }

}
