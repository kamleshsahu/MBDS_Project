package com.mapboxweather.kamleshsahu.mapboxdemo.Activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
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
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.style.sources.Source;
import com.mapboxweather.kamleshsahu.mapboxdemo.Adapter.DragupListAdapter_route;
import com.mapboxweather.kamleshsahu.mapboxdemo.Adapter.DragupListAdapter_weather;

import com.mapboxweather.kamleshsahu.mapboxdemo.Interface.DragUpChangeListener;
import com.mapboxweather.kamleshsahu.mapboxdemo.Methods.weatherUI_utils;
import com.mapboxweather.kamleshsahu.mapboxdemo.Methods.unitConverter;
import com.mapboxweather.kamleshsahu.mapboxdemo.Methods.weatherIconMap;

import com.mapboxweather.kamleshsahu.mapboxdemo.R;
import com.mapboxweather.kamleshsahu.mapboxdemo.WeatherService.Interface.WeatherServiceListener;
import com.mapboxweather.kamleshsahu.mapboxdemo.WeatherService.Models.mPoint;
import com.mapboxweather.kamleshsahu.mapboxdemo.WeatherService.Models.mStep;
import com.mapboxweather.kamleshsahu.mapboxdemo.WeatherService.WeatherService;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.mapboxweather.kamleshsahu.mapboxdemo.Constants.MapboxKey;
import static com.mapboxweather.kamleshsahu.mapboxdemo.Constants.fastest_route;
import static com.mapboxweather.kamleshsahu.mapboxdemo.Methods.MaptoList.maptolist;


/**
 * The most basic example of adding a map to an activity.
 */
public class SimpleMapViewActivity extends AppCompatActivity

    implements WeatherServiceListener, OnMapReadyCallback,Style.OnStyleLoaded,
        DragUpChangeListener
{

    private MapView mapView;

    Task getWeatherTask;


    Point sp=MainActivity.sp,dp=MainActivity.dp;
    DirectionsResponse directionapiresp=MainActivity.directionapiresp;

    int selectedroute=MainActivity.selectedroute;
    String timezone=MainActivity.timezone;
    String travelmode=MainActivity.travelmode;
    long jstarttime=MainActivity.jstart_date_millis+MainActivity.jstart_time_millis;

     AlertDialog.Builder bld;

    List<PolylineOptions> polylineOptionsList;
    Map<Integer,mPoint> mpoints;
    Map<Integer,mStep> msteps;
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
    Boolean AlreadyGotError=false;

     weatherUI_utils customLayer;

    static String layerids[];
    public static String[] linelayerids;
    List<String> layeridlist;
   public static Boolean layeridCreated;
    List<Source> markersourcelist;
    private Style style;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, MapboxKey);
        setContentView(R.layout.activity_basic_simple_mapview);
        // Mapbox access token is configured here. This needs to be called either in your application
        // object or in the same activity which contains the mapview.


        // This contains the MapView in XML and needs to be called after the access token is configured.
        markersourcelist=new ArrayList<>();
        layeridCreated = false;
        linelayerids=new String[directionapiresp.routes().size()];
        layeridlist=new ArrayList<>();
        mpoints = new HashMap<>();
        msteps = new HashMap<>();
        polylineOptionsList = new ArrayList<>();
        markersSteps = new ArrayList<>();
        markersInterm = new ArrayList<>();
        polylines = new ArrayList<>();
        AlreadyGotError=false;
        progress=new ProgressDialog(this);
        ////////////////////////////////////////////////////////////
        //menu
        editor = getSharedPreferences("distance", MODE_PRIVATE).edit();
        SharedPreferences prefs = getSharedPreferences("distance", MODE_PRIVATE);
        setIntervalDefaultValOnDisp(prefs.getInt("10", 0));


////////////////////////////////////////////////////////////////////


