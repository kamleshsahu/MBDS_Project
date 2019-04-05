package com.mapboxweather.kamleshsahu.mapboxdemo.WeatherService;

import com.mapbox.api.directions.v5.DirectionsCriteria;
import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.api.directions.v5.models.DirectionsRoute;
import com.mapbox.geojson.Point;
import com.mapboxweather.kamleshsahu.mapboxdemo.Methods.RouteFinder;
import com.mapboxweather.kamleshsahu.mapboxdemo.WeatherService.Models.mStep;
import com.mapboxweather.kamleshsahu.mapboxdemo.WeatherService.Methods.IntermediatePoints;
import com.mapboxweather.kamleshsahu.mapboxdemo.WeatherService.Methods.PointMatrixForAll;
import com.mapboxweather.kamleshsahu.mapboxdemo.WeatherService.Methods.WeatherForAllPoints;

import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class WeatherService {



    private DirectionsRoute routedata=null;
    private String timezoneid;
    private long interval=50000;
    private long jstarttime;
    private String travelmode;


    public WeatherService(DirectionsRoute routedata, String timezoneid, long interval, long jstarttime, String travelmode) {
        this.routedata = routedata;
        this.timezoneid = timezoneid;
        this.interval = interval;
        this.jstarttime = jstarttime;
        this.travelmode = travelmode;
    }


   public List<mStep> calc_data(){

       IntermediatePoints fn= new IntermediatePoints(interval,routedata,timezoneid,jstarttime,travelmode);
       List<mStep> msteps= fn.extractListofPoints();

       PointMatrixForAll pointMatrixs=new PointMatrixForAll(msteps,travelmode);
       pointMatrixs.calc();

       WeatherForAllPoints weatherForAllPoints=new WeatherForAllPoints(msteps);
       weatherForAllPoints.calcWeather();

        return msteps;
    }
    public static void main(String[] args) {
        Point sp=Point.fromLngLat(-105.2705, 40.015);
        Point dp=Point.fromLngLat(-104.9653, 39.7348);
        String profile= DirectionsCriteria.PROFILE_DRIVING;
        //       jstarttime=System.currentTimeMillis();
        Calendar calendar=Calendar.getInstance();

        String timezoneid=calendar.getTimeZone().getID();
        long jstarttime=calendar.getTimeInMillis();
        String travelmode= DirectionsCriteria.PROFILE_DRIVING;

        long interval=10000;


        RouteFinder rf=new RouteFinder(sp, dp, profile, "", new Callback<DirectionsResponse>() {
            @Override
            public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
                System.out.println(response.body());
                DirectionsRoute routedata=response.body().routes().get(0);


                WeatherService service;
                service = new WeatherService(routedata,timezoneid,interval,jstarttime,travelmode);
                List<mStep> list=service.calc_data();

//                IntermediatePoints fn= new IntermediatePoints(interval,routedata,timezoneid,jstarttime,travelmode);
//                List<mStep> msteps= fn.extractListofPoints();
//
//                System.out.println(msteps);
//
//                PointMatrixForAll pointMatrixs=new PointMatrixForAll(msteps,travelmode);
//                pointMatrixs.calc();
//
//                System.out.println(msteps);
//
//                WeatherForAllPoints weatherForAllPoints=new WeatherForAllPoints(msteps);
//                weatherForAllPoints.calcWeather();
//
//                System.out.println(msteps);

                System.out.println(list);
            }

            @Override
            public void onFailure(Call<DirectionsResponse> call, Throwable t) {
                t.printStackTrace();
                System.out.println(t.getMessage());
            }
        });
        rf.find();
    }
    
}
