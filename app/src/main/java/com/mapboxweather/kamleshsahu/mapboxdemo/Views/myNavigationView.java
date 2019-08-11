package com.mapboxweather.kamleshsahu.mapboxdemo.Views;//package com.example.mymapboxnavigation;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.services.android.navigation.ui.v5.NavigationView;
import com.mapbox.services.android.navigation.ui.v5.NavigationViewOptions;
import com.mapboxweather.kamleshsahu.mapboxdemo.Activity.MapboxNavigationActivity;

import static com.mapboxweather.kamleshsahu.mapboxdemo.Activity.MapboxNavigationActivity.activity;
import static com.mapboxweather.kamleshsahu.mapboxdemo.Activity.MapboxNavigationActivity.msteps;
import static com.mapboxweather.kamleshsahu.mapboxdemo.Activity.MapboxNavigationActivity.weatherUtils;

public class myNavigationView extends NavigationView implements MapboxMap.OnMapClickListener {

  //  public Activity activity;
    public MapboxMap mapboxMap;


    public myNavigationView(Context context) {
        super(context);
     }

    public myNavigationView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public myNavigationView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


//    public void setActivity(Activity activity) {
//        this.activity = activity;
//    }



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public void onMapReady(MapboxMap mapboxMap) {



        super.onMapReady(mapboxMap);
        this.mapboxMap=mapboxMap;
        mapboxMap.addOnMapClickListener(this);
        mapboxMap.getStyle(new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {
                weatherUtils.setMapboxStyle(style);
                weatherUtils.setMapboxMap(mapboxMap);
                weatherUtils.setActivity(activity);

            }
        });
    }

    @Override
    public void startNavigation(NavigationViewOptions options) {

        super.startNavigation(options);


    }

    @Override
    public boolean onMapClick(@NonNull LatLng point) {
        if(msteps!=null && MapboxNavigationActivity.layeridlist.size()>0)
            weatherUtils.mapOnClick(point,MapboxNavigationActivity.layeridlist.toArray(new String[MapboxNavigationActivity.layeridlist.size()]), msteps);

        return false;
    }


}
