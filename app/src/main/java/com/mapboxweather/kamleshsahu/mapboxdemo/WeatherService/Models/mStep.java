package com.mapboxweather.kamleshsahu.mapboxdemo.WeatherService.Models;

import com.mapbox.api.directions.v5.models.LegStep;
import com.mapbox.geojson.Point;

import java.util.List;
import java.util.Map;

import static com.mapboxweather.kamleshsahu.mapboxdemo.WeatherService.Methods.TimeFormatter.getSDFtime;

/**
 * Created by k on 3/28/2019.
 */

public class mStep {

    private long jstarttime;

    private long aft_duration;
    private long aft_distance;
    private LegStep step;
    private String timezoneid;
    private int pos;

    Currently weatherdata;
    String Display_arrtime;
    double steplength;

    private Point step_StartPoint=null;

    private Map<Integer,mPoint> interms;


    public mStep(int k, Point location, long jstarttime, long aft_duration, long aft_distance, String timezoneid, LegStep legStep) {

        this.pos=k;
        this.step_StartPoint=location;
        this.jstarttime=jstarttime;
        this.aft_duration=aft_duration;
        this.aft_distance=aft_distance;
        this.timezoneid=timezoneid;
        this.step=legStep;


    }

    public long getJstarttime() {
        return jstarttime;
    }

    public void setJstarttime(long jstarttime) {
        this.jstarttime = jstarttime;
    }

    public long getAft_duration() {
        return aft_duration;
    }

    public void setAft_duration(long aft_duration) {
        this.aft_duration = aft_duration;
    }

    public long getAft_distance() {
        return aft_distance;
    }

    public void setAft_distance(long aft_distance) {
        this.aft_distance = aft_distance;
    }

    public LegStep getStep() {
        return step;
    }

    public void setStep(LegStep step) {
        this.step = step;
    }

    public String getTimezoneid() {
        return timezoneid;
    }

    public void setTimezoneid(String timezoneid) {
        this.timezoneid = timezoneid;
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public Currently getWeatherdata() {
        return weatherdata;
    }

    public void setWeatherdata(Currently weatherdata) {
        this.weatherdata = weatherdata;
    }

    public String getDisplay_arrtime() {
        return Display_arrtime;
    }

    public void setDisplay_arrtime(String display_arrtime) {
        Display_arrtime = display_arrtime;
    }

    public double getSteplength() {
        return steplength;
    }

    public void setSteplength(double steplength) {
        this.steplength = steplength;
    }

    public Point getStep_StartPoint() {
        return step_StartPoint;
    }

    public void setStep_StartPoint(Point step_StartPoint) {
        this.step_StartPoint = step_StartPoint;
    }

    public Map<Integer,mPoint> getInterms() {
        return interms;
    }

    public void setInterms(Map<Integer,mPoint> interms) {
        this.interms = interms;
    }

    public String getSDFTime(){
        return getSDFtime(jstarttime,aft_duration,timezoneid);
    }
}
