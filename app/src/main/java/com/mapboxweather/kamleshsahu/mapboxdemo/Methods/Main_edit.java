package com.mapboxweather.kamleshsahu.mapboxdemo.Methods;

import com.mapbox.api.directions.v5.DirectionsCriteria;
import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.api.directions.v5.models.DirectionsRoute;
import com.mapbox.geojson.Point;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.mapboxweather.kamleshsahu.mapboxdemo.Methods.myStopwatch.startTimer;
import static com.mapboxweather.kamleshsahu.mapboxdemo.Methods.myStopwatch.stopTimer;


public class Main_edit {



   static private DirectionsRoute routedata=null;
   static private String timezoneid;
   static private long interval=50000;
   static private long jstarttime;
   static private String travelmode;
   


    public static void main(String... args){

        Point sp=Point.fromLngLat(-105.2705, 40.015);
        Point dp=Point.fromLngLat(-104.9653, 39.7348);
        String profile=DirectionsCriteria.PROFILE_DRIVING;
 //       jstarttime=System.currentTimeMillis();
        Calendar calendar=Calendar.getInstance();

        timezoneid=calendar.getTimeZone().getID();
        new RouteFinder(sp, dp, profile, "", new Callback<DirectionsResponse>() {
            @Override
            public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
                System.out.println(response.body());
                routedata=response.body().routes().get(0);
                startTimer();
                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        stopTimer();
                    }
                }).start();


            }

            @Override
            public void onFailure(Call<DirectionsResponse> call, Throwable t) {

            }
        }).find();
    }

    public Main_edit() {

    }




}
