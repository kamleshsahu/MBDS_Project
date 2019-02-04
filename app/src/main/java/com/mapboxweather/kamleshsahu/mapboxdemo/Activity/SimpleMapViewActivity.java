package com.mapboxweather.kamleshsahu.mapboxdemo.Activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
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

import com.mapbox.api.directions.v5.DirectionsCriteria;
import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.api.directions.v5.models.DirectionsRoute;
import com.mapbox.core.constants.Constants;
import com.mapbox.geojson.GeoJson;
import com.mapbox.geojson.LineString;
import com.mapbox.geojson.Point;
import com.mapbox.geojson.utils.GeoJsonUtils;
import com.mapbox.geojson.utils.PolylineUtils;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.Icon;
import com.mapbox.mapboxsdk.annotations.IconFactory;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.annotations.Polyline;
import com.mapbox.mapboxsdk.annotations.PolylineOptions;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.geometry.LatLngBounds;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.style.layers.Property;
import com.mapbox.mapboxsdk.style.sources.GeoJsonOptions;
import com.mapboxweather.kamleshsahu.mapboxdemo.Adapter.DragupListAdapter_route;
import com.mapboxweather.kamleshsahu.mapboxdemo.Adapter.DragupListAdapter_weather;
import com.mapboxweather.kamleshsahu.mapboxdemo.Methods.Main;
import com.mapboxweather.kamleshsahu.mapboxdemo.Methods.RouteFinder;
import com.mapboxweather.kamleshsahu.mapboxdemo.Methods.bitmapfromstring;
import com.mapboxweather.kamleshsahu.mapboxdemo.Methods.unitConverter;
import com.mapboxweather.kamleshsahu.mapboxdemo.Methods.weatherIconMap;
import com.mapboxweather.kamleshsahu.mapboxdemo.Models.Item;
import com.mapboxweather.kamleshsahu.mapboxdemo.Models.MStep;
import com.mapboxweather.kamleshsahu.mapboxdemo.Models.Resp;
import com.mapboxweather.kamleshsahu.mapboxdemo.R;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.vipul.hp_hp.library.Layout_to_Image;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.mapboxweather.kamleshsahu.mapboxdemo.Constants.ErrorHeading;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_simple_mapview);
        // Mapbox access token is configured here. This needs to be called either in your application
        // object or in the same activity which contains the mapview.
        Mapbox.getInstance(this, MapboxKey);

        // This contains the MapView in XML and needs to be called after the access token is configured.

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
                mapboxMap.setOnPolylineClickListener(polylineClickListener);

                mapboxMap.setOnMarkerClickListener(new MapboxMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(@NonNull Marker marker) {

                       Log.i("marker id ",marker.getId()+"");
//                        if(marker.getId()>=1000){
//                            Log.i("marker clicked","step marker clicked");
//                            new CustomDialogClass(SimpleMapViewActivity.this,mSteps.get((int)marker.getId()-1000)).show();
//                        }else{
//                            Log.i("marker clicked","item marker clicked");
//                            new CustomDialogClass(SimpleMapViewActivity.this,items.get((int)marker.getId())).show();
//                            }

                        if(marker.getTitle().startsWith("S")){
                            Log.i("marker clicked","step marker clicked");
                            int index= Integer.parseInt(marker.getTitle().substring(1));
                            new CustomDialogClass(SimpleMapViewActivity.this,mSteps.get(index)).show();
                        }else if(marker.getTitle().startsWith("I")){
                            Log.i("marker clicked","item marker clicked");
                            int index= Integer.parseInt(marker.getTitle().substring(1));
                            new CustomDialogClass(SimpleMapViewActivity.this,items.get(index)).show();
                        }
                        return false;
                    }
                });

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
        setCameraWithCoordinationBounds();
        //add route(s) to the map.

//        MapActivity.distance.setText("(" + directionapi.routes[selectedroute].legs[0].distance.humanReadable+ ")");
//        MapActivity.duration.setText(directionapi.routes[selectedroute].legs[0].durationInTraffic!=null?directionapi.routes[selectedroute].legs[0].durationInTraffic.humanReadable:directionapi.routes[selectedroute].legs[0].duration.humanReadable);
//        if (directionapi.routes[selectedroute].legs[0].duration.humanReadable != null) {
//            slidingUpPanelLayout.setPanelHeight(context.getResources().getDimensionPixelSize(R.dimen.dragupsize));
//        }

        Polyline selectedPolyline = null;
        PolylineOptions SelectedpolyOptions = null;
        polylines=new ArrayList<>();

        for (int i = 0; i < directionapiresp.routes().size(); i++) {
            if (i != selectedroute) {

                List<LatLng> points = new ArrayList<>();
                List<Point> coords = LineString.fromPolyline(directionapiresp.routes().get(i).geometry(), Constants.PRECISION_6).coordinates();
                //    new task().execute();

                for (Point point : coords) {
                    points.add(new LatLng(point.latitude(), point.longitude()));
                }

                PolylineOptions polyOptions = new PolylineOptions();
                polyOptions.color(getApplicationContext().getResources().getColor(R.color.alternateRoute));
                polyOptions.width(7);
                polyOptions.addAll(points);

                Polyline polyline =mapboxMap.addPolyline(polyOptions);
                polylines.add(polyline);

                polylineOptionsList.add(polyOptions);
            }else {
                List<LatLng> points = new ArrayList<>();
                List<Point> coords = LineString.fromPolyline(directionapiresp.routes().get(i).geometry(), Constants.PRECISION_6).coordinates();
                //    new task().execute();

                for (Point point : coords) {
                    points.add(new LatLng(point.latitude(), point.longitude()));
                }


                SelectedpolyOptions = new PolylineOptions();
                SelectedpolyOptions.color(getApplicationContext().getResources().getColor(R.color.seletedRoute));
                SelectedpolyOptions.width(9);
                SelectedpolyOptions.addAll(points);
                polylineOptionsList.add(SelectedpolyOptions);

            }
        }

        if(SelectedpolyOptions !=null) {
            selectedPolyline = mapboxMap.addPolyline(SelectedpolyOptions);
            polylines.add(selectedPolyline);
       }

