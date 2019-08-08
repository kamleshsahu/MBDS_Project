package com.mapboxweather.kamleshsahu.mapboxdemo.WeatherService_Navigation.Methods;

import android.location.Location;

import com.mapbox.geojson.Point;
import com.mapbox.services.android.navigation.v5.milestone.Milestone;
import com.mapbox.services.android.navigation.v5.milestone.StepMilestone;
import com.mapbox.services.android.navigation.v5.milestone.Trigger;
import com.mapbox.services.android.navigation.v5.milestone.TriggerProperty;
import com.mapbox.services.android.navigation.v5.routeprogress.RouteProgress;
import com.mapboxweather.kamleshsahu.mapboxdemo.WeatherService_Navigation.Models.StepCorrection;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by k on 5/6/2019.
 */

public class myUtils {

   public static StepCorrection getCorrection(Location location, RouteProgress routeProgress){
        int newdist=(int)routeProgress.currentLegProgress().currentStepProgress().distanceRemaining();
        int newduration=(int)routeProgress.currentLegProgress().currentStepProgress().durationRemaining();
        Point newlocation= Point.fromLngLat(location.getLongitude(),location.getLatitude());
        int distfrom_stepstart=(int)routeProgress.currentLegProgress().currentStepProgress().distanceTraveled();
        return new StepCorrection(newdist,newduration,newlocation,distfrom_stepstart);
    }

   public static List<Milestone> mycustomMilestone(){

        List<Milestone> list=new ArrayList<>();
        list.add(new StepMilestone.Builder().setIdentifier(1000)
                .setTrigger(Trigger.eq(TriggerProperty.FIRST_STEP,1)).build());
        list.add(new StepMilestone.Builder().setIdentifier(1000)
                .setTrigger(Trigger.eq(TriggerProperty.NEW_STEP,1)).build());
        return list;
    }
}
