package com.mapboxweather.kamleshsahu.mapboxdemo.WeatherService.Methods;

import com.mapboxweather.kamleshsahu.mapboxdemo.WeatherService.Models.mPoint;
import com.mapboxweather.kamleshsahu.mapboxdemo.WeatherService.Models.mStep;

import java.util.List;

/**
 * Created by k on 4/4/2019.
 */

public class PointMatrixForAll {
    List<mStep> msteps;

    String travelmode;

    public PointMatrixForAll(List<mStep> mSteps,String travelmode) {
        this.msteps = mSteps;
        this.travelmode=travelmode;
    }

    public void calc(){
        for(int i=0;i<msteps.size();i++){
            List<mPoint> mpoints=msteps.get(i).getInterms();
            String timezoneid=msteps.get(i).getTimezoneid();
            long jstarttime=msteps.get(i).getJstarttime();

            PointMatrix pointMatrix=new PointMatrix(
                    msteps.get(i).getStep_StartPoint(),
                    mpoints,
                    travelmode,
                    timezoneid,
                    jstarttime,
                    msteps.get(i).getAft_duration()
            );
            pointMatrix.calc();
        }
    }
}