////////////////////////////////////////////////////////////
        //drag up list adapter
        slidingUpPanelLayout = findViewById(R.id.sliding_layout);
        slidingUpPanelLayout.setPanelHeight(0);


        link = (RecyclerView) findViewById(R.id.dragup_list_recycler);
        link.setLayoutManager(new LinearLayoutManager(this));




        mapView = (MapView) findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);






 ///////////////////////////////////////////////////////////////////////////////////////////////////

                routeadapter = new DragupListAdapter_route(getApplicationContext(), directionapiresp.routes().get(0));
                link.setAdapter(routeadapter);
                if (directionapiresp.routes().get(selectedroute).legs().get(0).distance() != null) {
                    slidingUpPanelLayout.setPanelHeight(getApplicationContext().getResources().getDimensionPixelSize(R.dimen.dragupsize));
                    this.OnDragUpHeadLineChange();
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

        layeridCreated = false;

        linelayerids=new String[directionapiresp.routes().size()];

        setCameraWithCoordinationBounds();

        for(int i=0;i<directionapiresp.routes().size();i++){
            String id="p"+i;
            linelayerids[i]=id;
       //     layeridlist.add(id);
            if(i!=selectedroute)
            customLayer.addPolyline(directionapiresp.routes().get(i).geometry(),id,getResources().getColor(R.color.alternateRoute),selectedroute);
        }
        customLayer.addPolyline(directionapiresp.routes().get(selectedroute).geometry(),"p"+selectedroute,getResources().getColor(R.color.seletedRoute),selectedroute);

//        addMarkers(R.drawable.pina,"img1","sp",sp,"sp","sp");
//        addMarkers(R.drawable.pinb,"img2","dp",dp,"dp","dp");

    }

    MapboxMap.OnMapClickListener mapClickListener= new MapboxMap.OnMapClickListener() {

        @Override
        public boolean onMapClick(@NonNull LatLng point) {
            Log.d("map clicked", "map clicked");
            customLayer.mapOnClick(point,layeridlist,layerids,msteps);
            return false;
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
        customLayer.removeWeatherIcons(layeridlist,markersourcelist);


        getWeatherTask = new Task();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getWeatherTask.execute();
            }
        },500);

    }



    @Override
    public void onError(String etitle, String emsg) {
         progress.dismiss();
        displayError(etitle,emsg);
    }


    @Override
    public void OnWeatherDataListReady(Map<Integer, mStep> msteps) {

        this.msteps=msteps;
        progress.dismiss();
        link.setAdapter(new DragupListAdapter_weather(getApplicationContext(), maptolist(msteps)));
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

    }

    @Override
    public void onWeatherOfPointReady(int id, mPoint mpoint) {
        String pid =id+"";
        layeridlist.add(pid);
        customLayer.addMarkers(new weatherIconMap().getWeatherResource(mpoint.getWeather_data().getIcon()), pid, pid,
                Point.fromLngLat(mpoint.getPoint().longitude(), mpoint.getPoint().latitude()), pid, pid);
    }

    @Override
    public void onWeatherOfStepReady(int step_id, mStep mstep) {

        String id=step_id+"";
        layeridlist.add(id);
        customLayer.addMarkers(new weatherIconMap().getWeatherResource(mstep.getWeatherdata().getIcon()), id, id, mstep.getStep().maneuver().location(), id, id);

    }


    @Override
    public void onWeatherDataListProgressChange(int value) {
          progress.setProgress(value);
    }

    @Override
    public void onMapReady(MapboxMap mapboxMap) {
  // Customize map with markers, polylines, etc.

      SimpleMapViewActivity.mapboxMap = mapboxMap;
      mapboxMap.setStyle(Style.LIGHT,this);
        mapboxMap.addOnMapClickListener(mapClickListener);

    }

    @Override
    public void onStyleLoaded(@NonNull Style style) {
        this.style=style;
        customLayer = new weatherUI_utils(mapboxMap,SimpleMapViewActivity.this);
        customLayer.setDragUpListener(this);
        drawRoute();

    }

    @Override
    public void OnDragUpHeadLineChange() {
        ((TextView)findViewById(R.id.route_name)).setText(directionapiresp.routes().get(selectedroute).legs().get(0).summary());
        ((TextView)findViewById(R.id.duration)).setText(new unitConverter().durationBeautify(directionapiresp.routes().get(selectedroute).duration().longValue()));
        ((TextView)findViewById(R.id.distance)).setText("("+new unitConverter().metertoMiles(directionapiresp.routes().get(selectedroute).distance().longValue())+")");
        if(selectedroute==0)((TextView)findViewById(R.id.fastestroute)).setText(fastest_route);
        else ((TextView)findViewById(R.id.fastestroute)).setText("");
    }


    class Task extends AsyncTask<Object,Object,Object>{


        @Override
        protected Object doInBackground(Object[] objects) {
            WeatherService weatherServiceCall;
            weatherServiceCall = new WeatherService(directionapiresp.routes().get(selectedroute),timezone,interval,jstarttime,travelmode);
            weatherServiceCall.setListener(SimpleMapViewActivity.this);
            weatherServiceCall.calc_data();

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
//        getWeatherTask.cancel(true);
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
        if(progress!=null)
        progress.dismiss();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(progress!=null)
            progress.dismiss();
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
             mapboxMap.clear();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                     //   drawRoute();
                        showWeather(null);
                    }
                },500);


                return true;

            case R.id.action_clr:

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

               dialog.show();
           }
    };

    void setIntervalDefaultValOnDisp(int a){
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
    }
    
}
