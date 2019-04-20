package com.mapboxweather.kamleshsahu.mapboxdemo.WeatherService.Methods;

import com.mapbox.api.directions.v5.models.DirectionsRoute;
import com.mapbox.api.directions.v5.models.LegStep;
import com.mapbox.core.constants.Constants;
import com.mapbox.geojson.LineString;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapboxweather.kamleshsahu.mapboxdemo.WeatherService.Interface.IntermediatePointListener;
import com.mapboxweather.kamleshsahu.mapboxdemo.WeatherService.Models.mPoint;
import com.mapboxweather.kamleshsahu.mapboxdemo.WeatherService.Models.mStep;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by k on 3/27/2019.
 */

public class IntermediatePoints {

    IntermediatePointListener listener;
    public IntermediatePoints(IntermediatePointListener listener) {
        this.listener = listener;
    }

    public void setListener(IntermediatePointListener listener) {
        this.listener = listener;
    }

    public void extractListofPoints(long interval, DirectionsRoute routedata, String timezoneid, long jstarttime, String travelmode){

        Map<Integer,mStep> stepList_request_forWeathers=new HashMap<>();
        try {
        List<LegStep> steps = routedata.legs().get(0).steps();

        long aft_duration = 0;
        long aft_distance = 0;


        for (int k = 0; k < steps.size(); k++) {
            if (k == 0) {
                aft_duration = 0;
                aft_distance = 0;
            } else {
                aft_distance += steps.get(k - 1).distance();
                aft_duration += steps.get(k - 1).duration();
            }

            stepList_request_forWeathers.put((k+1)*1000,new mStep(k,steps.get(k).maneuver().location(),jstarttime, aft_duration, aft_distance, timezoneid, steps.get(k)));
            List<LatLng> points = new ArrayList<>();
            List<Point> coords = LineString.fromPolyline(steps.get(k).geometry(), Constants.PRECISION_6).coordinates();


            for (Point point : coords) {
                points.add(new LatLng(point.latitude(), point.longitude()));
            }


            int next = (int) interval;
            int dist = 0;
            int olddist = 0;
            int count=0;
            Map<Integer,mPoint> interms = new HashMap<>();
            for (int i = 10; i < points.size(); i += 10) {
                olddist = dist;
                dist += new DistanceCalculator().distance(points.get(i).getLatitude(), points.get(i - 10).getLatitude(), points.get(i).getLongitude(), points.get(i - 10).getLongitude(), 0, 0);
                //       //System.out.println(olddist+" --> "+dist);
                while (dist > next) {
                    LatLng p1 = points.get(i - 10);
                    LatLng p2 = points.get(i);
                    int m = (next - olddist) / (dist - olddist);
                    LatLng currpos = new LatLng(p1.getLatitude() + (p2.getLatitude() - p1.getLatitude()) * m, p1.getLongitude() + (p2.getLongitude() - p1.getLongitude()) * m);
                    interms.put((k+1)*1000+ ++count,new mPoint(Point.fromLngLat(currpos.getLongitude(), currpos.getLatitude())));
                    //           //System.out.println("interm added to list");
                    next += (int) interval;
                }
            }

                if (interms.size() > 0) {
                    stepList_request_forWeathers.get((k+1)*1000).setInterms(interms);
                }
        }


             if(listener!=null)
               listener.OnIntermediatePointsCalculated(stepList_request_forWeathers);

        }catch (Exception e){
            e.printStackTrace();
            if(listener!=null)
                listener.onError("Intermediate Point Calc.",e.getMessage());
        }

    }

}
