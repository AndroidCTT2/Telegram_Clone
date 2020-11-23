package com.mobile.messageclone.Chat;

import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.util.Log;

import com.instacart.library.truetime.TrueTimeRx;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class DateToString {

    public static String dateToString(String dateInput){
        String dateToString = "";
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy-HH-mm-ss-X");
        Date dateFromInput = new Date();
        try {
            dateFromInput = dateFormat.parse(dateInput);
        } catch (ParseException e) {
        }
        if (TrueTimeRx.isInitialized() == true) {
            Date trueTime = TrueTimeRx.now();
            LocalDateTime toDate = trueTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
            LocalDateTime fromDate = dateFromInput.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

            LocalDateTime tempDate = LocalDateTime.from(fromDate);

            long years = tempDate.until(toDate, ChronoUnit.YEARS);
            tempDate = tempDate.plusYears(years);

            long days = tempDate.until(toDate, ChronoUnit.DAYS);
            tempDate = tempDate.plusDays(days);
            Log.d("Days", "dateToString: " + dateInput);

            long seconds = tempDate.until(toDate, ChronoUnit.SECONDS);
            if (days < 1) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
                dateToString = simpleDateFormat.format(dateFromInput);
            } else if (years < 1) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM");
                dateToString = simpleDateFormat.format(dateFromInput);
            } else {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                dateToString = simpleDateFormat.format(dateFromInput);
            }
        }
        return dateToString;


    }
}