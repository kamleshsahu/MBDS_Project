package com.mapboxweather.kamleshsahu.mapboxdemo.WeatherService.Methods;


import com.mapbox.geojson.Point;
import com.mapboxweather.kamleshsahu.mapboxdemo.WeatherService.Models.Darkskyapi;

import java.util.Calendar;

import retrofit2.Call;

import static com.mapboxweather.kamleshsahu.mapboxdemo.Constants.DarkskyKey;
import static com.mapboxweather.kamleshsahu.mapboxdemo.WeatherService.Methods.Retrofit_darksky_instance.getApiServiceInstance;


public class WeatherFinder {

    double lat;
    double lng;
    String time;


    public WeatherFinder(double lat, double lng, String time) {
          this.lat=lat;
          this.lng=lng;
          this.time=time;
    }


    public static void main(String... args){


        Point point=Point.fromLngLat(-105.2705,40.015);

        String time= TimeFormatter.getSDFtime(System.currentTimeMillis(), Calendar.getInstance().getTimeZone().getID());

        Darkskyapi darkskyapi=new WeatherFinder(point.latitude(),point.longitude(),time).calcWeather();

    }


    Darkskyapi calcWeather(){


        String llt=lat+","+lng+","+time;

        Call<Darkskyapi> call = getApiServiceInstance().getweather(DarkskyKey,llt);


        try {
            return   call.execute().body();
        }catch (Exception error){
            error.printStackTrace();
            return null;
        }


     }

}
