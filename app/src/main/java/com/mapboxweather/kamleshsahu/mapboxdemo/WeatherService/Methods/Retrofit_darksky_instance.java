package com.mapboxweather.kamleshsahu.mapboxdemo.WeatherService.Methods;

import com.mapboxweather.kamleshsahu.mapboxdemo.Interface.ApiInterface;
import com.mapboxweather.kamleshsahu.mapboxdemo.WeatherService.Interface.DSWeatherService;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.mapboxweather.kamleshsahu.mapboxdemo.WeatherService.Constants.DarkSky_BaseURL;

/**
 * Created by k on 3/27/2019.
 */

public class Retrofit_darksky_instance {
  
    public static Retrofit retrofit;
    public Retrofit_darksky_instance() {
        super();

    }

    public static DSWeatherService getService(){

        if(retrofit==null) {
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .readTimeout(60, TimeUnit.SECONDS)
                    .writeTimeout(60, TimeUnit.SECONDS)
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(DarkSky_BaseURL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
           }

           return retrofit.create(DSWeatherService.class);

    }


}
