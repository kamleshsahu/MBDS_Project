package com.mapboxweather.kamleshsahu.mapboxdemo.WeatherService.Interface;

import com.mapboxweather.kamleshsahu.mapboxdemo.WeatherService.Models.Darkskyapi;

/**
 * Created by k on 4/18/2019.
 */

public interface WeatherofPointListener extends mError{

    void OnWeatherFetched(int id, Darkskyapi darksky_response);
}
