package com.mapboxweather.kamleshsahu.mapboxdemo;

import com.mapbox.api.directions.v5.DirectionsCriteria;
import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.api.directions.v5.models.DirectionsRoute;
import com.mapbox.geojson.Point;
import com.mapboxweather.kamleshsahu.mapboxdemo.Methods.RouteFinder;
import com.mapboxweather.kamleshsahu.mapboxdemo.WeatherService.Methods.IntermediatePoints;
import com.mapboxweather.kamleshsahu.mapboxdemo.WeatherService.Models.mStep;

import org.junit.Test;

import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
     //   assertEquals(4, 2 + 2);

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