package com.mapboxweather.kamleshsahu.mapboxdemo.Activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.mapboxweather.kamleshsahu.mapboxdemo.R;

import java.util.ArrayList;
import java.util.List;

import static com.mapbox.mapboxsdk.maps.Style.DARK;
import static com.mapbox.mapboxsdk.maps.Style.LIGHT;
import static com.mapbox.mapboxsdk.maps.Style.MAPBOX_STREETS;
import static com.mapbox.mapboxsdk.maps.Style.OUTDOORS;
import static com.mapbox.mapboxsdk.maps.Style.SATELLITE;
import static com.mapbox.mapboxsdk.maps.Style.SATELLITE_STREETS;
import static com.mapbox.mapboxsdk.maps.Style.TRAFFIC_DAY;
import static com.mapbox.mapboxsdk.maps.Style.TRAFFIC_NIGHT;
import static com.mapboxweather.kamleshsahu.mapboxdemo.WeatherService_Navigation.Constants.MAP_STYLE;


public class SettingsActivity extends AppCompatActivity {
    Spinner month,year;
    SharedPreferences.Editor prefs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        SharedPreferences sp=getSharedPreferences("mapboxdemo", MODE_PRIVATE);
        prefs=sp.edit();
        year = findViewById(R.id.year);
        getSupportActionBar().setTitle("Settings");
        List<String> list = new ArrayList<String>();
        list.add(LIGHT);
        list.add(DARK);
        list.add(MAPBOX_STREETS);
        list.add(OUTDOORS);
        list.add(SATELLITE);
        list.add(SATELLITE_STREETS);
        list.add(TRAFFIC_DAY);
        list.add(TRAFFIC_NIGHT);

        ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        year.setAdapter(dataAdapter1);
        year.setSelected(true);
        year.setSelection(sp.getInt("STYLE_INDEX",0));
        year.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (list.get(i).toString()){
                    case LIGHT:
                        prefs.putString(MAP_STYLE, LIGHT).apply();
                        prefs.putInt("STYLE_INDEX",0).apply();
                        break;
                    case DARK:
                        prefs.putString(MAP_STYLE, DARK).apply();
                        prefs.putInt("STYLE_INDEX",1).apply();
                        break;
                    case MAPBOX_STREETS:
                        prefs.putString(MAP_STYLE, MAPBOX_STREETS).apply();
                        prefs.putInt("STYLE_INDEX",2).apply();
                        break;
                    case SATELLITE:
                        prefs.putString(MAP_STYLE, SATELLITE).apply();
                        prefs.putInt("STYLE_INDEX",3).apply();
                        break;
                    case SATELLITE_STREETS:
                        prefs.putString(MAP_STYLE, SATELLITE_STREETS).apply();
                        prefs.putInt("STYLE_INDEX",4).apply();
                        break;
                    case OUTDOORS:
                        prefs.putString(MAP_STYLE, OUTDOORS).apply();
                        prefs.putInt("STYLE_INDEX",5).apply();
                        break;
                    case TRAFFIC_DAY:
                        prefs.putString(MAP_STYLE, TRAFFIC_DAY).apply();
                        prefs.putInt("STYLE_INDEX",6).apply();
                        break;
                    case TRAFFIC_NIGHT:
                        prefs.putString(MAP_STYLE, TRAFFIC_NIGHT).apply();
                        prefs.putInt("STYLE_INDEX",7).apply();
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }
}