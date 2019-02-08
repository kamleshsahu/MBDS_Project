package com.mapboxweather.kamleshsahu.mapboxdemo.Activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.FeatureGroupInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.RectF;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.UiThread;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.core.constants.Constants;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.Geometry;
import com.mapbox.geojson.LineString;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.Annotation;
import com.mapbox.mapboxsdk.annotations.Icon;
import com.mapbox.mapboxsdk.annotations.IconFactory;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.annotations.Polyline;
import com.mapbox.mapboxsdk.annotations.PolylineOptions;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.constants.MapboxConstants;
import com.mapbox.mapboxsdk.constants.Style;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.geometry.LatLngBounds;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.style.expressions.Expression;
import com.mapbox.mapboxsdk.style.layers.Layer;
import com.mapbox.mapboxsdk.style.layers.LineLayer;
import com.mapbox.mapboxsdk.style.layers.Property;
import com.mapbox.mapboxsdk.style.layers.PropertyFactory;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.mapboxsdk.style.sources.Source;
import com.mapboxweather.kamleshsahu.mapboxdemo.Adapter.DragupListAdapter_route;
import com.mapboxweather.kamleshsahu.mapboxdemo.Adapter.DragupListAdapter_weather;
import com.mapboxweather.kamleshsahu.mapboxdemo.Methods.Main;

import com.mapboxweather.kamleshsahu.mapboxdemo.Methods.unitConverter;
import com.mapboxweather.kamleshsahu.mapboxdemo.Methods.weatherIconMap;
import com.mapboxweather.kamleshsahu.mapboxdemo.Models.Item;
import com.mapboxweather.kamleshsahu.mapboxdemo.Models.MStep;
import com.mapboxweather.kamleshsahu.mapboxdemo.Models.Resp;
import com.mapboxweather.kamleshsahu.mapboxdemo.R;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.vipul.hp_hp.library.Layout_to_Image;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import static com.mapbox.geojson.LineString.fromJson;
import static com.mapbox.geojson.LineString.fromLngLats;
import static com.mapboxweather.kamleshsahu.mapboxdemo.Constants.MapboxKey;
import static com.mapboxweather.kamleshsahu.mapboxdemo.Constants.fastest_route;


/**
 * The most basic example of adding a map to an activity.
 */
public class SimpleMapViewActivity extends AppCompatActivity {

    private MapView mapView;
    public static Handler myItemhandler,myStephandler;



    Point sp=MainActivity.sp,dp=MainActivity.dp;
    DirectionsResponse directionapiresp=MainActivity.directionapiresp;
    int stepcount=0;
    int selectedroute=MainActivity.selectedroute;
    String timezone=MainActivity.timezone;
    String travelmode=MainActivity.travelmode;
    long jstarttime=MainActivity.jstart_date_millis+MainActivity.jstart_time_millis;

     android.app.AlertDialog.Builder bld;

    List<PolylineOptions> polylineOptionsList;
    List<Item> items;
    List<MStep> mSteps;
    List<Polyline> polylines;
    List<Marker> markersInterm;
    List<Marker> markersSteps;

    static SlidingUpPanelLayout slidingUpPanelLayout;
    static RecyclerView link;

    DragupListAdapter_route routeadapter;

    Menu menu;
    SharedPreferences.Editor editor;
    long interval=50000;
    int i=0;
    static MapboxMap mapboxMap;
    int totalsteps=0;

    public static ProgressDialog progress;
//    static final SimpleMapViewActivity cont=SimpleMapViewActivity.this;

    Boolean AlreadyGotError=false;

//    private static final String MARKER_SOURCE = "markers-source";
//    private static final String MARKER_STYLE_LAYER = "markers-style-layer";
//    private static final String MARKER_IMAGE = "custom-marker";

