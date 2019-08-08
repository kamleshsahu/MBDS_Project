package com.mapboxweather.kamleshsahu.mapboxdemo.WeatherService_Navigation.Interface;


import com.mapboxweather.kamleshsahu.mapboxdemo.WeatherService_Navigation.Models.mStep;

import java.util.Map;

/**
 * Created by k on 4/19/2019.
 */

public interface IntermediatePointListener extends mError {

       void OnIntermediatePointsCalculated(Map<Integer, mStep> msteps);

}
