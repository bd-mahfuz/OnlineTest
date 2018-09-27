package com.test.onlinetest.utility;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by mahfuz on 9/11/18.
 */

public class OnlineTestUtility {



    public static String getTimeToMilli(long millisecond) {
        if (millisecond < 0) {
            return null;
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm");

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millisecond);

        Date date = calendar.getTime();
        return dateFormat.format(date);
    }


    public static String getDateToMilli(long millisecond) {
        Log.d("mill,", millisecond+"");
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm a");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millisecond);

        Date date = calendar.getTime();

        return dateFormat.format(date);
    }

    public static long getTimeToMilii(String date, String time) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm a");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

        try {
            Date date1 = dateFormat.parse(date+" "+time);
            //System.out.println(dateFormat.format(date1));
            return date1.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }

    }

    public static boolean isThisDateValid(String dateToValidate){
        if(dateToValidate == null){
            return false;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyy");
        sdf.setLenient(false);

        try {

            //if not valid, it will throw ParseException
            Date date = sdf.parse(dateToValidate);
            System.out.println(date);

        } catch (ParseException e) {

            e.printStackTrace();
            return false;
        }

        return true;
    }


    public static boolean isDatePreviousDate(String dateString) {

        if (dateString.equals(null) || dateString.isEmpty()) {
            return false;
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = null;
        try {
            date = dateFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        long dateMilli = date.getTime();
        long currentDateInMilli = new Date().getTime();

//        Log.d("dateMilii", dateMilli+"");
//        Log.d("current date milli", currentDateInMilli+"");

        if (dateMilli >= currentDateInMilli) {
            return true;
        }
        return false;
    }

    public static long getMilliSecondDate(String dateString)  {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date date = dateFormat.parse(dateString);
            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static int getCurrentYear() {
        Date date = new Date();
        Calendar calendar= Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.YEAR);
    }

    public static int getCurrentDay() {
        Date date = new Date();
        Calendar calendar= Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    public static int getCurrentMonth() {
        Date date = new Date();
//        Calendar calendar= Calendar.getInstance();
//        calendar.setTime(date);
        return date.getMonth();
    }
}
