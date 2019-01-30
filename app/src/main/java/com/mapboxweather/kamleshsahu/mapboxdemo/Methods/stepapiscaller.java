package com.mapboxweather.kamleshsahu.mapboxdemo.Methods;

import com.mapbox.api.directions.v5.models.LegStep;
import java.text.SimpleDateFormat;
import java.util.TimeZone;


public class stepapiscaller  {

    private long jstarttime;

    private long aft_duration;
    private long aft_distance;
    private LegStep step;
    private String timezoneid;
    private int pos;



    public stepapiscaller(int pos,long jstarttime, long aft_duration, long aft_distance, String timezoneid, LegStep step) {
   
        this.aft_distance=aft_distance;
        this.aft_duration=aft_duration;
        this.jstarttime=jstarttime;
        this.step=step;
        this.timezoneid=timezoneid;
        this.pos=pos;

    }

    void call(){

        long arrival_time_millis = jstarttime + aft_duration*1000 ;
         
         final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
         sdf.setTimeZone(TimeZone.getTimeZone(timezoneid));
         String time=sdf.format(arrival_time_millis);

	     new WeatherFinder(pos,step.maneuver().location(),time,step,aft_distance).fetchWeather2();

        }


}
