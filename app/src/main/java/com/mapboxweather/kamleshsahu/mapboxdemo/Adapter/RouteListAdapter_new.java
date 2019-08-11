package com.mapboxweather.kamleshsahu.mapboxdemo.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.api.directions.v5.models.DirectionsRoute;
import com.mapboxweather.kamleshsahu.mapboxdemo.Methods.unitConverter;
import com.mapboxweather.kamleshsahu.mapboxdemo.R;
import com.mapboxweather.kamleshsahu.mapboxdemo.Interface.routeChangedinList;
//import com.mapboxweather.kamleshsahu.mapboxdemo.Activity.SimpleMapViewActivity;

public class RouteListAdapter_new extends RecyclerView.Adapter<RouteListAdapter_new.ViewHolder>{
        private Context context;
    private DirectionsResponse data;
    public static int selectedPosition=0;
    routeChangedinList listener;
    // RecyclerView recyclerView;
    public RouteListAdapter_new(Context context, DirectionsResponse data) {
        this.data=data;
        this.context=context;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.listview_item_travelwithactivity_new, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);

        viewHolder.item.setSelected(true);
        return viewHolder;
    }

    public void setListener(routeChangedinList listener) {
        this.listener = listener;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final DirectionsRoute route = data.routes().get(position);
        final int finalpos=position;

        if(selectedPosition==position) {
//            holder.cardView.setCardBackgroundColor(Color.parseColor("#3F51B5"));
//            holder.route.setTextColor(Color.parseColor("#ffffff"));
//            holder.distance.setTextColor(Color.parseColor("#ffffff"));
//            holder.route_detail.setTextColor(Color.parseColor("#ffffff"));
//            holder.time.setTextColor(Color.parseColor("#ffffff"));
            holder.selected.setBackgroundColor(Color.parseColor("#3F51B5"));
        }
        else{
//            holder.cardView.setCardBackgroundColor(Color.parseColor("#ffffff"));
//            holder.route.setTextColor(Color.parseColor("#000000"));
//            holder.distance.setTextColor(Color.parseColor("#000000"));
//            holder.time.setTextColor(Color.parseColor("#7CB342"));
            holder.selected.setBackgroundColor(Color.parseColor("#ffffff"));
        }


// ban gya hai ulta pulta mat krna aa k
        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedPosition=position;
                notifyDataSetChanged();

                   if(listener!=null)
                       listener.selectedRouteChangedInList(finalpos);
//

//

            }
        });


  //      holder.itemView.
  //      setScaleAnimation(holder.itemView);

     //  //System.out.println("AdapterList,on bind view ");
        holder.route.setText(route.legs().get(0).summary());
        holder.distance.setText(new unitConverter().metertoMiles(route.distance().longValue()));
       // holder.time.setText(route.legs[0].durationInTraffic!=null?route.legs[0].durationInTraffic.humanReadable: route.legs[0].duration.humanReadable);
        holder.time.setText(new unitConverter().durationBeautify(route.duration().longValue()));

        if(position==0)
        holder.route_detail.setText(R.string.FasttestRoute);

         }


    @Override
    public int getItemCount() {
        return data.routes().size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView route,route_detail,time,distance;
        CardView cardView;
        ImageView icon;LinearLayout item;
        LinearLayout selected;
        public ViewHolder(View itemView) {
            super(itemView);
                item = itemView.findViewById(R.id.itemContainer);
                route = (TextView) itemView.findViewById(R.id.route);
                route_detail = (TextView) itemView.findViewById(R.id.route_detail);
                time = (TextView) itemView.findViewById(R.id.time);
                distance = (TextView) itemView.findViewById(R.id.distance);
                cardView=itemView.findViewById(R.id.item_card);
            selected=itemView.findViewById(R.id.selected);
                //   icon=itemView.findViewById(R.id.icon);
        }



    }
}