    String layerids[],linelayerids[];
    List<String> layeridlist;
    Boolean layeridCreated;
    List<Source> markersourcelist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_simple_mapview);
        // Mapbox access token is configured here. This needs to be called either in your application
        // object or in the same activity which contains the mapview.
        Mapbox.getInstance(this, MapboxKey);

        // This contains the MapView in XML and needs to be called after the access token is configured.
        markersourcelist=new ArrayList<>();
        layeridCreated = false;
        linelayerids=new String[directionapiresp.routes().size()];
        layeridlist=new ArrayList<>();
        items = new ArrayList<>();
        mSteps = new ArrayList<>();
        polylineOptionsList = new ArrayList<>();
        markersSteps = new ArrayList<>();
        markersInterm = new ArrayList<>();
        polylines = new ArrayList<>();
        AlreadyGotError=false;
        progress=new ProgressDialog(this);
        ////////////////////////////////////////////////////////////
        //menu
        setTitle("MapView");
        editor = getSharedPreferences("distance", MODE_PRIVATE).edit();
        SharedPreferences prefs = getSharedPreferences("distance", MODE_PRIVATE);
        int a = prefs.getInt("10", 0);
        switch (a) {
            case 10:
                //  Toast.makeText(mApp, "10", Toast.LENGTH_SHORT).show();

                try {
                    Handler handler1 = new Handler();
                    handler1.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            MenuItem item = menu.findItem(R.id.km10);
                            interval = 10000;
                            i = 1;
                            item.setChecked(true);
                        }
                    }, 2000);
                } catch (Exception e) {

                }
                break;
            case 20:
                //  Toast.makeText(mApp, "20", Toast.LENGTH_SHORT).show();
                try {
                    Handler handler1 = new Handler();
                    handler1.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            MenuItem item = menu.findItem(R.id.km20);
                            item.setChecked(true);
                            interval = 20000;
                            i = 2;
                        }
                    }, 2000);
                } catch (Exception e) {

                }

                break;
            case 30:
                //  Toast.makeText(mApp, "30", Toast.LENGTH_SHORT).show();
                try {
                    Handler handler1 = new Handler();
                    handler1.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            MenuItem item = menu.findItem(R.id.km30);
                            item.setChecked(true);
                            interval = 30000;
                            i = 3;
                        }
                    }, 2000);
                } catch (Exception e) {

                }
                break;
            case 40:
                // Toast.makeText(mApp, "40", Toast.LENGTH_SHORT).show();
                try {
                    Handler handler1 = new Handler();
                    handler1.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            MenuItem item = menu.findItem(R.id.km40);
                            item.setChecked(true);
                            interval = 40000;
                            i = 4;
                        }
                    }, 2000);
                } catch (Exception e) {

                }
                break;
            case 50:
                // Toast.makeText(mApp, "50", Toast.LENGTH_SHORT).show();
                try {
                    Handler handler1 = new Handler();
                    handler1.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            MenuItem item = menu.findItem(R.id.km50);
                            item.setChecked(true);
                            interval = 50000;
                            i = 5;
                        }
                    }, 2000);
                } catch (Exception e) {

                }
                break;
            default:
                //   Toast.makeText(mApp, "0", Toast.LENGTH_SHORT).show();
        }
////////////////////////////////////////////////////////////////////


////////////////////////////////////////////////////////////
        //drag up list adapter
        slidingUpPanelLayout = findViewById(R.id.sliding_layout);
        slidingUpPanelLayout.setPanelHeight(0);


        link = (RecyclerView) findViewById(R.id.dragup_list_recycler);
        link.setLayoutManager(new LinearLayoutManager(this));

