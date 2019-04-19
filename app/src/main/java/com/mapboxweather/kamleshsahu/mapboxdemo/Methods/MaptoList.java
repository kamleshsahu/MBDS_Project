package com.mapboxweather.kamleshsahu.mapboxdemo.Methods;

import com.mapboxweather.kamleshsahu.mapboxdemo.WeatherService.Models.mPoint;
import com.mapboxweather.kamleshsahu.mapboxdemo.WeatherService.Models.mStep;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;


/**
 * Created by k on 4/19/2019.
 */

public class MaptoList {

  public static List<mStep> maptolist(Map<Integer, mStep> msteps){

       List<Map.Entry<Integer,mStep>> stepslist = new ArrayList<>(msteps.entrySet());

       Collections.sort(stepslist, new Comparator<Map.Entry<Integer, mStep>>() {
           @Override
           public int compare(Map.Entry<Integer, mStep> o1, Map.Entry<Integer, mStep> o2) {
               return o1.getKey().compareTo(o2.getKey());
           }
       });

       List<mStep> list=new ArrayList<>(stepslist.size());
       for(int i=0;i<stepslist.size();i++){
           list.add(stepslist.get(i).getValue());
       }
       return list;
   }

}