//        polylineOptionsList.add(SelectedpolyOptions);
 //       mapboxMap.addPolylines(polylineOptionsList);

    }

    Handler.Callback myIntermediatePointsCallback=new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {


            if (msg.obj != null) {
                System.out.println("received interm weather data :");
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

                    ImageView image = step_icon;
                    new bitmapfromstring(item.getWlist().getIcon(), image, weather);
                    Bitmap bitmap = layout_to_image.convert_layout();

//                    Icon icon = IconFactory.getInstance(SimpleMapViewActivity.this)
//                            .fromBitmap(bitmap);
                    Icon icon = IconFactory.getInstance(SimpleMapViewActivity.this)
                            .fromResource(new weatherIconMap().getWeatherResource(item.getWlist().getIcon()));

                    options.setIcon(icon);
                    Marker marker= mapboxMap.addMarker(options);
                    markersInterm.add(marker);
                   // marker.setId(markersInterm.indexOf(marker));
                    marker.setTitle("I"+markersInterm.indexOf(marker));
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
                System.out.println("received step weather data :");

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
                    ImageView image = step_icon;
                    new bitmapfromstring(mstep.getWlist().getIcon(), image, weather);
                    Bitmap bitmap = layout_to_image.convert_layout();
//
                    Icon icon = IconFactory.getInstance(SimpleMapViewActivity.this)
                            .fromResource(new weatherIconMap().getWeatherResource(mstep.getWlist().getIcon()));
//                    Icon icon = IconFactory.getInstance(SimpleMapViewActivity.this)
//                            .fromBitmap(bitmap);
                    options.setIcon(icon);
                    Marker marker= mapboxMap.addMarker(options);
                    markersSteps.add(marker);
//                    marker.setId(1000+markersSteps.indexOf(marker));
                    marker.setTitle("S"+markersSteps.indexOf(marker));

                    if (--stepcount <= 0) {
                        Collections.sort(mSteps, (o1, o2) -> o1.getPos().compareTo(o2.getPos()));
                        link.setAdapter(new DragupListAdapter_weather(getApplicationContext(), mSteps));

                        progress.dismiss();
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
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


    MapboxMap.OnPolylineClickListener polylineClickListener=new MapboxMap.OnPolylineClickListener() {
        @Override
        public void onPolylineClick(@NonNull Polyline polyline) {
            int val=0;

            for(int k=0;k<polylines.size();k++){
                if(polylines.get(k).equals(polyline)){
                    val=k;
                }
            }

            if(selectedroute!=val) {
                for (int k = 0; k < polylines.size(); k++) {
                    polylines.get(k).remove();
                    if (!polylines.get(k).equals(polyline)) {
                        polylineOptionsList.get(k).color(getResources().getColor(R.color.alternateRoute));
                        polylineOptionsList.get(k).width(7);
                        Polyline p = mapboxMap.addPolyline(polylineOptionsList.get(k));
                        polylines.set(k, p);
                    }
                }

                int prevroute = selectedroute;
                selectedroute = val;
                routeadapter = new DragupListAdapter_route(getApplicationContext(), directionapiresp.routes().get(selectedroute));
                routeadapter.notifyDataSetChanged();

                polylineOptionsList.get(val).color(getResources().getColor(R.color.seletedRoute));
                polylineOptionsList.get(val).width(9);
                Polyline selectedPolyline = mapboxMap.addPolyline(polylineOptionsList.get(val));
                polylines.set(val, selectedPolyline);


                Update_dragUpHeadline();

                if (selectedroute != prevroute) {
                    for (int k = 0; k < markersSteps.size(); k++) {
                        markersSteps.get(k).remove();
                    }
                    for (int k = 0; k < markersInterm.size(); k++) {
                        markersInterm.get(k).remove();
                    }
                }
            }
        }
    };


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

        new Task().execute();
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
                       System.out.println(" display error dialog dismiss");
                       dialog.dismiss();
                       bld = null;
                   }
               });
               bld.setTitle(title);
               bld.setOnCancelListener(new DialogInterface.OnCancelListener() {
                   @Override
                   public void onCancel(DialogInterface dialog) {
                       System.out.println(" display error dialog dismiss");
                       dialog.dismiss();
                       bld = null;
                   }
               });
               Log.d("TAG", "Showing alert dialog: " + msg);
               Dialog dialog = bld.create();
               //   dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);

               dialog.show();
           }
    };


}
