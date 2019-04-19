package com.mapboxweather.kamleshsahu.mapboxdemo.WeatherService.Interface;

import com.mapboxweather.kamleshsahu.mapboxdemo.WeatherService.Models.mStep;

import java.util.Map;

/**
 * Created by k on 4/19/2019.
 */

public interface WeatherServiceListener extends mError{

    void OnWeatherDataListReady(Map<Integer,mStep> msteps);

//    void onWeatherOfPointReady();
//
//    void onWeatherOfStepReady(int step_id);

    void onWeatherDataListProgressChange(int progress);
}
