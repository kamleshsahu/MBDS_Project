package com.mapboxweather.kamleshsahu.mapboxdemo.Interface;



import com.mapboxweather.kamleshsahu.mapboxdemo.Models.Darkskyapi2;
import com.mapboxweather.kamleshsahu.mapboxdemo.Models.Darkskyapi;
import com.mapboxweather.kamleshsahu.mapboxdemo.Models.TimeZoneApiResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {
    
    @Headers({"Content-Type: application/json",
            "Accept: application/json"})


    @GET("forecast/{DarkskyKey}/{llt}?exclude=hourly,daily,minutely,flags")
    Call<Darkskyapi> getweather(@Path("DarkskyKey") String DarkskyKey,@Path("llt") String latlongtime);

    @GET("forecast/{DarkskyKey}/{llt}?exclude=hourly,daily,minutely,flags")
    Call<Darkskyapi2> getweather2(@Path("DarkskyKey") String DarkskyKey, @Path("llt") String latlongtime);

    @GET("maps/api/timezone/json")
    Call<TimeZoneApiResponse> getTimezone (@Query("location") String location,@Query("timestamp") long timestamp,@Query("key") String GoogleKey);
}
