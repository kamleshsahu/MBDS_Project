package com.mapboxweather.kamleshsahu.mapboxdemo.WeatherService.Models;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.mapboxweather.kamleshsahu.mapboxdemo.WeatherService.UIutils.weatherIconMap;

import com.mapboxweather.kamleshsahu.mapboxdemo.R;

public class CustomDialogClass extends Dialog implements
        android.view.View.OnClickListener {

    public Activity c;
    public Dialog d;
    public ImageView icon;
    public TextView temp;
    public TextView time;
    public TextView icondescription;
    mStep mstep;
    mPoint item;

    public CustomDialogClass(Activity a) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
    }

    public CustomDialogClass(Activity a,mPoint item) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
        this.item=item;
    }

    public CustomDialogClass(Activity a,mStep mstep) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
        this.mstep=mstep;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.weatherforcast_list_item);
        icon=findViewById(R.id.weatherImg);
        icondescription=findViewById(R.id.WeatherVal);
        temp=findViewById(R.id.TempVal);
        time=findViewById(R.id.date);
        try {
//            Log.i("custom marker class","on create run...");
//            if(mStep!=null) //System.out.println(new Gson().toJson(mStep));
//            else Log.e("mstep null","mstep is null");
//            if(item!=null) //System.out.println(new Gson().toJson(item));
//            else Log.e("item null","item is null");
            if (mstep != null) {
          //      new bitmapfromstring(mStep.getWlist().getIcon(), icon, icondescription);
          //      Log.i("item temp",mStep.getWlist().getTemperature());
                icon.setImageResource(new weatherIconMap().getWeatherResource(mstep.getWeatherdata().getIcon()));
                icondescription.setText(mstep.getWeatherdata().getIcon());
                temp.setText(String.format("%s°F", mstep.getWeatherdata().getTemperature()));
                time.setText(mstep.getDisplay_arrtime());
            } else {
            //    new bitmapfromstring(item.getWlist().getIcon(), icon, icondescription);
           //     Log.i("item temp",item.getWlist().getTemperature());
                icon.setImageResource(new weatherIconMap().getWeatherResource(item.getWeather_data().getIcon()));
                icondescription.setText(item.getWeather_data().getIcon());
                temp.setText(String.format("%s°F", item.getWeather_data().getTemperature()));
                time.setText(item.getDisplay_arrtime());
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.btn_yes:
//                c.finish();
//                break;
//            case R.id.btn_no:
//                dismiss();
//                break;
//            default:
//                break;
//        }
        dismiss();
    }
}