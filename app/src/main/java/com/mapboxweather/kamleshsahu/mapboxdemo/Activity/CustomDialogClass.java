package com.mapboxweather.kamleshsahu.mapboxdemo.Activity;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapboxweather.kamleshsahu.mapboxdemo.Methods.bitmapfromstring;
import com.mapboxweather.kamleshsahu.mapboxdemo.Models.Item;
import com.mapboxweather.kamleshsahu.mapboxdemo.Models.MStep;
import com.mapboxweather.kamleshsahu.mapboxdemo.R;

public class CustomDialogClass extends Dialog implements
        android.view.View.OnClickListener {

    public Activity c;
    public Dialog d;
    public ImageView icon;
    public TextView temp;
    public TextView time;
    public TextView icondescription;
    MStep mStep;
    Item item;

    public CustomDialogClass(Activity a) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
    }

    public CustomDialogClass(Activity a,Item item) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
        this.item=item;
    }

    public CustomDialogClass(Activity a,MStep mstep) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
        this.mStep=mstep;
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
//            if(mStep!=null) System.out.println(new Gson().toJson(mStep));
//            else Log.e("mstep null","mstep is null");
//            if(item!=null) System.out.println(new Gson().toJson(item));
//            else Log.e("item null","item is null");
            if (mStep != null) {
                new bitmapfromstring(mStep.getWlist().getIcon(), icon, icondescription);
          //      Log.i("item temp",mStep.getWlist().getTemperature());
                temp.setText(String.format("%s°F", mStep.getWlist().getTemperature()));
                time.setText(mStep.getArrtime());
            } else {
                new bitmapfromstring(item.getWlist().getIcon(), icon, icondescription);
           //     Log.i("item temp",item.getWlist().getTemperature());
                temp.setText(String.format("%s°F", item.getWlist().getTemperature()));
                time.setText(item.getArrtime());
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