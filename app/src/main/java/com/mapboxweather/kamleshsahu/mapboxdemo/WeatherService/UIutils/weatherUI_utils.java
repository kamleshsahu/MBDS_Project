package com.mapboxweather.kamleshsahu.mapboxdemo.WeatherService.UIutils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.Log;

import com.google.gson.Gson;
import com.mapbox.core.constants.Constants;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.LineString;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.style.layers.LineLayer;
import com.mapbox.mapboxsdk.style.layers.Property;
import com.mapbox.mapboxsdk.style.layers.PropertyFactory;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.mapboxsdk.style.sources.Source;
import com.mapboxweather.kamleshsahu.mapboxdemo.WeatherService.Models.CustomDialogClass;
import com.mapboxweather.kamleshsahu.mapboxdemo.Interface.DragUpChangeListener;
import com.mapboxweather.kamleshsahu.mapboxdemo.R;
import com.mapboxweather.kamleshsahu.mapboxdemo.WeatherService.Models.mStep;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.mapboxweather.kamleshsahu.mapboxdemo.Activity.MainActivity.directionapiresp;
import static com.mapboxweather.kamleshsahu.mapboxdemo.Activity.MainActivity.selectedroute;
import static com.mapboxweather.kamleshsahu.mapboxdemo.Activity.SimpleMapViewActivity.layeridCreated;
import static com.mapboxweather.kamleshsahu.mapboxdemo.Activity.SimpleMapViewActivity.linelayerids;

/**
 * Created by k on 4/24/2019.
 */
public class weatherUI_utils {
    Style mapboxStyle;
    Context context;
    MapboxMap mapboxMap;
    Activity activity;
    DragUpChangeListener dragUpListener;
    public weatherUI_utils(MapboxMap mapboxMap, Activity activity) {
        this.mapboxMap=mapboxMap;
       this.mapboxStyle=mapboxMap.getStyle();
       this.activity=activity;
       this.context=activity.getApplicationContext();
    }

    public void setDragUpListener(DragUpChangeListener dragUpListener) {
        this.dragUpListener = dragUpListener;
    }

    public void addMarkers (int iconid, String MARKER_IMAGE, String MARKER_SOURCE, Point point, String
            MARKER_STYLE_LAYER, String index){
        List<Feature> features = new ArrayList<>();
        /* Source: A data source specifies the geographic coordinate where the image marker gets placed. */
        //  features.add(Feature.fromGeometry(Point.fromLngLat(-78.7448, 40.2489)));

        features.add(Feature.fromGeometry(point, null, index));
        FeatureCollection featureCollection = FeatureCollection.fromFeatures(features);
        GeoJsonSource source = new GeoJsonSource(MARKER_SOURCE, featureCollection);
        mapboxStyle.removeSource(source.getId());
        mapboxStyle.addSource(source);

   /* Style layer: A style layer ties together the source and image and specifies how they are displayed on the map. */
        SymbolLayer markerStyleLayer = new SymbolLayer(MARKER_STYLE_LAYER, MARKER_SOURCE)
                .withProperties(
                        PropertyFactory.iconAllowOverlap(true),
                        PropertyFactory.iconImage(MARKER_IMAGE)
                );

        Bitmap icon = BitmapFactory.decodeResource(
                context.getResources(), iconid);
        mapboxStyle.addLayer(markerStyleLayer);
        mapboxStyle.addImage(MARKER_IMAGE, icon);

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

        mapboxStyle.addLayer(lineLayer);
        mapboxStyle.addSource(source);

    }

    public void removeWeatherIcons(List<String> layeridlist, List<Source> markersourcelist){

        for(int i=0;i<layeridlist.size();i++) {
            mapboxStyle.removeLayer(layeridlist.get(i));
            mapboxStyle.removeImage(layeridlist.get(i));
        }

        for(int i=0;i<markersourcelist.size();i++){
            mapboxStyle.removeSource(markersourcelist.get(i));

        }

       layeridCreated = false;
        //System.out.println("all removed");
        layeridlist=new ArrayList<>();
        markersourcelist=new ArrayList<>();
    }

    public void mapOnClick(LatLng point,List<String> layeridlist,String[] layerids,Map<Integer,mStep> msteps) {
        final PointF pointf = mapboxMap.getProjection().toScreenLocation(point);

        RectF rectF = new RectF(pointf.x - 10, pointf.y - 10, pointf.x + 10, pointf.y + 10);
        //        String layerids[] = {"S1", "S2","S3","S4","S5","S6","S7","S8","S9"};
        //        List<String> layeridlist=new ArrayList<>();
        if (!layeridCreated) {
            //               Collections.reverse(layeridlist);
            layerids = layeridlist.toArray(new String[layeridlist.size()]);
        }
        List<Feature> features = mapboxMap.queryRenderedFeatures(rectF, layerids);


        if (features.size() > 0) {
            Feature feature = features.get(0);

            Log.i("featute id :", feature.id());

            int featureid=Integer.parseInt(feature.id());

            if (featureid<10) {
                Log.i("marker clicked :", "polyline clicked");
                routechangeListener(featureid);
            } else if (featureid % 1000 == 0) {
                Log.i("marker clicked :", "step marker clicked");
                int step_id = (featureid / 1000) * 1000;
                new CustomDialogClass(activity, msteps.get(step_id)).show();
            } else if (featureid % 1000 != 0) {
                Log.i("marker clicked :", "item marker clicked");
                int step_id = (featureid / 1000) * 1000;
                int index = (featureid);
                new CustomDialogClass(activity, msteps.get(step_id).getInterms().get(index)).show();
            }


        } else {
            //System.out.println(" else part else part");
            RectF rectF1 = new RectF(pointf.x - 20, pointf.y - 20, pointf.x + 20, pointf.y + 20);
            List<Feature> features1 = mapboxMap.queryRenderedFeatures(rectF1,linelayerids);
            if(features1.size()>0) {
                int routeid = Integer.parseInt(features1.get(0).id());
                routechangeListener( routeid);
            }
        }
    }

    void routechangeListener(int routeid){
        if(routeid<10){

                selectedroute=routeid;

                for(int i=0;i<directionapiresp.routes().size();i++){
                    if(selectedroute!=i) {
                        mapboxStyle.getLayer("" + i).setProperties(
                                PropertyFactory.lineWidth(7f),
                                PropertyFactory.lineColor(activity.getResources().getColor(R.color.alternateRoute)));
                    }
                }

                mapboxStyle.getLayer(routeid+"").setProperties(
                        PropertyFactory.lineWidth(8f),
                        PropertyFactory.lineColor(activity.getResources().getColor(R.color.seletedRoute)));

                if(dragUpListener!=null)
                dragUpListener.OnDragUpHeadLineChange();

            }

    }
 }


