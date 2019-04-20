package com.mapboxweather.kamleshsahu.mapboxdemo.Activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.graphics.RectF;
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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.core.constants.Constants;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.LineString;
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
import com.mapbox.mapboxsdk.style.layers.LineLayer;
import com.mapbox.mapboxsdk.style.layers.Property;
import com.mapbox.mapboxsdk.style.layers.PropertyFactory;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.mapboxsdk.style.sources.Source;
import com.mapboxweather.kamleshsahu.mapboxdemo.Adapter.DragupListAdapter_route;
import com.mapboxweather.kamleshsahu.mapboxdemo.Adapter.DragupListAdapter_weather;

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

    implements WeatherServiceListener
{

    private MapView mapView;
 //   public static Handler myItemhandler,myStephandler;
    Task getWeatherTask;


    Point sp=MainActivity.sp,dp=MainActivity.dp;
    DirectionsResponse directionapiresp=MainActivity.directionapiresp;
    int stepcount=0;
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
//////////////////////////////////////////////////////////////////


        mapView = (MapView) findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(new OnMapReadyCallback() {


            @Override
            public void onMapReady(final MapboxMap mapboxMap) {

                // Customize map with markers, polylines, etc.

                SimpleMapViewActivity.mapboxMap = mapboxMap;
                drawRoute();


                mapboxMap.addOnMapClickListener(mapClickListener);
            }

        });


//        myItemhandler = new Handler(myIntermediatePointsCallback);
//
//        myStephandler = new Handler(myStepsHandlerCallback);




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

        layeridCreated = false;

        linelayerids=new String[directionapiresp.routes().size()];

        setCameraWithCoordinationBounds();

        for(int i=0;i<directionapiresp.routes().size();i++){
            String id="p"+i;
            linelayerids[i]=id;
       //     layeridlist.add(id);
            if(i!=selectedroute)
            addPolyline(directionapiresp.routes().get(i).geometry(),id,getResources().getColor(R.color.alternateRoute));
        }
        addPolyline(directionapiresp.routes().get(selectedroute).geometry(),"p"+selectedroute,getResources().getColor(R.color.seletedRoute));

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


                if(feature.id().startsWith("p")){
                    routechangeListener(pointf);
                }
                else if (Integer.parseInt(feature.id())%1000==0) {
                    Log.i("marker clicked :", "step marker clicked");
                    int index = (Integer.parseInt(feature.id())/1000)*1000;
                    new CustomDialogClass(SimpleMapViewActivity.this, msteps.get(index)).show();
                } else if(Integer.parseInt(feature.id())%1000!=0){
                    Log.i("marker clicked :", "item marker clicked");
                    int step_id = (Integer.parseInt(feature.id())/1000)*1000;
                    int index= (Integer.parseInt(feature.id()));
                    new CustomDialogClass(SimpleMapViewActivity.this, msteps.get(step_id).getInterms().get(index)).show();
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


        getWeatherTask = new Task();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getWeatherTask.execute();
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

    @Override
    public void onError(String etitle, String emsg) {
         displayError(etitle,emsg);
    }

    @Override
    public void OnWeatherDataListReady(Map<Integer, mStep> msteps) {

        this.msteps=msteps;
        progress.dismiss();
        link.setAdapter(new DragupListAdapter_weather(getApplicationContext(), maptolist(msteps)));
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        for(Map.Entry<Integer,mStep> mstep_:msteps.entrySet()){
            mStep mstep=mstep_.getValue();
            MarkerOptions options = new MarkerOptions();
            options.setPosition(new LatLng(mstep.getStep().maneuver().location().latitude(), mstep.getStep().maneuver().location().longitude()));


            String id = mstep_.getKey()+"";
            layeridlist.add(id);
            addMarkers(new weatherIconMap().getWeatherResource(mstep.getWeatherdata().getIcon()), id, id, mstep.getStep().maneuver().location(), id, id);

            Map<Integer,mPoint> step_mpoints=mstep.getInterms();


            if(step_mpoints!=null && step_mpoints.size()>0) {
                mpoints.putAll(step_mpoints);

                for(Map.Entry<Integer,mPoint> mpoint_:step_mpoints.entrySet()){

                    mPoint mpoint=mpoint_.getValue();

                    if (mpoint != null) {
                        String pid = mpoint_.getKey()+"";
                        layeridlist.add(pid);
                        addMarkers(new weatherIconMap().getWeatherResource(mpoint.getWeather_data().getIcon()), pid, pid,
                                Point.fromLngLat(mpoint.getPoint().longitude(), mpoint.getPoint().latitude()), pid, pid);

                    }
                }
            }

        }
    }

    @Override
    public void onWeatherDataListProgressChange(int value) {
          progress.setProgress(value);
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

//        @Override
//        protected void onPostExecute(List<mStep> steps) {
//
//
//        }
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
               // weatherApi = new WeatherApi();
               // weatherApi.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                //               Toast.makeText(this, "Fetching Weather...", Toast.LENGTH_SHORT).show();
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
