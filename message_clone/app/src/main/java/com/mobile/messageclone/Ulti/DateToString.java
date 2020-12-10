package com.mobile.messageclone.Ulti;

import android.icu.text.SimpleDateFormat;
import android.util.Log;

import com.instacart.library.truetime.TrueTimeRx;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
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
        Date trueTime;
        if (TrueTimeRx.isInitialized() == true) {
            trueTime = TrueTimeRx.now();
        }
        else {
            trueTime = Calendar.getInstance().getTime();
        }

            LocalDateTime toDate = trueTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
            LocalDateTime fromDate = dateFromInput.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

            LocalDateTime tempDate = LocalDateTime.from(fromDate);

            long years = tempDate.until(toDate, ChronoUnit.YEARS);
            tempDate = tempDate.plusYears(years);

            long days = tempDate.until(toDate, ChronoUnit.DAYS);
            tempDate = tempDate.plusDays(days);


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

        return dateToString;


    }

    public static String LastSeenString(Date dateInput) {

        String returnString;

        Date date = dateInput;
        Date todayDate;
        if (TrueTimeRx.isInitialized() == true) {
            todayDate = TrueTimeRx.now();
        } else {
            todayDate = android.icu.util.Calendar.getInstance().getTime();
        }

        LocalDateTime fromDateTime = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime toDateTime = todayDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

        LocalDateTime tempDateTime = LocalDateTime.from(fromDateTime);

        long years = tempDateTime.until(toDateTime, ChronoUnit.YEARS);
        tempDateTime = tempDateTime.plusYears(years);

        long months = tempDateTime.until(toDateTime, ChronoUnit.MONTHS);
        tempDateTime = tempDateTime.plusMonths(months);

        long days = tempDateTime.until(toDateTime, ChronoUnit.DAYS);
        tempDateTime = tempDateTime.plusDays(days);


        long hours = tempDateTime.until(toDateTime, ChronoUnit.HOURS);
        tempDateTime = tempDateTime.plusHours(hours);

        long minutes = tempDateTime.until(toDateTime, ChronoUnit.MINUTES);
        tempDateTime = tempDateTime.plusMinutes(minutes);

        long seconds = tempDateTime.until(toDateTime, ChronoUnit.SECONDS);
        //ContactAndSeenTime contactAndSeenTime1 = new ContactAndSeenTime();
        //contactAndSeenTime1.Status = status;
        // Log.d("Phone", contactAndSeenTime1.Status);
        // Log.d("Phone", finalContact1.getFirstNickName());
        // contactAndSeenTime1.contact = finalContact1;


        if (days >= 1 && days <= 2) {
            java.text.SimpleDateFormat simpleDateFormat = new java.text.SimpleDateFormat("HH:mm");
            Log.d("Phone", "yesterday at " + simpleDateFormat.format(date));
            returnString = "yesterday at " + simpleDateFormat.format(date);
        } else if (days < 1 && hours >= 1) {

            Log.d("Phone", "at " + hours + " hours ago");
            returnString = "at " + hours + " hours ago";
        } else if (days < 1 && hours < 1 && minutes>0) {
            Log.d("Phone", +minutes + " minutes ago");
            returnString = "at " + minutes + " minutes ago";

        }
        else if (days<1 && hours<1 && minutes<=0)
        {
            returnString="at a few second ago";
        }
        else {
            returnString = "at " + fromDateTime.getDayOfMonth() + "-" + fromDateTime.getMonthValue() + "-" + fromDateTime.getYear();
            Log.d("Phone", "yesterday at " + fromDateTime.getDayOfMonth() + "-" + fromDateTime.getMonthValue() + "-" + fromDateTime.getYear());
        }

        return returnString;
    }
}