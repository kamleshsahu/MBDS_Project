package com.mapboxweather.kamleshsahu.mapboxdemo.WeatherService_Navigation.Interface;



import com.mapboxweather.kamleshsahu.mapboxdemo.WeatherService_Navigation.Models.Darkskyapi;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Path;

public interface ApiInterface {
    
    @Headers({"Content-Type: application/json",
            "Accept: application/json"})


    @GET("forecast/{DarkskyKey}/{llt}?exclude=hourly,daily,minutely,flags")
    Call<Darkskyapi> getweather(@Path("DarkskyKey") String DarkskyKey, @Path("llt") String latlongtime, @Header("token") String value);


//
//    @GET("maps/api/timezone/json")
//    Call<TimeZoneApiResponse> getTimezone(@Query("location") String location, @Query("timestamp") long timestamp, @Query("key") String GoogleKey);


}
