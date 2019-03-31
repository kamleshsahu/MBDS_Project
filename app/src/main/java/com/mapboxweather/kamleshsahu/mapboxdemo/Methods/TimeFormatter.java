package com.mapboxweather.kamleshsahu.mapboxdemo.Methods;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

import static com.mapboxweather.kamleshsahu.mapboxdemo.Constants.month;
import static com.mapboxweather.kamleshsahu.mapboxdemo.Constants.strDays;

/**
 * Created by k on 3/29/2019.
 */

public class TimeFormatter {

    public TimeFormatter() {
    }

   static public String getDisplayTime(String time, String timezone) {
        final Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone(timezone));
        c.setTimeInMillis(Long.parseLong(time) * 1000);
        int mHour = c.get(Calendar.HOUR_OF_DAY);
        int mMinute = c.get(Calendar.MINUTE);


        String sHour = mHour < 10 ? "0" + mHour : "" + mHour;
        String sMinute = mMinute < 10 ? "0" + mMinute : "" + mMinute;
        String stn_arrtime = sHour + ":" + sMinute + " ," + strDays[c.get(Calendar.DAY_OF_WEEK) - 1] + " ," + c.get(Calendar.DAY_OF_MONTH) + " " + month[c.get(Calendar.MONTH)] + " " + String.valueOf(c.get(Calendar.YEAR)).substring(2);
        return stn_arrtime;
    }

   static public String getSDFtime(long arrival_time_millis,String timezoneid){
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        sdf.setTimeZone(TimeZone.getTimeZone(timezoneid));
        String time = sdf.format(arrival_time_millis);
        return time;
    }
}
