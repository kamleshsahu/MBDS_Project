package com.mapboxweather.kamleshsahu.mapboxdemo.Models;

import com.mapbox.geojson.Point;

/**
 * Created by k on 3/28/2019.
 */

public   class mPoint{
    Point point;
    String ds_arr_time;
    String location_name;
    Currently weather_data;
    String display_arrtime;



    public mPoint(Point point) {
        this.point = point;
    }

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
}