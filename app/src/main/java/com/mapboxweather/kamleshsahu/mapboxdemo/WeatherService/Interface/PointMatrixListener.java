package com.mapboxweather.kamleshsahu.mapboxdemo.WeatherService.Interface;

import com.mapboxweather.kamleshsahu.mapboxdemo.WeatherService.Models.mPoint;

import java.util.Map;

/**
 * Created by k on 4/18/2019.
 */

public interface PointMatrixListener extends mError{

    void OnPointMatrixCalculated(int id);
}
