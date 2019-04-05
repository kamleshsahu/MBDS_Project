package com.mapboxweather.kamleshsahu.mapboxdemo.WeatherService.Methods;

import com.mapboxweather.kamleshsahu.mapboxdemo.WeatherService.Models.Darkskyapi;
import com.mapboxweather.kamleshsahu.mapboxdemo.WeatherService.Models.mPoint;
import com.mapboxweather.kamleshsahu.mapboxdemo.WeatherService.Models.mStep;

import java.util.List;

import static com.mapboxweather.kamleshsahu.mapboxdemo.WeatherService.Methods.TimeFormatter.formatTimeforDisp;
import static com.mapboxweather.kamleshsahu.mapboxdemo.WeatherService.Methods.TimeFormatter.getSDFtime;

/**
 * Created by k on 4/4/2019.
 */

public class WeatherForAllPoints {

    List<mStep> mStepList;

    public WeatherForAllPoints(List<mStep> mStepList) {
        this.mStepList = mStepList;
    }

    public void calcWeather(){

        for(int i=0;i<mStepList.size();i++){

            mStep mstep=mStepList.get(i);

            WeatherFinder wfs=new WeatherFinder(
                    mstep.getStep_StartPoint().latitude(),
                    mstep.getStep_StartPoint().longitude(),
                    mstep.getSDFTime()
            );

            Darkskyapi dsw= wfs.calcWeather();
            mstep.setWeatherdata(dsw.getCurrently());
            mstep.setDisplay_arrtime(formatTimeforDisp(dsw.getCurrently()
                    .getTime(),dsw.getTimezone()));


            List<mPoint> mpointList=mstep.getInterms();
            if(mpointList!=null && mpointList.size()>0) {
                for (int j = 0; j < mpointList.size(); j++) {
                    mPoint mpoint = mpointList.get(j);
                    WeatherFinder wf = new WeatherFinder(
                            mpoint.getPoint().latitude(),
                            mpoint.getPoint().longitude(),
                            mpoint.getDs_arr_time()
                    );

                Darkskyapi p_dsw= wf.calcWeather();
                mpoint.setWeather_data(p_dsw.getCurrently());
                mpoint.setDisplay_arrtime(formatTimeforDisp(p_dsw.getCurrently().getTime(),p_dsw.getTimezone()));
               }
            }
        }


    }
}
