package com.mapboxweather.kamleshsahu.mapboxdemo.WeatherService.Methods;

import com.mapbox.geojson.Point;
import com.mapboxweather.kamleshsahu.mapboxdemo.WeatherService.Models.mPoint;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by k on 4/18/2019.
 */

public class SplitInParts {


    public SplitInParts() {
        super();
    }

  static List<List<Point>> split_in_parts(Map<Integer,mPoint> intermsmap, int max_Api_Count){
        List<List<Point>> intermsInParts=new ArrayList<>();

      List<Map.Entry<Integer,mPoint>> interms = new ArrayList<>(intermsmap.entrySet());
      Collections.sort(
              interms
              , new Comparator<Map.Entry<Integer, mPoint>>() {
                  @Override
                  public int compare(Map.Entry<Integer, mPoint> o1, Map.Entry<Integer, mPoint> o2) {
                      return o1.getKey().compareTo(o2.getKey());
                  }
              }
      );


      for (int from = 0; from < interms.size(); from += max_Api_Count) {
            int to = from + max_Api_Count;
            if (to > interms.size())
                to = interms.size();

            ArrayList<Point> temp=new ArrayList<>();
            for(int i=from;i<to;i++){
                temp.add(interms.get(i).getValue().getPoint());
            }

            intermsInParts.add(temp);
        }
        return intermsInParts;
    }
}
