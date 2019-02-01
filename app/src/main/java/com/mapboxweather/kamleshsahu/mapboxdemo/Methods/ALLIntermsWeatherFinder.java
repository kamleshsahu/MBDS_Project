package com.mapboxweather.kamleshsahu.mapboxdemo.Methods;


import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.google.gson.Gson;
import com.mapbox.api.directions.v5.DirectionsCriteria;
import com.mapbox.api.matrix.v1.MapboxMatrix;
import com.mapbox.api.matrix.v1.models.MatrixResponse;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapboxweather.kamleshsahu.mapboxdemo.Activity.SimpleMapViewActivity;
import com.mapboxweather.kamleshsahu.mapboxdemo.Models.Darkskyapi;
import com.mapboxweather.kamleshsahu.mapboxdemo.Models.Darkskyapi2;
import com.mapboxweather.kamleshsahu.mapboxdemo.Models.Item;
import com.mapboxweather.kamleshsahu.mapboxdemo.Models.MyDistMatrixResponse;
import com.mapboxweather.kamleshsahu.mapboxdemo.Models.Resp;
import com.mapboxweather.kamleshsahu.mapboxdemo.Models.mError;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;


import retrofit2.Call;
import retrofit2.Response;

import static com.mapboxweather.kamleshsahu.mapboxdemo.Constants.ErrorHead_IntermFunction;
import static com.mapboxweather.kamleshsahu.mapboxdemo.Constants.ErrorHead_STEP;
import static com.mapboxweather.kamleshsahu.mapboxdemo.Constants.MapboxKey;

import static com.mapboxweather.kamleshsahu.mapboxdemo.Constants.month;
import static com.mapboxweather.kamleshsahu.mapboxdemo.Constants.strDays;


public class ALLIntermsWeatherFinder  {
    private Point origin=null;
    private long jstarttime;
    private String timezoneid;
    private long aft_duration;
    private List<Point> interms;
    MatrixResponse distanceMatrix;
    private long dep_time_millis;
    private String travelmode;
    private List<List<Point>> intermsInParts=new ArrayList<>();


     ALLIntermsWeatherFinder(Point origin, List<Point> interms,String travelmode, String timezoneid, long jstarttime, long aft_duration) {
      this.origin=origin;
      this.jstarttime=jstarttime;
      this.timezoneid=timezoneid;
      this.aft_duration=aft_duration;
      this.interms=interms;
      this.travelmode=travelmode;

     }


    public void call() {

        dep_time_millis = jstarttime + aft_duration * 1000;


        int max_Api_Count=getMax_API_Count(travelmode);
        for (int from = 0; from < interms.size(); from += max_Api_Count) {
            int to = from + max_Api_Count;
            if (to > interms.size())
                to = interms.size();
            intermsInParts.add(new ArrayList<>(interms.subList(from, to)));
        }

        for (int i = 0; i < intermsInParts.size(); i++) {

            int finalI = i;



            Integer arr[] = new Integer[intermsInParts.get(finalI).size()];
            for (int k = 0; k < intermsInParts.get(finalI).size(); k++) {
                arr[k] = k + 1;
            }

            MapboxMatrix matrixcall = MapboxMatrix.builder()
                    .accessToken(MapboxKey)
                    .profile(travelmode)
                    .coordinate(origin)
                    .coordinates(intermsInParts.get(finalI))
                    .sources(0)
                    .destinations(arr)
                    .build();



            matrixcall.enqueueCall(new retrofit2.Callback<MatrixResponse>() {
                @Override
                public void onResponse(Call<MatrixResponse> call, Response<MatrixResponse> response) {


                    if (response.isSuccessful()) {
                        System.out.println(response.code());
                        System.out.println(response.raw());
                        distanceMatrix = response.body();
                        for (int k = 0; k < distanceMatrix.destinations().size(); k++) {


                            long duration = distanceMatrix.durations().get(0)[k].intValue();

                            long arrival_time_millis = dep_time_millis + duration * 1000;
                            final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
                            //  System.out.println("timezone id :"+timezoneid);
                            //   if(timezoneid!=null)

                            sdf.setTimeZone(TimeZone.getTimeZone(timezoneid));
                            String time = sdf.format(arrival_time_millis);
                            final int FinalK = k;


                            new WeatherFinder(FinalK, intermsInParts.get(finalI).get(FinalK), distanceMatrix, time).fetchWeather();

                            }
                    } else {
                        Log.e("error","request not successful,matrixcall enque");
                        Log.e("error body ",new Gson().toJson(response.raw()));
                        Log.e("error url :",response.raw().request().url().toString());
                        Message message = new Message();
                        message.obj = new Resp(new mError(ErrorHead_IntermFunction, response.message()));
                        SimpleMapViewActivity.myItemhandler.sendMessage(message);

                    }
                }

                @Override
                public void onFailure(Call<MatrixResponse> call, Throwable t) {

                    Log.e("error","onfailture,matrixcall enque");
                    t.printStackTrace();
                    Message message = new Message();
                    message.obj = new Resp(new mError(ErrorHead_IntermFunction, t.getMessage()));
                    SimpleMapViewActivity.myItemhandler.sendMessage(message);
                }
            });

           }

    }

    int getMax_API_Count(String travelmode){

         switch (travelmode){
             case DirectionsCriteria.PROFILE_DRIVING_TRAFFIC :return 9;
             case DirectionsCriteria.PROFILE_DRIVING :return 24;
             case DirectionsCriteria.PROFILE_CYCLING :return 24;
             case DirectionsCriteria.PROFILE_WALKING :return 24;

             default:return 9;
         }

    }




}
