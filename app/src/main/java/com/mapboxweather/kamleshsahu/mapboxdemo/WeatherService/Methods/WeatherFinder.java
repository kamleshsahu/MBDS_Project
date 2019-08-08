package com.mapboxweather.kamleshsahu.mapboxdemo.WeatherService.Methods;


import com.mapboxweather.kamleshsahu.mapboxdemo.WeatherService.Interface.DSWeatherService;
import com.mapboxweather.kamleshsahu.mapboxdemo.WeatherService.Interface.WeatherofPointListener;
import com.mapboxweather.kamleshsahu.mapboxdemo.WeatherService.Models.Darkskyapi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.mapboxweather.kamleshsahu.mapboxdemo.WeatherService.Constants.DarkskyKey;
import static com.mapboxweather.kamleshsahu.mapboxdemo.WeatherService.Methods.Retrofit_darksky_instance.getService;


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


        DSWeatherService apiService = getService();

        Call<Darkskyapi> call = apiService.getweather(DarkskyKey,llt,id+"");

        call.enqueue(listener);

     }



     Callback<Darkskyapi> listener=new Callback<Darkskyapi>() {
         @Override
         public void onResponse(Call<Darkskyapi> call, Response<Darkskyapi> response) {

             int id=Integer.parseInt(response.raw().request().headers().get("token"));
             if(response.isSuccessful()){
                if(weatherListener!=null)
                 weatherListener.OnWeatherFetched(id,response.body());
             }

         }

         @Override
         public void onFailure(Call<Darkskyapi> call, Throwable t) {
             if(weatherListener!=null)
               weatherListener.onError("weather error",t.getLocalizedMessage());
         }
     };

}