//        sp = Point.fromLngLat(-105.2705, 40.015);
//        dp = Point.fromLngLat(-104.9653, 39.7348);
///////////////////////////////////////////////////////////////// /


        mapView = (MapView) findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(new OnMapReadyCallback() {


            @Override
            public void onMapReady(final MapboxMap mapboxMap) {

                // Customize map with markers, polylines, etc.

                SimpleMapViewActivity.mapboxMap = mapboxMap;
                drawRoute();


                mapboxMap.addOnMapClickListener(mapClickListener);

//                mapboxMap.setOnMarkerClickListener(new MapboxMap.OnMarkerClickListener() {
//                    @Override
//                    public boolean onMarkerClick(@NonNull Marker marker) {
//
//                        marker.getPosition();
//                        marker.hideInfoWindow();
//                       Log.i("marker id ",marker.getId()+"");
////                        if(marker.getId()>=1000){
////                            Log.i("marker clicked","step marker clicked");
////                            new CustomDialogClass(SimpleMapViewActivity.this,mSteps.get((int)marker.getId()-1000)).show();
////                        }else{
////                            Log.i("marker clicked","item marker clicked");
////                            new CustomDialogClass(SimpleMapViewActivity.this,items.get((int)marker.getId())).show();
////                            }
//
//                        if(marker.getTitle().startsWith("S")){
//                            Log.i("marker clicked","step marker clicked");
//                            int index= Integer.parseInt(marker.getTitle().substring(1));
//                            new CustomDialogClass(SimpleMapViewActivity.this,mSteps.get(index)).show();
//                        }else if(marker.getTitle().startsWith("I")){
//                            Log.i("marker clicked","item marker clicked");
//                            int index= Integer.parseInt(marker.getTitle().substring(1));
//                            new CustomDialogClass(SimpleMapViewActivity.this,items.get(index)).show();
//                        }
//                        return false;
//                    }
//                });
//                mapboxMap.setOnPolylineClickListener(polylineClickListener);
            }

        });


        myItemhandler = new Handler(myIntermediatePointsCallback);

        myStephandler = new Handler(myStepsHandlerCallback);




 ///////////////////////////////////////////////////////////////////////////////////////////////////

                routeadapter = new DragupListAdapter_route(getApplicationContext(), directionapiresp.routes().get(0));
                link.setAdapter(routeadapter);
                if (directionapiresp.routes().get(selectedroute).legs().get(0).distance() != null) {
                    slidingUpPanelLayout.setPanelHeight(getApplicationContext().getResources().getDimensionPixelSize(R.dimen.dragupsize));
                    Update_dragUpHeadline();
                }



//////////////////////////////////////////////////////////////////////////////////
   //     bld = new AlertDialog.Builder(cont);



    }
