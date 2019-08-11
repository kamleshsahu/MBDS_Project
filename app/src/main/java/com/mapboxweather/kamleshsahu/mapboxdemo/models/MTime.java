package com.mapboxweather.kamleshsahu.mapboxdemo.models;

import android.databinding.BaseObservable;
import android.os.Parcel;
import android.os.Parcelable;


import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

import static com.mapboxweather.kamleshsahu.mapboxdemo.WeatherService_Navigation.Constants.month;


public class MTime extends BaseObservable implements Parcelable {

     public String timezone;
     public int mYear;
    public int mMonth;
    public int mDay;
    public int mHour;
    public int mMinute;
    public long jstart_date_millis, jstart_time_millis;
     public String disp_time;

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


    protected MTime(Parcel in) {
        timezone = in.readString();
        mYear = in.readInt();
        mMonth = in.readInt();
        mDay = in.readInt();
        mHour = in.readInt();
        mMinute = in.readInt();
        jstart_date_millis = in.readLong();
        jstart_time_millis = in.readLong();
        disp_time = in.readString();
    }

    public static final Creator<MTime> CREATOR = new Creator<MTime>() {
        @Override
        public MTime createFromParcel(Parcel in) {
            return new MTime(in);
        }

        @Override
        public MTime[] newArray(int size) {
            return new MTime[size];
        }
    };

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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(timezone);
        dest.writeInt(mYear);
        dest.writeInt(mMonth);
        dest.writeInt(mDay);
        dest.writeInt(mHour);
        dest.writeInt(mMinute);
        dest.writeLong(jstart_date_millis);
        dest.writeLong(jstart_time_millis);
        dest.writeString(disp_time);
    }


    public long gettime_millis() {
        return jstart_date_millis+jstart_time_millis;
    }
}
