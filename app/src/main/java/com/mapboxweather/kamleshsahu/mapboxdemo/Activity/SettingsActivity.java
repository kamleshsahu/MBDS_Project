package com.mapboxweather.kamleshsahu.mapboxdemo.Activity;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.style.light.Light;
import com.mapboxweather.kamleshsahu.mapboxdemo.R;

import java.util.ArrayList;
import java.util.List;

import static com.mapbox.mapboxsdk.storage.Resource.STYLE;
import static com.mapboxweather.kamleshsahu.mapboxdemo.WeatherService.Constants.DARK;
import static com.mapboxweather.kamleshsahu.mapboxdemo.WeatherService.Constants.LIGHT;
import static com.mapboxweather.kamleshsahu.mapboxdemo.WeatherService.Constants.MAP_STYLE;
import static com.mapboxweather.kamleshsahu.mapboxdemo.WeatherService.Constants.OUTDOORS;
import static com.mapboxweather.kamleshsahu.mapboxdemo.WeatherService.Constants.SATELLITE;
import static com.mapboxweather.kamleshsahu.mapboxdemo.WeatherService.Constants.SATELLITE_STREETS;
import static com.mapboxweather.kamleshsahu.mapboxdemo.WeatherService.Constants.STREETS;
import static com.mapboxweather.kamleshsahu.mapboxdemo.WeatherService.Constants.TRAFFIC_DAY;
import static com.mapboxweather.kamleshsahu.mapboxdemo.WeatherService.Constants.TRAFFIC_NIGHT;

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
        list.add(STREETS);
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
                        prefs.putString(MAP_STYLE, Style.LIGHT).apply();
                        prefs.putInt("STYLE_INDEX",0).apply();
                        break;
                    case DARK:
                        prefs.putString(MAP_STYLE,Style.DARK).apply();
                        prefs.putInt("STYLE_INDEX",1).apply();
                        break;
                    case STREETS:
                        prefs.putString(MAP_STYLE,Style.MAPBOX_STREETS).apply();
                        prefs.putInt("STYLE_INDEX",2).apply();
                        break;
                    case SATELLITE:
                        prefs.putString(MAP_STYLE,Style.SATELLITE).apply();
                        prefs.putInt("STYLE_INDEX",3).apply();
                        break;
                    case SATELLITE_STREETS:
                        prefs.putString(MAP_STYLE,Style.SATELLITE_STREETS).apply();
                        prefs.putInt("STYLE_INDEX",4).apply();
                        break;
                    case OUTDOORS:
                        prefs.putString(MAP_STYLE,Style.OUTDOORS).apply();
                        prefs.putInt("STYLE_INDEX",5).apply();
                        break;
                    case TRAFFIC_DAY:
                        prefs.putString(MAP_STYLE,Style.TRAFFIC_DAY).apply();
                        prefs.putInt("STYLE_INDEX",6).apply();
                        break;
                    case TRAFFIC_NIGHT:
                        prefs.putString(MAP_STYLE,Style.TRAFFIC_NIGHT).apply();
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