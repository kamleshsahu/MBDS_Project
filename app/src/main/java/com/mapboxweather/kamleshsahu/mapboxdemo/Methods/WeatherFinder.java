package com.mapboxweather.kamleshsahu.mapboxdemo.Methods;



import android.os.Message;
import android.util.Log;

import com.google.gson.Gson;
import com.mapbox.api.directions.v5.models.LegStep;
import com.mapbox.api.matrix.v1.models.MatrixResponse;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapboxweather.kamleshsahu.mapboxdemo.Activity.SimpleMapViewActivity;
import com.mapboxweather.kamleshsahu.mapboxdemo.Interface.ApiInterface;
import com.mapboxweather.kamleshsahu.mapboxdemo.Models.Darkskyapi;
import com.mapboxweather.kamleshsahu.mapboxdemo.Models.Darkskyapi2;
import com.mapboxweather.kamleshsahu.mapboxdemo.Models.Item;
import com.mapboxweather.kamleshsahu.mapboxdemo.Models.MStep;
import com.mapboxweather.kamleshsahu.mapboxdemo.Models.Resp;
import com.mapboxweather.kamleshsahu.mapboxdemo.Models.mError;


import java.util.Calendar;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.mapboxweather.kamleshsahu.mapboxdemo.Constants.DarkSky_BaseURL;
import static com.mapboxweather.kamleshsahu.mapboxdemo.Constants.DarkskyKey;
import static com.mapboxweather.kamleshsahu.mapboxdemo.Constants.ErrorHead_Weather;
import static com.mapboxweather.kamleshsahu.mapboxdemo.Constants.month;
import static com.mapboxweather.kamleshsahu.mapboxdemo.Constants.strDays;

public class WeatherFinder {

    double lat;
    double lng;
    String time;
    MatrixResponse matrixResponse;
    int pos=-1;
    LegStep step;
    long aft_distance;

    public WeatherFinder(int pos,Point location, String time,LegStep step,long after_distance) {
        this.lat=location.latitude();
        this.lng=location.longitude();
        this.time=time;
        this.step=step;
        this.pos=pos;
        this.aft_distance=after_distance;
    }

    public WeatherFinder(int pos, Point point, MatrixResponse matrixResponse,String time) {
        this.pos=pos;
        lat=point.latitude();
        lng=point.longitude();
        this.time=time;
        this.matrixResponse=matrixResponse;
    }


    public static void main(String... args){


        Point point=Point.fromLngLat(-105.2705,40.015);
        Callback<Darkskyapi2> darkskyapiCallback=new Callback<Darkskyapi2>() {
            @Override
            public void onResponse(Call<Darkskyapi2> call, Response<Darkskyapi2> response) {
                System.out.println("response:");
                System.out.println(response.code());
                System.out.println(response.raw());
                System.out.println(new Gson().toJson(response.body()));
            }

            @Override
            public void onFailure(Call<Darkskyapi2> call, Throwable t) {
                System.out.println("error:");
                System.out.println(t.getStackTrace());
                t.printStackTrace();
            }
        };
      //  new WeatherFinder(0,point,darkskyapiCallback).fetchWeather();


    }


