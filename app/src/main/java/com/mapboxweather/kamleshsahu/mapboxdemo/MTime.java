package com.mapboxweather.kamleshsahu.mapboxdemo;

import android.databinding.BaseObservable;


import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

import static com.mapboxweather.kamleshsahu.mapboxdemo.WeatherService.Constants.month;

public class MTime extends BaseObservable {

     String timezone;
     int mYear, mMonth, mDay, mHour, mMinute;
     long jstart_date_millis, jstart_time_millis;
     String disp_time;

    public MTime() {
        final Calendar c = Calendar.getInstance();
        timezone=c.getTimeZone().getID();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);
        jstart_date_millis=c.getTimeInMillis()-((mHour*60+mMinute)*60*1000);
        jstart_time_millis=(mHour*60+mMinute)*60*1000;
        resetDisp_time();
    }


    public void setdate(int year, int monthOfYear, int dayOfMonth){
        Calendar cal = Calendar.getInstance();
        cal.setTimeZone(TimeZone.getTimeZone(timezone));
        cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        cal.set(Calendar.MONTH, monthOfYear);
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        mDay=dayOfMonth;
        mMonth=monthOfYear;
        mYear=year;
        mHour = 0;
        mMinute = 0;
        jstart_date_millis = cal.getTimeInMillis();
        jstart_time_millis =0;
        resetDisp_time();
    }

    public void settime(int hourOfDay, int minute){
        mHour = hourOfDay;
        mMinute = minute;
        jstart_time_millis = (mHour * 60 + mMinute) * 60 * 1000;
        resetDisp_time();
    }


    public String getDisp_time() {
        return disp_time;
    }

    private void resetDisp_time() {
        this.disp_time =String.format(Locale.ENGLISH,"%02d:%02d,%02d %s ,%s", mHour,mMinute,mDay,month[mMonth],String.valueOf(mYear).substring(2));
    }


}
