package com.mapboxweather.kamleshsahu.mapboxdemo.WeatherService_Navigation.Methods;

import android.content.Context;
import android.graphics.PointF;
import android.graphics.RectF;

import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.api.directions.v5.models.DirectionsRoute;
import com.mapbox.core.constants.Constants;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.LineString;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.style.layers.LineLayer;
import com.mapbox.mapboxsdk.style.layers.Property;
import com.mapbox.mapboxsdk.style.layers.PropertyFactory;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapboxweather.kamleshsahu.mapboxdemo.Interface.selectedRouteChangedListener;
import com.mapboxweather.kamleshsahu.mapboxdemo.R;

import java.util.ArrayList;
import java.util.List;


public class MPolyline {
    MapboxMap mapboxMap;
    Style mapboxStyle;
    List<DirectionsRoute> routes;
    Context context;
    int selectedroute;
    public static String[] linelayerids;

    selectedRouteChangedListener listener;

    public MPolyline(Context context, MapboxMap mapboxMap, Style style, DirectionsResponse response, int primary) {
        this.mapboxMap = mapboxMap;
        this.routes=response.routes();
        this.mapboxStyle=style;
        this.context=context;

        linelayerids=new String[routes.size()];
        selectedroute=primary;
        addRoutes(routes,primary);
    }

    public void setListener(selectedRouteChangedListener listener) {
        this.listener = listener;
    }

    void addRoutes(List<DirectionsRoute> routes, int selectedroute){

        for(int i=0;i<routes.size();i++){
            String id=i+"";
            linelayerids[i]=id;
            if(i!=selectedroute)
                addPolyline(routes.get(i).geometry(),id,context.getResources().getColor(R.color.alternateRoute),selectedroute);
        }
        addPolyline(routes.get(selectedroute).geometry(),""+selectedroute,context.getResources().getColor(R.color.seletedRoute),selectedroute);
    }


    public void addPolyline(String poly, String LINE_LAYER_ID, int color,int selectedroute) {
        List<Feature> features = new ArrayList<>();
        /* Source: A data source specifies the geographic coordinate where the image marker gets placed. */
        //  features.add(Feature.fromGeometry(Point.fromLngLat(-78.7448, 40.2489)));


        features.add(Feature.fromGeometry(LineString.fromPolyline(poly, Constants.PRECISION_6), null, LINE_LAYER_ID));





        FeatureCollection featureCollection = FeatureCollection.fromFeatures(features);
        GeoJsonSource source = new GeoJsonSource(LINE_LAYER_ID, featureCollection);

// The layer properties for our line. This is where we make the line dotted, set the
// color, etc.
        LineLayer lineLayer= new LineLayer(LINE_LAYER_ID, LINE_LAYER_ID);

        if(LINE_LAYER_ID.equals("p"+selectedroute)){
            lineLayer.withProperties(PropertyFactory.lineCap(Property.LINE_CAP_SQUARE),
                    PropertyFactory.lineJoin(Property.LINE_JOIN_MITER),
                    PropertyFactory.lineOpacity(.9f),

                    PropertyFactory.lineWidth(8f),
                    PropertyFactory.lineColor(color));

        }else {
            lineLayer.withProperties(PropertyFactory.lineCap(Property.LINE_CAP_SQUARE),
                    PropertyFactory.lineJoin(Property.LINE_JOIN_MITER),
                    PropertyFactory.lineOpacity(.9f),
                    PropertyFactory.lineWidth(7f),
                    PropertyFactory.lineColor(color));


        }


        //      mapboxStyle.addLayer(markerStyleLayer);
//        mapboxStyle.removeLayer(lineLayer.getId());
//        mapboxStyle.removeSource(source.getId());
        mapboxStyle.addLayer(lineLayer);
        mapboxStyle.addSource(source);

    }

    public void mapOnClick(LatLng point) {
        final PointF pointf = mapboxMap.getProjection().toScreenLocation(point);

            RectF rectF1 = new RectF(pointf.x - 20, pointf.y - 20, pointf.x + 20, pointf.y + 20);
            List<Feature> features1 = mapboxMap.queryRenderedFeatures(rectF1,linelayerids);
            if(features1.size()>0) {
                int routeid = Integer.parseInt(features1.get(0).id());
                updateRoutesinMap(routeid,false);
            }
     }


    public void updateRoutesinMap(int routeid, boolean updated){
        if(routeid<10 && selectedroute!=routeid){

            selectedroute=routeid;

            for(int i=0;i<routes.size();i++){
                if(selectedroute!=i) {
                    mapboxStyle.getLayer("" + i).setProperties(
                            PropertyFactory.lineWidth(7f),
                            PropertyFactory.lineColor(context.getResources().getColor(R.color.alternateRoute)));
                }
            }

            mapboxStyle.getLayer(routeid+"").setProperties(
                    PropertyFactory.lineWidth(8f),
                    PropertyFactory.lineColor(context.getResources().getColor(R.color.seletedRoute)));

//            if(dragUpListener!=null)
//                dragUpListener.OnDragUpHeadLineChange();

            if(listener!=null && !updated)
                listener.onSelectedRouteChanged(routeid);

        }

    }


}
