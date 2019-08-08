package com.mapboxweather.kamleshsahu.mapboxdemo.Methods;

import com.mapbox.geojson.Point;
import com.mapboxweather.kamleshsahu.mapboxdemo.Interface.ApiInterface;

import com.mapboxweather.kamleshsahu.mapboxdemo.Models.TimeZoneApiResponse;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.mapboxweather.kamleshsahu.mapboxdemo.WeatherService_Navigation.Constants.GoogleKey;


public class TimeZoneOfOrigin {

    Point startpoint;
    Callback<TimeZoneApiResponse> timeZoneResponseCallback;

    public TimeZoneOfOrigin(Point startpoint, Callback<TimeZoneApiResponse> timeZoneResponseCallback) {
        this.startpoint=startpoint;
        this.timeZoneResponseCallback=timeZoneResponseCallback;
    }

    public static void main(String... args){
        Point sp=Point.fromLngLat(-105.2705, 40.015);

        Callback<TimeZoneApiResponse> timeZoneApiResponseCallback=new Callback<TimeZoneApiResponse>() {
            @Override
            public void onResponse(Call<TimeZoneApiResponse> call, Response<TimeZoneApiResponse> response) {
                //System.out.println("response:");
                //System.out.println(response.raw());
                //System.out.println(response.code());
                //System.out.println(response.body().getTimeZoneId());
            }

            @Override
            public void onFailure(Call<TimeZoneApiResponse> call, Throwable t) {
                  t.printStackTrace();
            }
        };

        new TimeZoneOfOrigin(sp,timeZoneApiResponseCallback).getTimeZone();
    }

    public void getTimeZone(){

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .connectTimeout(30, TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://maps.googleapis.com/")
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        String latlong=startpoint.latitude()+","+startpoint.longitude();
        ApiInterface apiService = retrofit.create(ApiInterface.class);
        Call<TimeZoneApiResponse> call = apiService.getTimezone(latlong,System.currentTimeMillis()/1000,GoogleKey);

        call.enqueue(timeZoneResponseCallback);
    }



}
