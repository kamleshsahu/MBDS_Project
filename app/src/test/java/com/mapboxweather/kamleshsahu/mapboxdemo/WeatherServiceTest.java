package com.mapboxweather.kamleshsahu.mapboxdemo;

import com.mapbox.api.directions.v5.DirectionsCriteria;
import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.api.directions.v5.models.DirectionsRoute;
import com.mapbox.geojson.Point;
import com.mapboxweather.kamleshsahu.mapboxdemo.Methods.RouteFinder;
import com.mapboxweather.kamleshsahu.mapboxdemo.WeatherService.Methods.IntermediatePoints;
import com.mapboxweather.kamleshsahu.mapboxdemo.WeatherService.Models.mStep;
import com.mapboxweather.kamleshsahu.mapboxdemo.WeatherService.WeatherService;

import junit.framework.TestCase;
import junit.framework.TestResult;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runners.JUnit4;

import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

/**
 * Created by k on 4/5/2019.
 */

public class WeatherServiceTest {

    public static void main(String[] args) {
        Point sp=Point.fromLngLat(-105.2705, 40.015);
        Point dp=Point.fromLngLat(-104.9653, 39.7348);
        String profile= DirectionsCriteria.PROFILE_DRIVING;
        //       jstarttime=System.currentTimeMillis();
        Calendar calendar=Calendar.getInstance();

        String timezoneid=calendar.getTimeZone().getID();
        long jstarttime=calendar.getTimeInMillis();
        String travelmode= DirectionsCriteria.PROFILE_DRIVING;

        long interval=50000;

        RouteFinder rf=new RouteFinder(sp, dp, profile, "", new Callback<DirectionsResponse>() {
            @Override
            public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
                System.out.println(response.body());
                DirectionsRoute routedata=response.body().routes().get(0);


//                WeatherService service;
//                service = new WeatherService(routedata,timezoneid,interval,jstarttime,travelmode);
//                List<mStep> list=service.calc_data();

                IntermediatePoints fn= new IntermediatePoints(interval,routedata,timezoneid,jstarttime,travelmode);
                List<mStep> msteps= fn.extractListofPoints();





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
