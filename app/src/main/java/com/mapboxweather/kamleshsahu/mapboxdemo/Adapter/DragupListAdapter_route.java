package com.mapboxweather.kamleshsahu.mapboxdemo.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.mapbox.api.directions.v5.models.DirectionsRoute;
import com.mapbox.api.directions.v5.models.LegStep;
import com.mapboxweather.kamleshsahu.mapboxdemo.Methods.ManeuverMap;

import com.mapboxweather.kamleshsahu.mapboxdemo.Methods.unitConverter;
import com.mapboxweather.kamleshsahu.mapboxdemo.R;


/**
 * Created by RAJA on 18-12-2017.
 */

public class DragupListAdapter_route extends RecyclerView.Adapter<DragupListAdapter_route.PnrViewHolder>{

    private Context context;
    private DirectionsRoute route;
    ManeuverMap maneuverMap;
    unitConverter converter;
    public DragupListAdapter_route(Context context, DirectionsRoute route){
        this.context=context;
        this.route=route;
        this.maneuverMap=new ManeuverMap();
        this.converter=new unitConverter();
    }




    @Override
    public PnrViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater= LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.dragup_list_route,parent,false);
        return new PnrViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PnrViewHolder holder, int position) {
        LegStep mStep =route.legs().get(0).steps().get(position);

        setScaleAnimation(holder.itemView);
       // Glide.with(holder.image.getContext()).load(passengerList.getLink()).into(holder.image);
try {
    String maneuvers=mStep.maneuver().type()+mStep.maneuver().modifier();

    holder.directionImage.setImageResource(maneuverMap.getManeuverResource(maneuvers));

}catch (Exception e){

}


//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            holder.instr.setText(Html.fromHtml(mStep.htmlInstructions, Html.FROM_HTML_MODE_COMPACT));
//        }else {
//            holder.instr.setText(Html.fromHtml(mStep.htmlInstructions));
//        }
             holder.instr.setText(mStep.maneuver().instruction());
//        holder.distance.setText("Traveled : "+mStep.getAft_distance()/(long)1000+" km");
//        holder.arrtime.setText("Start time:"+mStep.getArrtime());
        try {
//            String dist= mStep.distance()+"";
            holder.stepLength.setText(converter.metertoMiles2(mStep.distance()));
      //      holder.arrtime.setText(mStep.getArrtime());
      //      holder.weather.setText(mStep.getWlist().getSummary());
      //      holder.temp.setText(mStep.getWlist().getTemperature() + "°F");
      //     holder.stepLength.setText(String.format("%.2f", (float) mStep.getStep().getDistance().getValue() / (float) 1000 * (0.621371)) + " miles");


  //          String address = new Geocoder(context, Locale.ENGLISH).getFromLocation(mStep.getStep().getStart_location().getLat(), mStep.getStep().getStart_location().getLng(), 1).get(0).getAddressLine(0);
            //  //System.out.println("hre is address :"+address);
  //          holder.address.setText(address);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

   @Override
    public int getItemCount() {
      return route.legs().get(0).steps().size();
    }

    public class PnrViewHolder extends RecyclerView.ViewHolder {



        TextView instr,stepLength;
        ImageView directionImage;
        public PnrViewHolder(View itemView) {
            super(itemView);
            instr= (TextView) itemView.findViewById(R.id.desc);
            stepLength=itemView.findViewById(R.id.stepLength);
            directionImage=itemView.findViewById(R.id.direction_image);
        }
    }
    private void setScaleAnimation(View view) {
        ScaleAnimation anim = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setDuration(500);
        view.startAnimation(anim);
    }
}
