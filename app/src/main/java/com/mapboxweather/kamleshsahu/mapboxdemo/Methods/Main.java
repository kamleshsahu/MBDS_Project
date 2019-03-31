package com.mapboxweather.kamleshsahu.mapboxdemo.Methods;

import android.os.Message;

import com.mapbox.api.directions.v5.DirectionsCriteria;
import com.mapbox.api.directions.v5.models.DirectionsRoute;
import com.mapbox.api.directions.v5.models.LegStep;
import com.mapbox.core.constants.Constants;
import com.mapbox.geojson.LineString;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapboxweather.kamleshsahu.mapboxdemo.Activity.SimpleMapViewActivity;
import com.mapboxweather.kamleshsahu.mapboxdemo.Interface.ApiInterface;
import com.mapboxweather.kamleshsahu.mapboxdemo.Models.Resp;
import com.mapboxweather.kamleshsahu.mapboxdemo.Models.mError;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.mapboxweather.kamleshsahu.mapboxdemo.Activity.SimpleMapViewActivity.progress;
import static com.mapboxweather.kamleshsahu.mapboxdemo.Constants.DarkSky_BaseURL;
import static com.mapboxweather.kamleshsahu.mapboxdemo.Constants.ErrorHead_MainFunction;


public class Main {


    private DirectionsRoute routedata=null;
    private String timezoneid;
    private long interval=50000;
    private long jstarttime;
    private String travelmode;
    public static ApiInterface apiService;


    public static void main(String... args){

        Point sp=Point.fromLngLat(-105.2705, 40.015);
        Point dp=Point.fromLngLat(-104.9653, 39.7348);
        String profile=DirectionsCriteria.PROFILE_DRIVING;
 //       jstarttime=System.currentTimeMillis();
        Calendar calendar=Calendar.getInstance();

 //       timezoneid=calendar.getTimeZone().getID();
//        new RouteFinder(sp, dp, profile, new Callback<DirectionsResponse>() {
//            @Override
//            public void onResponse(Call<DirectionsResponse> call, Resp<DirectionsResponse> response) {
//                //System.out.println("response");
//                //System.out.println(response.body());
//
//
//                new Main(response.body().routes().get(0)).execute();
//            }
//
//            @Override
//            public void onFailure(Call<DirectionsResponse> call, Throwable t) {
//                t.printStackTrace();
//            }
//        }).find();


    }

    public Main(DirectionsRoute routedata,String travelmode,String timezoneid,long jstarttime,long interval) {

        this.routedata=routedata;
        this.interval=interval;
        this.timezoneid=timezoneid;
        this.jstarttime=jstarttime;
        this.travelmode=travelmode;

    }

    public void execute(){
        try {

            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .readTimeout(60, TimeUnit.SECONDS)
                    .writeTimeout(60, TimeUnit.SECONDS)
                    .connectTimeout(60, TimeUnit.SECONDS)
                    //.addInterceptor(loggingInterceptor)
                    //.addNetworkInterceptor(networkInterceptor)
                    .build();
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(DarkSky_BaseURL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            apiService = retrofit.create(ApiInterface.class);


            List<LegStep> steps = routedata.legs().get(0).steps();

            long aft_duration = 0;
            long aft_distance = 0;

            int totalsteps=steps.size();
            for (int k = 0; k < steps.size(); k++) {
                if (k == 0) {
                    aft_duration = 0;
                    aft_distance = 0;
                } else {
                    aft_distance += steps.get(k - 1).distance();
                    aft_duration += steps.get(k - 1).duration();
                }

                new stepapiscaller(k, jstarttime, aft_duration, aft_distance, timezoneid, steps.get(k)).call();
                if(progress!=null) progress.setProgress((int)(90/totalsteps)* k);
                List<LatLng> points = new ArrayList<>();
                List<Point> coords = LineString.fromPolyline(steps.get(k).geometry(), Constants.PRECISION_6).coordinates();


                for (Point point : coords) {
                    points.add(new LatLng(point.latitude(), point.longitude()));
                }


                int next = (int) interval;
                int dist = 0;
                int olddist = 0;
                List<Point> interms = new ArrayList<>();
                for (int i = 20; i < points.size(); i += 20) {
                    olddist = dist;
                    dist += new DistanceCalculator().distance(points.get(i).getLatitude(), points.get(i - 20).getLatitude(), points.get(i).getLongitude(), points.get(i - 20).getLongitude(), 0, 0);
                    //       //System.out.println(olddist+" --> "+dist);
                    while (dist > next) {
                        LatLng p1 = points.get(i - 20);
                        LatLng p2 = points.get(i);
                        int m = (next - olddist) / (dist - olddist);
                        LatLng currpos = new LatLng(p1.getLatitude() + (p2.getLatitude() - p1.getLatitude()) * m, p1.getLongitude() + (p2.getLongitude() - p1.getLongitude()) * m);
                        interms.add(Point.fromLngLat(currpos.getLongitude(), currpos.getLatitude()));
                        //           //System.out.println("interm added to list");
                        next += (int) interval;
                    }
                }

                if (interms.size() > 0) {
                    new WeatherFinderforPoints(steps.get(k).maneuver().location(), interms, travelmode, timezoneid, jstarttime, aft_duration).call();
                }
            }

        }catch (Exception e){
            e.printStackTrace();
            Message message = new Message();
            message.obj = new Resp(new mError(ErrorHead_MainFunction,e.getMessage()));
            SimpleMapViewActivity.myStephandler.sendMessage(message);

        }

    }


}