    void fetchWeather(){

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .connectTimeout(120, TimeUnit.SECONDS)
                //.addInterceptor(loggingInterceptor)
                //.addNetworkInterceptor(networkInterceptor)
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DarkSky_BaseURL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        String llt=lat+","+lng+","+time;
                //"40.015,-105.2705";
                //+","+System.currentTimeMillis()*1000;

//        Calendar calendar=Calendar.getInstance();

//        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
//        sdf.setTimeZone(TimeZone.getTimeZone(calendar.getTimeZone().getID()));
//        llt+=","+sdf.format(calendar.getTime());

        ApiInterface apiService = retrofit.create(ApiInterface.class);
        Call<Darkskyapi> call = apiService.getweather(DarkskyKey,llt);

      //  call.enqueue(darkskyapiCallback);


            try {
                call.enqueue(new Callback<Darkskyapi>() {
                    @Override
                    public void onResponse(Call<Darkskyapi> call, Response<Darkskyapi> response) {


                        if(response.isSuccessful()){
                        Darkskyapi resp = response.body();
                        System.out.println("item weather resp :" + new Gson().toJson(resp));

                        int k = pos;

                        final Calendar c = Calendar.getInstance();
                        c.setTimeZone(TimeZone.getTimeZone(resp.getTimezone()));
                        c.setTimeInMillis(Long.parseLong(resp.getCurrently().getTime()) * 1000);
                        int mHour = c.get(Calendar.HOUR_OF_DAY);
                        int mMinute = c.get(Calendar.MINUTE);


                        String sHour = mHour < 10 ? "0" + mHour : "" + mHour;
                        String sMinute = mMinute < 10 ? "0" + mMinute : "" + mMinute;
                        String stn_arrtime = sHour + ":" + sMinute + " ," + strDays[c.get(Calendar.DAY_OF_WEEK) - 1] + " ," + c.get(Calendar.DAY_OF_MONTH) + " " + month[c.get(Calendar.MONTH)] + " " + String.valueOf(c.get(Calendar.YEAR)).substring(2);

                        Item item = new Item(new LatLng(resp.getLatitude(), resp.getLongitude()), resp.getCurrently(), stn_arrtime, "", matrixResponse.destinations().get(k).name());

                        Message message = new Message();
                        message.obj = new Resp(item);
                        SimpleMapViewActivity.myItemhandler.sendMessage(message);

                        }else {
                            Log.e("error","response not success,on response,item weather caller,matrixcall enque");

                            System.out.println(response.errorBody());
                            Message message = new Message();
                            message.obj = new Resp(new mError(ErrorHead_Weather, response.message()));
                            SimpleMapViewActivity.myItemhandler.sendMessage(message);
                        }
                }

                    @Override
                    public void onFailure(Call<Darkskyapi> call, Throwable t) {
                        t.printStackTrace();
                        Log.e("error","weather enque,on failture,item weather caller,matrixcall enque");
                        Message message = new Message();
                        message.obj = new Resp(new mError(ErrorHead_Weather,t.getMessage()));
                        SimpleMapViewActivity.myItemhandler.sendMessage(message);
                    }
                });
            } catch (Exception e) {
                Log.e("error","item weather caller,catch block,matrixcall enque");
                e.printStackTrace();
                Message message = new Message();
                message.obj = new Resp(new mError(ErrorHead_Weather,e.getMessage()));
                SimpleMapViewActivity.myItemhandler.sendMessage(message);
            }

        }





    void fetchWeather2(){

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .connectTimeout(120, TimeUnit.SECONDS)
                //.addInterceptor(loggingInterceptor)
                //.addNetworkInterceptor(networkInterceptor)
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DarkSky_BaseURL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        String llt=lat+","+lng+","+time;
        //"40.015,-105.2705";
        //+","+System.currentTimeMillis()*1000;

//        Calendar calendar=Calendar.getInstance();

//        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
//        sdf.setTimeZone(TimeZone.getTimeZone(calendar.getTimeZone().getID()));

        ApiInterface apiService = retrofit.create(ApiInterface.class);
        Call<Darkskyapi> call = apiService.getweather(DarkskyKey,llt);

        //  call.enqueue(darkskyapiCallback);


        try {
            call.enqueue(new Callback<Darkskyapi>() {
                @Override
                public void onResponse(Call<Darkskyapi> call, Response<Darkskyapi> response) {

                    if(response.isSuccessful()) {
                        Darkskyapi resp = response.body();
                        final Calendar c = Calendar.getInstance();
                        c.setTimeZone(TimeZone.getTimeZone(resp.getTimezone()));
                        c.setTimeInMillis(Long.parseLong(resp.getCurrently().getTime()) * 1000);
                        int mHour = c.get(Calendar.HOUR_OF_DAY);
                        int mMinute = c.get(Calendar.MINUTE);

                        String sHour = mHour < 10 ? "0" + mHour : "" + mHour;
                        String sMinute = mMinute < 10 ? "0" + mMinute : "" + mMinute;
                        String stn_arrtime = sHour + ":" + sMinute + " ," + strDays[c.get(Calendar.DAY_OF_WEEK) - 1] + " ," + c.get(Calendar.DAY_OF_MONTH) + " " + month[c.get(Calendar.MONTH)] + " " + String.valueOf(c.get(Calendar.YEAR)).substring(2);
                        //"."+d.getTimezone();
                        MStep mStep = new MStep(pos, step, resp.getCurrently(), stn_arrtime, aft_distance, step.distance());

                        Message message = new Message();
                        message.obj = new Resp(mStep);
                        SimpleMapViewActivity.myStephandler.sendMessage(message);
                    }else{
                        Message message = new Message();
                        message.obj = new Resp(new mError(ErrorHead_Weather,response.message()));
                        SimpleMapViewActivity.myStephandler.sendMessage(message);
                    }
                }

                @Override
                public void onFailure(Call<Darkskyapi> call, Throwable t) {
                    Message message = new Message();
                    message.obj = new Resp(new mError(ErrorHead_Weather,t.getMessage()));
                    SimpleMapViewActivity.myStephandler.sendMessage(message);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Message message = new Message();
            message.obj = new Resp(new mError(ErrorHead_Weather,e.getMessage()));
            SimpleMapViewActivity.myStephandler.sendMessage(message);
        }

    }




}
