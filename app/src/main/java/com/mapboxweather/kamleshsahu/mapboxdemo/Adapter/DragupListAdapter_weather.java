package com.mapboxweather.kamleshsahu.mapboxdemo.Adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.mapboxweather.kamleshsahu.mapboxdemo.Methods.unitConverter;
import com.mapboxweather.kamleshsahu.mapboxdemo.WeatherService.UIutils.weatherIconMap;
import com.mapboxweather.kamleshsahu.mapboxdemo.R;
import com.mapboxweather.kamleshsahu.mapboxdemo.WeatherService.Models.mStep;

import java.util.List;



/**
 * Created by RAJA on 18-12-2017.
 */

public class DragupListAdapter_weather extends RecyclerView.Adapter<DragupListAdapter_weather.PnrViewHolder>{

    private weatherIconMap iconInstance;
    private Context context;
    private List<mStep> msteps;
    unitConverter converter;
    public DragupListAdapter_weather(Context context, List<mStep> mSteps){
        this.context=context;
        this.msteps=mSteps;
        this.iconInstance=new weatherIconMap();
        this.converter=new unitConverter();
    }



    @Override
    public PnrViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater= LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.draguplist_weather,parent,false);
        return new PnrViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PnrViewHolder holder, int position) {
        mStep mstep =msteps.get(position);
       // Glide.with(holder.image.getContext()).load(passengerList.getLink()).into(holder.image);

        setScaleAnimation(holder.itemView);

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            holder.instr.setText(Html.fromHtml(mStep.getStep().htmlInstructions, Html.FROM_HTML_MODE_COMPACT));
//        }else {
//            holder.instr.setText(Html.fromHtml(mStep.getStep().htmlInstructions));
//        }
             holder.instr.setText(mstep.getStep().maneuver().instruction());
//        holder.distance.setText("Traveled : "+mStep.getAft_distance()/(long)1000+" km");
//        holder.arrtime.setText("Start time:"+mStep.getArrtime());
        try {



            String arrtime=mstep.getDisplay_arrtime().split(",",2)[0];
            holder.arrtime.setText(arrtime);
            holder.weather.setText(mstep.getWeatherdata().getSummary());
            holder.temp.setText(mstep.getWeatherdata().getTemperature() + "°F");

            holder.distance.setText(converter.metertoMiles2(mstep.getAft_distance()));
            holder.stepLength.setText(converter.metertoMiles2(mstep.getStep().duration()));
//            if(DistanceUnit ==2) {
//                holder.distance.setText(String.format("%.2f", (float) mStep.getAft_distance() / (float) 1000 ) + " km");
//                holder.stepLength.setText(String.format("%.2f", (float) mStep.getStep().distance.inMeters / (float) 1000 ) + " km");
//            }else{
//                holder.distance.setText(String.format("%.2f", (float) mStep.getAft_distance() / (float) 1000 * (0.621371)) + " mi");
//                holder.stepLength.setText(String.format("%.2f", (float) mStep.getStep().distance.inMeters / (float) 1000 * (0.621371)) + " mi");
//            }

            Drawable icon = context.getResources().getDrawable( iconInstance.getWeatherResource(mstep.getWeatherdata().getIcon()) );

         //   new w(icon,context,mStep);

            Glide.with(context)
                    .load(icon)
                    //     .override(100,100)
                    .into(holder.weatherimg);



  //          String address = new Geocoder(context, Locale.ENGLISH).getFromLocation(mStep.getStep().getStart_location().getLat(), mStep.getStep().getStart_location().getLng(), 1).get(0).getAddressLine(0);
            //  //System.out.println("hre is address :"+address);
  //          holder.address.setText(address);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

   @Override
    public int getItemCount() {
      return msteps.size();
    }

    public class PnrViewHolder extends RecyclerView.ViewHolder {



        TextView instr,distance,arrtime,temp,weather,stepLength,address;
        ImageView weatherimg;
        public PnrViewHolder(View itemView) {
            super(itemView);
            instr= (TextView) itemView.findViewById(R.id.instr);
            weather= (TextView) itemView.findViewById(R.id.weather);
            temp= (TextView) itemView.findViewById(R.id.temp);
            distance= (TextView) itemView.findViewById(R.id.distance);
            arrtime= (TextView) itemView.findViewById(R.id.arrtime);
            weatherimg=itemView.findViewById(R.id.weatherImg);
            stepLength=itemView.findViewById(R.id.stepLength);
//            address=itemView.findViewById(R.id.address);
        }
    }
    private void setScaleAnimation(View view) {
        ScaleAnimation anim = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setDuration(500);
        view.startAnimation(anim);
    }
}
