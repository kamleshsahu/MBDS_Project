package com.mapboxweather.kamleshsahu.mapboxdemo.Methods;


import android.graphics.Bitmap;
import android.widget.ImageView;
import android.widget.TextView;

import com.mapboxweather.kamleshsahu.mapboxdemo.R;

/**
 * Created by kamlesh on 28-04-2018.
 */

public class bitmapfromstring {


    public bitmapfromstring(String name, ImageView image, TextView weather) {


        switch (name) {
            case "clear_day":
                image.setBackgroundResource(R.drawable.clear_day);
                weather.setText("Clear Day");
                break;
            case "cloudy":
                image.setBackgroundResource(R.drawable.cloudy);
                weather.setText("Cloudy");
                break;
            case "clear-night":
                image.setBackgroundResource(R.drawable.clear_night);
                weather.setText("Clear Night");
                break;
            case "fog":
                image.setBackgroundResource(R.drawable.fog);
                weather.setText("Fog");
                break;
            case "hail":
                image.setBackgroundResource(R.drawable.hail);
                weather.setText("Hail");
                break;
            case "partly-cloudy-day":
                image.setBackgroundResource(R.drawable.partly_cloudy_day);
                weather.setText("Partly Cloudy Day");
                break;
            case "partly-cloudy-night":
                image.setBackgroundResource(R.drawable.partly_cloudy_night);
                weather.setText("Partly Cloudy Night");
                break;
            case "rain":
                image.setBackgroundResource(R.drawable.rain);
                weather.setText("Rain");
                break;
            case "sleet":
                image.setBackgroundResource(R.drawable.sleet);
                weather.setText("Sleet");
                break;
            case "snow":
                image.setBackgroundResource(R.drawable.snow);
                weather.setText("Snow");
                break;
            case "thunderstorm":
                image.setBackgroundResource(R.drawable.thunderstorm);
                weather.setText("Thunderstorm");
                break;
            case "tornado":
                image.setBackgroundResource(R.drawable.tornado);
                weather.setText("Tornado");
                break;
            case "wind":
                image.setBackgroundResource(R.drawable.wind);
                weather.setText("Wind");
                break;
            default:
                image.setBackgroundResource(R.drawable.clear_day);
                weather.setText("Clear Day");
        }

    }


}
