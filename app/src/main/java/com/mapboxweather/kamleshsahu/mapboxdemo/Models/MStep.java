package com.mapboxweather.kamleshsahu.mapboxdemo.Models;


import com.mapbox.api.directions.v5.models.LegStep;

import java.time.Instant;
import java.util.List;

public class MStep {

    Currently wdata;
    String arrtime;
    double steplength;
    LegStep step;
    long aft_distance;

    Integer pos;


    public MStep() {
        super();
    }

    public MStep(Integer pos,LegStep step , Currently wdata, String arrtime, long aft_distance, double steplength) {
        this.step=step;
        this.wdata=wdata;
        this.arrtime=arrtime;
        this.steplength=steplength;
        this.aft_distance=aft_distance;
        this.pos=pos;
    }




    public String getArrtime() {
        return arrtime;
    }

    public double getSteplength() {
        return steplength;
    }

    public void setSteplength(double steplength) {
        this.steplength = steplength;
    }

    public Currently getWlist() {
        return wdata;
    }

    public LegStep getStep() {
        return step;
    }

    public void setStep(LegStep step) {
        this.step = step;
    }

    public long getAft_distance() {
        return aft_distance;
    }

    public void setAft_distance(long aft_distance) {
        this.aft_distance = aft_distance;
    }

    public Integer getPos() {
        return pos;
    }

    @Override
	public String toString() {
		// TODO Auto-generated method stub
		return "Arr :"+arrtime+", weather :"+wdata.getIcon()+", Aft Distance :"+aft_distance;
	}
    
    
    
}

