package com.example.vv.quicknote.util;

import android.annotation.SuppressLint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.DayOfWeek;
import java.util.Calendar;
import java.util.Locale;

import static com.example.vv.quicknote.constants.Constants.EMPTY_STR;

/**
 * @author Vivek Verma
 * @since 28/3/21
 */
public class DateUtil {
    private static final String[] DAY_OF_WEEK = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};

    @SuppressLint("DefaultLocale")
    public static String getCurrentTimeStamp() {
        Calendar calendar = Calendar.getInstance();
        String dayOfWeek = "---";
        try {
            dayOfWeek = DAY_OF_WEEK[calendar.get(Calendar.DAY_OF_WEEK) - 1];
        } catch (Exception ignored) {
        }
        return String.format("%s %02d-%02d-%d", dayOfWeek, calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.YEAR));
    }
}