///////////////////////////////////////////////////////////////

    void drawRoute(){
        mapboxMap.clear();
        mapboxMap.addMarker(new MarkerOptions()
                .position(new LatLng(sp.latitude(), sp.longitude()))

        );
        mapboxMap.addMarker(new MarkerOptions()
                .position(new LatLng(dp.latitude(), dp.longitude()))

        );

//        Bitmap icon1 = BitmapFactory.decodeResource(
//                SimpleMapViewActivity.this.getResources(), R.drawable.pina);
//
//        Bitmap icon2 = BitmapFactory.decodeResource(
//                SimpleMapViewActivity.this.getResources(),R.drawable.pinb);




        layeridCreated = false;

        linelayerids=new String[directionapiresp.routes().size()];

        setCameraWithCoordinationBounds();
        //add route(s) to the map.

//        MapActivity.distance.setText("(" + directionapi.routes[selectedroute].legs[0].distance.humanReadable+ ")");
//        MapActivity.duration.setText(directionapi.routes[selectedroute].legs[0].durationInTraffic!=null?directionapi.routes[selectedroute].legs[0].durationInTraffic.humanReadable:directionapi.routes[selectedroute].legs[0].duration.humanReadable);
//        if (directionapi.routes[selectedroute].legs[0].duration.humanReadable != null) {
//            slidingUpPanelLayout.setPanelHeight(context.getResources().getDimensionPixelSize(R.dimen.dragupsize));
//        }

//        Polyline selectedPolyline = null;
//        PolylineOptions SelectedpolyOptions = null;
//        polylines=new ArrayList<>();
//
//        for (int i = 0; i < directionapiresp.routes().size(); i++) {
//            if (i != selectedroute) {
//
//                List<LatLng> points = new ArrayList<>();
//                List<Point> coords = LineString.fromPolyline(directionapiresp.routes().get(i).geometry(), Constants.PRECISION_6).coordinates();
//                //    new task().execute();
//
//                for (Point point : coords) {
//                    points.add(new LatLng(point.latitude(), point.longitude()));
//                }
//
//                PolylineOptions polyOptions = new PolylineOptions();
//                polyOptions.color(getApplicationContext().getResources().getColor(R.color.alternateRoute));
//                polyOptions.width(7);
//                polyOptions.addAll(points);
//
//                Polyline polyline =mapboxMap.addPolyline(polyOptions);
//                polylines.add(polyline);
//
//                polylineOptionsList.add(polyOptions);
//            }else {
//                List<LatLng> points = new ArrayList<>();
//                List<Point> coords = LineString.fromPolyline(directionapiresp.routes().get(i).geometry(), Constants.PRECISION_6).coordinates();
//                //    new task().execute();
//
//                for (Point point : coords) {
//                    points.add(new LatLng(point.latitude(), point.longitude()));
//                }
//
//
//                SelectedpolyOptions = new PolylineOptions();
//                SelectedpolyOptions.color(getApplicationContext().getResources().getColor(R.color.seletedRoute));
//                SelectedpolyOptions.width(9);
//                SelectedpolyOptions.addAll(points);
//                polylineOptionsList.add(SelectedpolyOptions);
//
//            }
//        }
//
//        if(SelectedpolyOptions !=null) {
//            selectedPolyline = mapboxMap.addPolyline(SelectedpolyOptions);
//            polylines.add(selectedPolyline);
//       }
//
//        polylineOptionsList.add(SelectedpolyOptions);
//        mapboxMap.addPolylines(polylineOptionsList);




        for(int i=0;i<directionapiresp.routes().size();i++){
            String id="p"+i;
            linelayerids[i]=id;
       //     layeridlist.add(id);
            if(i!=selectedroute)
            addPolyline(directionapiresp.routes().get(i).geometry(),id,getResources().getColor(R.color.alternateRoute));
        }
        addPolyline(directionapiresp.routes().get(i).geometry(),"p"+selectedroute,getResources().getColor(R.color.seletedRoute));

//        addMarkers(R.drawable.pina,"img1","sp",sp,"sp","sp");
//        addMarkers(R.drawable.pinb,"img2","dp",dp,"dp","dp");

    }

    MapboxMap.OnMapClickListener mapClickListener= new MapboxMap.OnMapClickListener() {

        @Override
        public void onMapClick(@NonNull LatLng point) {
            Log.d("map clicked", "map clicked");
// Convert LatLng coordinates to screen pixel and only query the rendered features.
            final PointF pointf = mapboxMap.getProjection().toScreenLocation(point);

            RectF rectF = new RectF(pointf.x - 10, pointf.y - 10, pointf.x + 10, pointf.y + 10);
    //        String layerids[] = {"S1", "S2","S3","S4","S5","S6","S7","S8","S9"};
    //        List<String> layeridlist=new ArrayList<>();
            if(!layeridCreated) {
 //               Collections.reverse(layeridlist);
                layerids = layeridlist.toArray(new String[layeridlist.size()]);
            }
            List<Feature> features = mapboxMap.queryRenderedFeatures(rectF,layerids);



            if (features.size() > 0) {
                Feature feature = features.get(0);

                Log.i("featute id :", feature.id());

                if (feature.id().startsWith("S")) {
                    Log.i("marker clicked :", "step marker clicked");
                    int index = Integer.parseInt(feature.id().substring(1));
                    new CustomDialogClass(SimpleMapViewActivity.this, mSteps.get(index)).show();
                } else if (feature.id().startsWith("I")) {
                    Log.i("marker clicked :", "item marker clicked");
                    int index = Integer.parseInt(feature.id().substring(1));
                    new CustomDialogClass(SimpleMapViewActivity.this, items.get(index)).show();
//                }else if(feature.id().startsWith("p")){
//                            String id=features.get(0).id();
//                            selectedroute=Integer.parseInt(id.substring(1));
//
//                            for(int i=0;i<directionapiresp.routes().size();i++){
//                                if(selectedroute!=i) {
//                                    mapboxMap.getLayer("p" + i).setProperties(
//                                            PropertyFactory.lineWidth(7f),
//                                            PropertyFactory.lineColor(getResources().getColor(R.color.alternateRoute)));
//                                }
//                            }
//
//                            mapboxMap.getLayer(id).setProperties(
//                                    PropertyFactory.lineWidth(8f),
//                                    PropertyFactory.lineColor(getResources().getColor(R.color.seletedRoute)));
////
               }

                else{
                      routechangeListener(pointf);
                }

            }else{
                //System.out.println(" else part else part");
                routechangeListener(pointf);
            }

        }
    };


    void routechangeListener(PointF pointf){
 //       //System.out.println("feature id not matching ");
        RectF rectF1 = new RectF(pointf.x - 20, pointf.y - 20, pointf.x + 20, pointf.y + 20);
        List<Feature> features1 = mapboxMap.queryRenderedFeatures(rectF1,linelayerids);
        if(features1.size()>0){
 //           //System.out.println("line data :"+features1.get(0).id());
            if(features1.get(0).id().startsWith("p")){
                String id=features1.get(0).id();
                selectedroute=Integer.parseInt(id.substring(1));

                for(int i=0;i<directionapiresp.routes().size();i++){
                    if(selectedroute!=i) {
                        mapboxMap.getLayer("p" + i).setProperties(
                                PropertyFactory.lineWidth(7f),
                                PropertyFactory.lineColor(getResources().getColor(R.color.alternateRoute)));
                    }
                }

                mapboxMap.getLayer(id).setProperties(
                        PropertyFactory.lineWidth(8f),
                        PropertyFactory.lineColor(getResources().getColor(R.color.seletedRoute)));
                Update_dragUpHeadline();
            }
        }
    }

    Handler.Callback myIntermediatePointsCallback=new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {


            if (msg.obj != null) {
                //System.out.println("received interm weather data :");
                Resp resp = (Resp) msg.obj;
                Item item=resp.getIntermediatePointData();
                if (item != null) {
                    items.add(item);
                    MarkerOptions options = new MarkerOptions();
                    options.setPosition(item.getPoint());

                    LinearLayout relativeLayout = findViewById(R.id.show);
//                    TextView time = findViewById(R.id.step_time);
                    TextView weather = findViewById(R.id.step_weather);
                    ImageView step_icon = findViewById(R.id.step_icon);
//                    TextView location_name = findViewById(R.id.location_name);
                    Layout_to_Image layout_to_image = new Layout_to_Image(getApplicationContext(), relativeLayout);


//                    String time_data[] = item.getArrtime().split(",", 2);
//                    if (time_data.length >= 2)
//                        time.setText(time_data[0] + "\n" + time_data[1]);
//                    else time.setText(item.getArrtime());
//                    if (item.getLname() != null) {
//                        String lname[] = item.getLname().split(",");
//                        if (lname.length >= 2)
//                            location_name.setText(lname[0].length() < 20 ? lname[0] : lname[0].substring(0, 19) + "..,\n" + lname[1]);
//                        else {
//                            location_name.setText(lname[0]);
//                        }
//                    }

//                    ImageView image = step_icon;
//                    new bitmapfromstring(item.getWlist().getIcon(), image, weather);
//                    Bitmap bitmap = layout_to_image.convert_layout();
//
////                    Icon icon = IconFactory.getInstance(SimpleMapViewActivity.this)
////                            .fromBitmap(bitmap);
//                    Icon icon = IconFactory.getInstance(SimpleMapViewActivity.this)
//                            .fromResource(new weatherIconMap().getWeatherResource(item.getWlist().getIcon()));
//
//                    options.setIcon(icon);
//                    Marker marker= mapboxMap.addMarker(options);
//                      markersInterm.add(marker);
//                   // marker.setId(markersInterm.indexOf(marker));
//                    marker.setTitle("I"+markersInterm.indexOf(marker));

                    String id="I"+items.indexOf(item);
                    layeridlist.add(id);
                    addMarkers(new weatherIconMap().getWeatherResource(item.getWlist().getIcon()),id,id,Point.fromLngLat(item.getPoint().getLongitude(),item.getPoint().getLatitude()),id,id);

                }else {
                    Log.e("error","item null,item handler");
                    progress.dismiss();
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    displayError(resp.getError().getHeading(),resp.getError().getMessage());
                }
            }else{
                Log.e("error","message obj null,item handler");
                progress.dismiss();
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                displayError("unknown error","error while finding weather");
            }

            return false;
        }
    };

    Handler.Callback myStepsHandlerCallback=new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {


            if (msg.obj != null) {
                //System.out.println("received step weather data :");

                Resp resp=(Resp)msg.obj;
                MStep mstep = resp.getmStep();

                if (mstep != null) {
                    mSteps.add(mstep);

                    MarkerOptions options = new MarkerOptions();
                    options.setPosition(new LatLng(mstep.getStep().maneuver().location().latitude(), mstep.getStep().maneuver().location().longitude()));
                    LinearLayout relativeLayout = findViewById(R.id.show);
//                    TextView time = findViewById(R.id.step_time);
                    TextView weather = findViewById(R.id.step_weather);
                    ImageView step_icon = findViewById(R.id.step_icon);
//                   TextView location_name = findViewById(R.id.location_name);
                    Layout_to_Image layout_to_image = new Layout_to_Image(getApplicationContext(), relativeLayout);
//
//
////                    String time_data[] = mstep.getArrtime().split(",", 2);
////                    if (time_data.length >= 2)
////                        time.setText(time_data[0] + "\n" + time_data[1]);
////                    else time.setText(mstep.getArrtime());
//
//

//                    ImageView image = step_icon;
//                    new bitmapfromstring(mstep.getWlist().getIcon(), image, weather);
//                    Bitmap bitmap = layout_to_image.convert_layout();
////
//                    Icon icon = IconFactory.getInstance(SimpleMapViewActivity.this)
//                            .fromResource(new weatherIconMap().getWeatherResource(mstep.getWlist().getIcon()));
//
//                    options.setIcon(icon);
//                    Marker marker= mapboxMap.addMarker(options);
//                    markersSteps.add(marker);
//
//                    marker.setTitle("S"+markersSteps.indexOf(marker));
                     String id="S"+mstep.getPos();
                     layeridlist.add(id);
                     addMarkers(new weatherIconMap().getWeatherResource(mstep.getWlist().getIcon()),id,id,mstep.getStep().maneuver().location(),id,id);

                    if (--stepcount <= 0) {
                        Collections.sort(mSteps, (o1, o2) -> o1.getPos().compareTo(o2.getPos()));
                        link.setAdapter(new DragupListAdapter_weather(getApplicationContext(), mSteps));
                      new Handler().postDelayed(new Runnable() {
                          @Override
                          public void run() {
                              progress.dismiss();
                              getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                          }
                      },1000);

                    }

                }else {
                    if(progress.isShowing()){
                        progress.dismiss();
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
//                    try {
//
//                        displayError(resp.getError().getHeading(), resp.getError().getMessage());
//                    }catch (Exception e){
//                        Log.e("diplay error :","mstep obj is coming null");
//                        e.printStackTrace();
//                    }
                    }
                }
            }

            return false;
        }
    };


