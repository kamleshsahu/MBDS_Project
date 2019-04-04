//package com.mapboxweather.kamleshsahu.mapboxdemo.Methods;
//
//import android.os.Message;
//
//import com.mapbox.api.directions.v5.models.LegStep;
//import com.mapboxweather.kamleshsahu.mapboxdemo.Activity.SimpleMapViewActivity;
//import com.mapboxweather.kamleshsahu.mapboxdemo.Models.Resp;
//import com.mapboxweather.kamleshsahu.mapboxdemo.Models.mError;
//
//import java.text.SimpleDateFormat;
//import java.util.TimeZone;
//
//import static com.mapboxweather.kamleshsahu.mapboxdemo.Constants.ErrorHead_STEP;
//import static com.mapboxweather.kamleshsahu.mapboxdemo.Constants.ErrorHead_Weather;
//
//
//public class stepapiscaller  {
//
//    private long jstarttime;
//
//    private long aft_duration;
//    private long aft_distance;
//    private LegStep step;
//    private String timezoneid;
//    private int pos;
//
//
//
//    public stepapiscaller(int pos,long jstarttime, long aft_duration, long aft_distance, String timezoneid, LegStep step) {
//
//        this.aft_distance=aft_distance;
//        this.aft_duration=aft_duration;
//        this.jstarttime=jstarttime;
//        this.step=step;
//        this.timezoneid=timezoneid;
//        this.pos=pos;
//
//    }
//
//    void call(){
//
//         try {
//             long arrival_time_millis = jstarttime + aft_duration * 1000;
//
//             final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
//             sdf.setTimeZone(TimeZone.getTimeZone(timezoneid));
//             String time = sdf.format(arrival_time_millis);
//
//  //           new WeatherFinder_old(pos, step.maneuver().location(), time, step, aft_distance).fetchWeather2();
//         }catch (Error error){
//             error.printStackTrace();
//             Message message = new Message();
//             message.obj = new Resp(new mError(ErrorHead_STEP,error.getMessage()));
//             SimpleMapViewActivity.myStephandler.sendMessage(message);
//
//         }
//        }
//
//
//}
