package com.mapboxweather.kamleshsahu.mapboxdemo.Methods;


import android.os.Message;
import android.util.Log;

import com.mapbox.geojson.Point;
import com.mapboxweather.kamleshsahu.mapboxdemo.Activity.SimpleMapViewActivity;
import com.mapboxweather.kamleshsahu.mapboxdemo.Models.Darkskyapi;
import com.mapboxweather.kamleshsahu.mapboxdemo.Models.Darkskyapi2;
import com.mapboxweather.kamleshsahu.mapboxdemo.Models.Resp;
import com.mapboxweather.kamleshsahu.mapboxdemo.Models.mError;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.mapboxweather.kamleshsahu.mapboxdemo.Constants.DarkskyKey;
import static com.mapboxweather.kamleshsahu.mapboxdemo.Constants.ErrorHead_Weather;
import static com.mapboxweather.kamleshsahu.mapboxdemo.Methods.Main.apiService;

public class WeatherFinder_edit {

    double lat;
    double lng;
    String time;


    public WeatherFinder_edit(double lat,double lng,String time) {
          this.lat=lat;
          this.lng=lng;
          this.time=time;
    }


    public static void main(String... args){


        Point point=Point.fromLngLat(-105.2705,40.015);
        Callback<Darkskyapi2> darkskyapiCallback=new Callback<Darkskyapi2>() {
            @Override
            public void onResponse(Call<Darkskyapi2> call, Response<Darkskyapi2> response) {
                //System.out.println("response:");
                //System.out.println(response.code());
                //System.out.println(response.raw());
                //System.out.println(new Gson().toJson(response.body()));
            }

            @Override
            public void onFailure(Call<Darkskyapi2> call, Throwable t) {
                //System.out.println("error:");
                //System.out.println(t.getStackTrace());
                t.printStackTrace();
            }
        };
      //  new WeatherFinder(0,point,darkskyapiCallback).fetchWeather();


    }


    Darkskyapi calcWeather(){


        String llt=lat+","+lng+","+time;
                //"40.015,-105.2705";
                //+","+System.currentTimeMillis()*1000;

//        Calendar calendar=Calendar.getInstance();

//        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
//        sdf.setTimeZone(TimeZone.getTimeZone(calendar.getTimeZone().getID()));
//        llt+=","+sdf.format(calendar.getTime());


        final Darkskyapi[] weather_data = new Darkskyapi[1];

        Call<Darkskyapi> call = apiService.getweather(DarkskyKey,llt);

      //  call.enqueue(darkskyapiCallback);



            try {
                call.enqueue(new Callback<Darkskyapi>() {
                    @Override
                    public void onResponse(Call<Darkskyapi> call, Response<Darkskyapi> response) {


                        if(response.isSuccessful()){
                        Darkskyapi resp = response.body();
                        //System.out.println("item weather resp :" + new Gson().toJson(resp));

//                        int k = pos;



                         weather_data[0] =resp;
//                        Item item = new Item(new LatLng(resp.getLatitude(), resp.getLongitude()), resp.getCurrently(), stn_arrtime, "", matrixResponse.destinations().get(k).name());

//                        Message message = new Message();
//                        message.obj = new Resp(item);
//                        SimpleMapViewActivity.myItemhandler.sendMessage(message);

                        }else {
                            Log.e("error","response not success,on response,item weather caller,matrixcall enque");

                            //System.out.println(response.errorBody());
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


            return weather_data[0];
        }

}
