package com.mapboxweather.kamleshsahu.mapboxdemo.WeatherService.Methods;

import com.mapboxweather.kamleshsahu.mapboxdemo.WeatherService.Models.mPoint;
import com.mapboxweather.kamleshsahu.mapboxdemo.WeatherService.Models.mStep;

import java.util.List;

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

            mstep.setWeatherdata(wfs.calcWeather().getCurrently());


            List<mPoint> mpointList=mstep.getInterms();

            for(int j=0;j<mpointList.size();j++){
                mPoint mpoint=mpointList.get(j);
                   WeatherFinder wf=new WeatherFinder(
                           mpoint.getPoint().latitude(),
                           mpoint.getPoint().longitude(),
                           mpoint.getDs_arr_time()
                           );
                  mpoint.setWeather_data(wf.calcWeather().getCurrently());
            }

        }


    }
}
