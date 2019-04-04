package com.mapboxweather.kamleshsahu.mapboxdemo.WeatherService.Methods;


import com.mapbox.geojson.Point;
import com.mapboxweather.kamleshsahu.mapboxdemo.WeatherService.Models.Darkskyapi;

import java.util.Calendar;

import retrofit2.Call;

import static com.mapboxweather.kamleshsahu.mapboxdemo.Constants.DarkskyKey;
import static com.mapboxweather.kamleshsahu.mapboxdemo.WeatherService.Methods.Retrofit_darksky_instance.apiService;
import static com.mapboxweather.kamleshsahu.mapboxdemo.WeatherService.Methods.TimeFormatter.getSDFtime;

public class WeatherFinder {

    double lat;
    double lng;
    String time;


    public WeatherFinder(double lat, double lng, String time) {
          this.lat=lat;
          this.lng=lng;
          this.time=time;
    }


    public static void main(String... args){


        Point point=Point.fromLngLat(-105.2705,40.015);
//        Callback<Darkskyapi2> darkskyapiCallback=new Callback<Darkskyapi2>() {
//            @Override
//            public void onResponse(Call<Darkskyapi2> call, Response<Darkskyapi2> response) {
//                //System.out.println("response:");
//                //System.out.println(response.code());
//                //System.out.println(response.raw());
//                //System.out.println(new Gson().toJson(response.body()));
//            }
//
//            @Override
//            public void onFailure(Call<Darkskyapi2> call, Throwable t) {
//                //System.out.println("error:");
//                //System.out.println(t.getStackTrace());
//                t.printStackTrace();
//            }
//        };
      //  new WeatherFinder_old(0,point,darkskyapiCallback).fetchWeather();



        String time= TimeFormatter.getSDFtime(System.currentTimeMillis(), Calendar.getInstance().getTimeZone().getID());

        Darkskyapi darkskyapi=new WeatherFinder(point.latitude(),point.longitude(),time).calcWeather();

    }


    Darkskyapi calcWeather(){


        String llt=lat+","+lng+","+time;
                //"40.015,-105.2705";
                //+","+System.currentTimeMillis()*1000;

//        Calendar calendar=Calendar.getInstance();

//        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
//        sdf.setTimeZone(TimeZone.getTimeZone(calendar.getTimeZone().getID()));
//        llt+=","+sdf.format(calendar.getTime());



        Call<Darkskyapi> call = apiService.getweather(DarkskyKey,llt);


        try {
    //          call.enqueue(darkskyapiCallback);
          return   call.execute().body();
        }catch (Exception error){
            error.printStackTrace();
            return null;
        }


//
//
//            try {
//                call.enqueue(new Callback<Darkskyapi>() {
//                    @Override
//                    public void onResponse(Call<Darkskyapi> call, Response<Darkskyapi> response) {
//
//
//                        if(response.isSuccessful()){
//                        Darkskyapi resp = response.body();
//                        //System.out.println("item weather resp :" + new Gson().toJson(resp));
//
////                        int k = pos;
//
//
//
//                         weather_data[0] =resp;
////                        Item item = new Item(new LatLng(resp.getLatitude(), resp.getLongitude()), resp.getCurrently(), stn_arrtime, "", matrixResponse.destinations().get(k).name());
//
////                        Message message = new Message();
////                        message.obj = new Resp(item);
////                        SimpleMapViewActivity.myItemhandler.sendMessage(message);
//
//                        }else {
//                            Log.e("error","response not success,on response,item weather caller,matrixcall enque");
//
//                            //System.out.println(response.errorBody());
//                            Message message = new Message();
//                            message.obj = new Resp(new mError(ErrorHead_Weather, response.message()));
//                            SimpleMapViewActivity.myItemhandler.sendMessage(message);
//                        }
//                }
//
//                    @Override
//                    public void onFailure(Call<Darkskyapi> call, Throwable t) {
//                        t.printStackTrace();
//                        Log.e("error","weather enque,on failture,item weather caller,matrixcall enque");
//                        Message message = new Message();
//                        message.obj = new Resp(new mError(ErrorHead_Weather,t.getMessage()));
//                        SimpleMapViewActivity.myItemhandler.sendMessage(message);
//                    }
//                });
//            } catch (Exception e) {
//                Log.e("error","item weather caller,catch block,matrixcall enque");
//                e.printStackTrace();
//                Message message = new Message();
//                message.obj = new Resp(new mError(ErrorHead_Weather,e.getMessage()));
//                SimpleMapViewActivity.myItemhandler.sendMessage(message);
//            }
//
//
//            return weather_data[0];
        }

}