//    MapboxMap.OnPolylineClickListener polylineClickListener=new MapboxMap.OnPolylineClickListener() {
//        @Override
//        public void onPolylineClick(@NonNull Polyline polyline) {
//            int val=0;
//
//            for(int k=0;k<polylines.size();k++){
//                if(polylines.get(k).equals(polyline)){
//                    val=k;
//                }
//            }
//
//            if(selectedroute!=val) {
//                for (int k = 0; k < polylines.size(); k++) {
//                    polylines.get(k).remove();
//                    if (!polylines.get(k).equals(polyline)) {
//                        polylineOptionsList.get(k).color(getResources().getColor(R.color.alternateRoute));
//                        polylineOptionsList.get(k).width(7);
//                        Polyline p = mapboxMap.addPolyline(polylineOptionsList.get(k));
//                        polylines.set(k, p);
//                    }
//                }
//
//                int prevroute = selectedroute;
//                selectedroute = val;
//                routeadapter = new DragupListAdapter_route(getApplicationContext(), directionapiresp.routes().get(selectedroute));
//                routeadapter.notifyDataSetChanged();
//
//                polylineOptionsList.get(val).color(getResources().getColor(R.color.seletedRoute));
//                polylineOptionsList.get(val).width(9);
//                Polyline selectedPolyline = mapboxMap.addPolyline(polylineOptionsList.get(val));
//                polylines.set(val, selectedPolyline);
//
//
//                Update_dragUpHeadline();
//
//                if (selectedroute != prevroute) {
//                    for (int k = 0; k < markersSteps.size(); k++) {
//                        markersSteps.get(k).remove();
//                    }
//                    for (int k = 0; k < markersInterm.size(); k++) {
//                        markersInterm.get(k).remove();
//                    }
//                }
//            }
//        }
//    };


    void setCameraWithCoordinationBounds() {
        LatLngBounds bounds = new LatLngBounds.Builder()
                .include(new LatLng(sp.latitude(), sp.longitude()))
                .include(new LatLng(dp.latitude(), dp.longitude()))
                .build();
        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;
        int padding = (int) (width * 0.22);
        mapboxMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, padding));

    }

    void Update_dragUpHeadline(){
        ((TextView)findViewById(R.id.route_name)).setText(directionapiresp.routes().get(selectedroute).legs().get(0).summary());
        ((TextView)findViewById(R.id.duration)).setText(new unitConverter().durationBeautify(directionapiresp.routes().get(selectedroute).duration().longValue()));
        ((TextView)findViewById(R.id.distance)).setText("("+new unitConverter().metertoMiles(directionapiresp.routes().get(selectedroute).distance().longValue())+")");
        if(selectedroute==0)((TextView)findViewById(R.id.fastestroute)).setText(fastest_route);
        else ((TextView)findViewById(R.id.fastestroute)).setText("");
    }




    public void showWeather(View view){
        AlreadyGotError=false;
        progress=new ProgressDialog(this);
        progress.setTitle("Loading Weather Data...");
        progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progress.setIndeterminate(false);
        progress.setProgress(0);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        progress.getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        progress.show();

        totalsteps=directionapiresp.routes().get(selectedroute).legs().get(0).steps().size();
        removeWeatherIcons();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                new Task().execute();
            }
        },500);

    }


    void removeWeatherIcons(){

        for(int i=0;i<layeridlist.size();i++) {
            mapboxMap.removeLayer(layeridlist.get(i));
            mapboxMap.removeImage(layeridlist.get(i));

        }

        for(int i=0;i<markersourcelist.size();i++){
            mapboxMap.removeSource(markersourcelist.get(i));
        }



        layeridCreated = false;
        //System.out.println("all removed");
        layeridlist=new ArrayList<>();
        markersourcelist=new ArrayList<>();
    }

    class Task extends AsyncTask<Object,Object,Object>{

        @Override
        protected Object doInBackground(Object[] objects) {
            new Main(directionapiresp.routes().get(selectedroute),travelmode,timezone,jstarttime,interval).execute();
            return null;
        }
    }
    // Add the mapView lifecycle to the activity's lifecycle methods
    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
