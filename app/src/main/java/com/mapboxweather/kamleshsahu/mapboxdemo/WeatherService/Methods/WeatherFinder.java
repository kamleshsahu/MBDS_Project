package com.mapboxweather.kamleshsahu.mapboxdemo.WeatherService.Methods;


import com.mapbox.geojson.Point;
import com.mapboxweather.kamleshsahu.mapboxdemo.WeatherService.Interface.ApiInterface;
import com.mapboxweather.kamleshsahu.mapboxdemo.WeatherService.Interface.WeatherofPointListener;
import com.mapboxweather.kamleshsahu.mapboxdemo.WeatherService.Models.Darkskyapi;

import java.util.Calendar;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.mapboxweather.kamleshsahu.mapboxdemo.Constants.DarkskyKey;
import static com.mapboxweather.kamleshsahu.mapboxdemo.WeatherService.Methods.Retrofit_darksky_instance.getRetrofitInstance;


public class WeatherFinder {



    WeatherofPointListener weatherListener;

    public WeatherFinder(WeatherofPointListener weatherListener) {
        this.weatherListener = weatherListener;
    }
    public void setWeatherListener(WeatherofPointListener weatherListener) {
        this.weatherListener = weatherListener;
    }

//    public static void main(String... args){
//
//
//        Point point=Point.fromLngLat(-105.2705,40.015);
//
//        String time= TimeFormatter.getSDFtime(System.currentTimeMillis(), Calendar.getInstance().getTimeZone().getID());
//
//        Darkskyapi darkskyapi=new WeatherFinder(point.latitude(),point.longitude(),time).calcWeather();
//
//    }




   public void calcWeather(int id,double lat, double lng, String time){


        String llt=lat+","+lng+","+time;
        Retrofit retrofit=getRetrofitInstance();
        retrofit.newBuilder().callFactory(new okhttp3.Call.Factory() {
            @Override
            public okhttp3.Call newCall(Request request) {
                request = request.newBuilder().tag(new Integer[]{null}).build();

                okhttp3.Call call =newCall(request);

                // We set the element to the call, to (at least) keep some consistency
                // If you want to only have Strings, create a String array and put the default value to null;
                ((Integer[])request.tag())[0] = id;

                return call;
            }
        });
        ApiInterface apiService = retrofit.create(ApiInterface.class);
        Call<Darkskyapi> call = apiService.getweather(DarkskyKey,llt);

        call.enqueue(listener);

     }

     Callback<Darkskyapi> listener=new Callback<Darkskyapi>() {
         @Override
         public void onResponse(Call<Darkskyapi> call, Response<Darkskyapi> response) {

             int id=((Integer[])call.request().tag())[0];
             if(response.isSuccessful()){

             }

         }

         @Override
         public void onFailure(Call<Darkskyapi> call, Throwable t) {

         }
     };

}
