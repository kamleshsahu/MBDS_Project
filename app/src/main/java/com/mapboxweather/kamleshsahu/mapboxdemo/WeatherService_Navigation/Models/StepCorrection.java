package com.mapboxweather.kamleshsahu.mapboxdemo.WeatherService_Navigation.Models;

import com.mapbox.geojson.Point;

/**
 * Created by k on 5/3/2019.
 */

public class StepCorrection {

    int newdistance;
    int newduration;
    Point newlocation;
    int distfromstepstart;

    public StepCorrection(int newdistance, int newduration, Point newlocation, int distfromstepstart) {
        this.newdistance = newdistance;
        this.newduration = newduration;
        this.newlocation = newlocation;
        this.distfromstepstart=distfromstepstart;
    }

    public int getNewdistance() {
        return newdistance;
    }

    public void setNewdistance(int newdistance) {
        this.newdistance = newdistance;
    }

    public int getNewduration() {
        return newduration;
    }

    public void setNewduration(int newduration) {
        this.newduration = newduration;
    }

    public Point getNewlocation() {
        return newlocation;
    }

    public void setNewlocation(Point newlocation) {
        this.newlocation = newlocation;
    }

    public int getDistfromstepstart() {
        return distfromstepstart;
    }

    public void setDistfromstepstart(int distfromstepstart) {
        this.distfromstepstart = distfromstepstart;
    }
}