//        mapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mapView.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mapactivity_menu, menu);
        this.menu=menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.km10:
                item.setChecked(true);
                interval=10000;
                i=1;
                editor.putInt("10",10);
                editor.apply();

                return true;
            case R.id.km20:
                item.setChecked(true);
                interval=20000;
                i=2;
                editor.putInt("10",20);
                editor.apply();

                return true;
            case R.id.km30:
                item.setChecked(true);
                interval=30000;
                i=3;
                editor.putInt("10",30);
                editor.apply();

                return true;
            case R.id.km40:
                item.setChecked(true);
                interval=40000;
                i=4;
                editor.putInt("10",40);
                editor.apply();

                return true;
            case R.id.km50:
                item.setChecked(true);
                interval=50000;
                i=5;
                editor.putInt("10",50);
                editor.apply();

                return true;
            case R.id.action_retry:
               // weatherApi = new WeatherApi();
               // weatherApi.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                //               Toast.makeText(this, "Fetching Weather...", Toast.LENGTH_SHORT).show();

                drawRoute();
                showWeather(null);
                return true;

            case R.id.action_clr:
                //              Toast.makeText(this, "clear", Toast.LENGTH_SHORT).show();

                recreate();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

     void displayError(String title, String msg){
           if(!AlreadyGotError) {
               AlreadyGotError=true;
               int maxLength = (msg.length() < 40) ? msg.length() : 40;
               msg = msg.substring(0, maxLength);
               bld = new AlertDialog.Builder(SimpleMapViewActivity.this);
               bld.setMessage(msg);
               bld.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {
                       //System.out.println(" display error dialog dismiss");
                       dialog.dismiss();
                       bld = null;
                   }
               });
               bld.setTitle(title);

               Log.d("TAG", "Showing alert dialog: " + msg);
               Dialog dialog = bld.create();
               //   dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);

               dialog.show();
           }
    };




    private void addMarkers(int iconid,String MARKER_IMAGE,String MARKER_SOURCE,Point point,String MARKER_STYLE_LAYER,String index) {
        List<Feature> features = new ArrayList<>();
        /* Source: A data source specifies the geographic coordinate where the image marker gets placed. */
      //  features.add(Feature.fromGeometry(Point.fromLngLat(-78.7448, 40.2489)));


        features.add(Feature.fromGeometry(point, null,index));


        FeatureCollection featureCollection = FeatureCollection.fromFeatures(features);
        GeoJsonSource source = new GeoJsonSource(MARKER_SOURCE, featureCollection);





                  mapboxMap.removeSource(source.getId());
                  mapboxMap.addSource(source);



        /* Style layer: A style layer ties together the source and image and specifies how they are displayed on the map. */
        SymbolLayer markerStyleLayer = new SymbolLayer(MARKER_STYLE_LAYER, MARKER_SOURCE)
                .withProperties(
                        PropertyFactory.iconAllowOverlap(true),
                        PropertyFactory.iconImage(MARKER_IMAGE)


                );

        Bitmap icon = BitmapFactory.decodeResource(
               SimpleMapViewActivity.this.getResources(),iconid);
        mapboxMap.addLayer(markerStyleLayer);
     //  mapboxMap.addLayerAbove(markerStyleLayer,"p"+selectedroute);
        mapboxMap.addImage(MARKER_IMAGE,icon);

    }


    private void addPolyline(String poly, String LINE_LAYER_ID, int color) {
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


  //      mapboxMap.addLayer(markerStyleLayer);

          mapboxMap.addLayer(lineLayer);
          mapboxMap.addSource(source);

    }

}
