package com.mapboxweather.kamleshsahu.mapboxdemo;

import com.mapbox.geojson.Point;

import java.util.ArrayList;
import java.util.List;

public class test {

    public static void main(String... args){
        List<Point> interms=new ArrayList<>();

        for(int k=0;k<40;k++){
             interms.add(Point.fromLngLat(123.3435,24.44534));
        }

        List<List<Point>> intermsInParts=new ArrayList<>();
        for(int from=0;from<interms.size();from+=10){
            int to=from+10;
            if(to>interms.size())
                to=interms.size();
            intermsInParts.add(new ArrayList<>(interms.subList(from,to)));
        }
    }
}
