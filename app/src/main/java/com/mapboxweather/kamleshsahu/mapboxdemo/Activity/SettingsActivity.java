package com.mapboxweather.kamleshsahu.mapboxdemo.Activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
    Spinner month,mapTheme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        SharedPreferences sp= PreferenceManager.getDefaultSharedPreferences(this);

        mapTheme = findViewById(R.id.year);
        getSupportActionBar().setTitle("Settings");

        String prev_MAP_THEME=sp.getString(MAP_STYLE,LIGHT);


        List<String> list = new ArrayList<String>();
        list.add("LIGHT");
        list.add("DARK");
        list.add("MAPBOX STREETS");
        list.add("SATELLITE");
        list.add("SATELLITE STREETS");
        list.add("OUTDOORS");
        list.add("TRAFFIC DAY");
        list.add("TRAFFIC NIGHT");



        ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mapTheme.setAdapter(dataAdapter1);
        mapTheme.setSelected(true);
        mapTheme.setSelection(sp.getInt("STYLE_INDEX",0));
        mapTheme.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (list.get(i).toString()){
                    case "LIGHT":
                        sp.edit().putString(MAP_STYLE, LIGHT).apply();
                        sp.edit().putInt("STYLE_INDEX",0).apply();
                        break;
                    case "DARK":
                        sp.edit().putString(MAP_STYLE, DARK).apply();
                        sp.edit().putInt("STYLE_INDEX",1).apply();
                        break;
                    case "MAPBOX STREETS":
                        sp.edit().putString(MAP_STYLE, MAPBOX_STREETS).apply();
                        sp.edit().putInt("STYLE_INDEX",2).apply();
                        break;
                    case "SATELLITE":
                        sp.edit().putString(MAP_STYLE, SATELLITE).apply();
                        sp.edit().putInt("STYLE_INDEX",3).apply();
                        break;
                    case "SATELLITE STREETS":
                        sp.edit().putString(MAP_STYLE, SATELLITE_STREETS).apply();
                        sp.edit().putInt("STYLE_INDEX",4).apply();
                        break;
                    case "OUTDOORS":
                        sp.edit().putString(MAP_STYLE, OUTDOORS).apply();
                        sp.edit().putInt("STYLE_INDEX",5).apply();
                        break;
                    case "TRAFFIC DAY":
                        sp.edit().putString(MAP_STYLE, TRAFFIC_DAY).apply();
                        sp.edit().putInt("STYLE_INDEX",6).apply();
                        break;
                    case "TRAFFIC NIGHT":
                        sp.edit().putString(MAP_STYLE, TRAFFIC_NIGHT).apply();
                        sp.edit().putInt("STYLE_INDEX",7).apply();
                        break;
                }


             //   setResult(RESULT_OK,new Intent());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }

        });

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

      //  finish();

    }
}