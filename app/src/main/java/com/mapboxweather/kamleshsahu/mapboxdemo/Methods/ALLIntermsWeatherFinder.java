package com.mapboxweather.kamleshsahu.mapboxdemo.Methods;


import android.os.Message;

import com.google.gson.Gson;
import com.mapbox.api.matrix.v1.models.MatrixResponse;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapboxweather.kamleshsahu.mapboxdemo.Activity.SimpleMapViewActivity;
import com.mapboxweather.kamleshsahu.mapboxdemo.Models.Darkskyapi;
import com.mapboxweather.kamleshsahu.mapboxdemo.Models.Darkskyapi2;
import com.mapboxweather.kamleshsahu.mapboxdemo.Models.Item;
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



     ALLIntermsWeatherFinder(Point origin, List<Point> interms,String travelmode, String timezoneid, long jstarttime, long aft_duration) {
      this.origin=origin;
      this.jstarttime=jstarttime;
      this.timezoneid=timezoneid;
      this.aft_duration=aft_duration;
      this.interms=interms;
      this.travelmode=travelmode;
     }


    public void call()  {


        dep_time_millis = jstarttime + aft_duration*1000;
        try {
               DistMatrixFinder distMatrix = new DistMatrixFinder(origin, interms,travelmode, dep_time_millis,distMatrixCallback);
               distMatrix.getMatrix();

          }catch(Exception e){
                   e.printStackTrace();
          }


    }

   private retrofit2.Callback<MatrixResponse> distMatrixCallback =new retrofit2.Callback<MatrixResponse>() {
        @Override
        public void onResponse(Call<MatrixResponse> call, Response<MatrixResponse> response) {


            if(response.isSuccessful()) {
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
                    new WeatherFinder(FinalK, interms.get(FinalK), distanceMatrix, time).fetchWeather();


                    //  return new Item(point, resp.getCurrently(), stn_arrtime, distanceMatrix.rows[0].elements[k].DistanceCalculator.humanReadable, distanceMatrix.destinationAddresses[k].toString());
                }
            }else {
                Message message = new Message();
                message.obj = new Resp(new mError(ErrorHead_IntermFunction,response.message()));
                SimpleMapViewActivity.myItemhandler.sendMessage(message);

            }
        }

        @Override
        public void onFailure(Call<MatrixResponse> call, Throwable t) {
            t.printStackTrace();
            Message message = new Message();
            message.obj = new Resp(new mError(ErrorHead_IntermFunction,t.getMessage()));
            SimpleMapViewActivity.myItemhandler.sendMessage(message);
        }
    };




}
