package com.mapboxweather.kamleshsahu.mapboxdemo.WeatherService_Navigation;


import android.os.AsyncTask;

import com.mapbox.api.directions.v5.models.DirectionsRoute;
import com.mapboxweather.kamleshsahu.mapboxdemo.WeatherService_Navigation.Interface.IntermediatePointListener;
import com.mapboxweather.kamleshsahu.mapboxdemo.WeatherService_Navigation.Interface.PointMatrixListener;
import com.mapboxweather.kamleshsahu.mapboxdemo.WeatherService_Navigation.Interface.WeatherServiceListener;
import com.mapboxweather.kamleshsahu.mapboxdemo.WeatherService_Navigation.Interface.WeatherofPointListener;
import com.mapboxweather.kamleshsahu.mapboxdemo.WeatherService_Navigation.Methods.IntermediatePoints;
import com.mapboxweather.kamleshsahu.mapboxdemo.WeatherService_Navigation.Methods.PointMatrixForAll;
import com.mapboxweather.kamleshsahu.mapboxdemo.WeatherService_Navigation.Methods.TimeFormatter;
import com.mapboxweather.kamleshsahu.mapboxdemo.WeatherService_Navigation.Methods.WeatherFinder;
import com.mapboxweather.kamleshsahu.mapboxdemo.WeatherService_Navigation.Models.Darkskyapi;
import com.mapboxweather.kamleshsahu.mapboxdemo.WeatherService_Navigation.Models.mPoint;
import com.mapboxweather.kamleshsahu.mapboxdemo.WeatherService_Navigation.Models.mStep;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;


public class WeatherService extends AsyncTask<Void,Object,Void>
        implements
        PointMatrixListener,
        WeatherofPointListener,
        IntermediatePointListener
{

    private DirectionsRoute routedata=null;
    private String timezoneid;
    private long interval=50000;
    private long jstarttime;
    private String travelmode;
    Map<Integer, mStep> msteps;
    WeatherFinder wfs;
    PointMatrixForAll pointMatrixs;
    IntermediatePoints fn;
    WeatherServiceListener listener;
    Set<Integer> queue;
    int totalpoints;
    int count=0;



    public WeatherService(DirectionsRoute routedata, String timezoneid, long interval, long jstarttime, String travelmode) {
        this.routedata = routedata;
        this.timezoneid = timezoneid;
        this.interval = interval;
        this.jstarttime = jstarttime;
        this.travelmode = travelmode;
        queue=new HashSet<>();
    }

    public void subscribe(WeatherServiceListener listener) {
      this.listener=listener;
    }
    public void unsubscribe() {
        this.listener=null;
    }




    @Override
    public void OnIntermediatePointsCalculated(Map<Integer, mStep> msteps) {
        this.msteps = msteps;

        queue.addAll(msteps.keySet());
        for(int key:msteps.keySet()){
            Map<Integer, mPoint> interms;
            if((interms=msteps.get(key).getInterms())!=null)
            queue.addAll(interms.keySet());
        }
        totalpoints=queue.size();

        for (Map.Entry<Integer, mStep> mstep : msteps.entrySet()) {

            wfs.calcWeather(mstep.getKey(),
                    mstep.getValue().getStep_StartPoint().latitude(),
                    mstep.getValue().getStep_StartPoint().longitude(),
                    mstep.getValue().getSDFTime());
        }

        pointMatrixs.calc(msteps,travelmode);
    }

    @Override
    public void OnPointMatrixCalculated(int id) {

       int step_id=id-(id%1000);

       mPoint mpoint=msteps.get(step_id).getInterms().get(id);

        wfs.calcWeather(id,
                mpoint.getPoint().latitude(),
                mpoint.getPoint().longitude(),
                mpoint.getDs_arr_time());

    }


    @Override
    public void OnWeatherFetched(int id, Darkskyapi response) {
        int step_id=id-(id%1000);

        if(id%1000 != 0) {
            mPoint mpoint = msteps.get(step_id).getInterms().get(id);
            mpoint.setWeather_data(response.getCurrently());
            mpoint.setDisplay_arrtime(TimeFormatter.formatTimeforDisp(response.getCurrently().getTime(),response.getTimezone()));

            if(listener!=null)
                listener.onWeatherOfPointReady(id,mpoint);
        }else{
           mStep mstep= msteps.get(step_id);
           mstep.setWeatherdata(response.getCurrently());
           mstep.setDisplay_arrtime(TimeFormatter.formatTimeforDisp(response.getCurrently()
                    .getTime(),response.getTimezone()));
           if(listener!=null)
               listener.onWeatherOfStepReady(step_id,mstep);
        }

        queue.remove(id);

        if(listener!=null)
        if(queue.size()==0){
            listener.OnWeatherDataListReady(msteps);
        }else{
            listener.onWeatherDataListProgressChange((count++ *100)/totalpoints);
        }
    }

    @Override
    public void onError(String etitle, String emsg) {
        if(listener!=null)
        listener.onError(etitle,emsg);
    }


    @Override
    protected Void doInBackground(Void... voids) {
        fn = new IntermediatePoints(this);
        pointMatrixs = new PointMatrixForAll(this);
        wfs = new WeatherFinder(this);
        fn.extractListofPoints(interval,routedata,timezoneid,jstarttime,travelmode);
        return null;
    }



    
}
