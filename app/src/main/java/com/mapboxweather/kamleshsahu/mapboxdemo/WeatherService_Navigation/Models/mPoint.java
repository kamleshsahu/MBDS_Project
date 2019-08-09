package com.mapboxweather.kamleshsahu.mapboxdemo.WeatherService_Navigation.Models;

import android.os.Parcel;
import android.os.Parcelable;

import com.mapbox.geojson.Point;

/**
 * Created by k on 3/28/2019.
 */

public   class mPoint implements Parcelable {
    Point point;
    String ds_arr_time;
    String location_name;
    Currently weather_data;
    String display_arrtime;



    public mPoint(Point point) {
        this.point = point;
    }

    protected mPoint(Parcel in) {
        ds_arr_time = in.readString();
        location_name = in.readString();
        display_arrtime = in.readString();
    }

    public static final Creator<mPoint> CREATOR = new Creator<mPoint>() {
        @Override
        public mPoint createFromParcel(Parcel in) {
            return new mPoint(in);
        }

        @Override
        public mPoint[] newArray(int size) {
            return new mPoint[size];
        }
    };

    public Point getPoint() {
        return point;
    }

    public void setPoint(Point point) {
        this.point = point;
    }

    public String getDs_arr_time() {
        return ds_arr_time;
    }

    public void setDs_arr_time(String ds_arr_time) {
        this.ds_arr_time = ds_arr_time;
    }

    public String getLocation_name() {
        return location_name;
    }

    public void setLocation_name(String location_name) {
        this.location_name = location_name;
    }

    public Currently getWeather_data() {
        return weather_data;
    }

    public void setWeather_data(Currently weather_data) {
        this.weather_data = weather_data;
    }

    public String getDisplay_arrtime() {
        return display_arrtime;
    }

    public void setDisplay_arrtime(String display_arrtime) {
        this.display_arrtime = display_arrtime;
    }

    @Override
    public String toString() {
        return "loc :"+location_name +",weather:"+weather_data.getSummary()+",time:"+display_arrtime;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(ds_arr_time);
        dest.writeString(location_name);
        dest.writeString(display_arrtime);
    }
}