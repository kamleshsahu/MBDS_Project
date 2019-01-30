package com.mapboxweather.kamleshsahu.mapboxdemo.Methods;

import com.mapbox.api.directions.v5.DirectionsCriteria;
import com.mapbox.api.directions.v5.MapboxDirections;
import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.geojson.Point;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.mapboxweather.kamleshsahu.mapboxdemo.Constants.MapboxKey;

public class RouteFinder {

    private Point sp,dp;
    private Callback<DirectionsResponse> responseCallback;
    private String travelmode;
    private String avoid;

    public RouteFinder(Point sp,Point dp,String travelmode,String avoid,Callback<DirectionsResponse> responseCallback) {
        this.sp=sp;
        this.dp=dp;
        this.travelmode=travelmode;
        this.responseCallback=responseCallback;
        this.avoid=avoid;
    }



    public void find(){
        MapboxDirections client = MapboxDirections.builder()
                .accessToken(MapboxKey)
                .origin(sp)
                .destination(dp)
                .overview(DirectionsCriteria.OVERVIEW_FULL)
                .profile(travelmode)
                .exclude(avoid)
                .steps(true)
                .alternatives(true)
                .build();



        client.enqueueCall(responseCallback);
    }

    public static void main(String... args){
        Point sp=Point.fromLngLat(-105.2705, 40.015);
        Point dp=Point.fromLngLat(-104.9653, 39.7348);
        String avoid=DirectionsCriteria.EXCLUDE_TOLL;
        String travelmode=DirectionsCriteria.PROFILE_CYCLING;
       new RouteFinder(sp, dp, travelmode,avoid, new Callback<DirectionsResponse>() {
           @Override
           public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
               System.out.println("response");
               System.out.println(response.body());
           }

           @Override
           public void onFailure(Call<DirectionsResponse> call, Throwable t) {
               t.printStackTrace();
           }
       }).find();

    }
}
