package com.mapboxweather.kamleshsahu.mapboxdemo.Activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.crashlytics.android.Crashlytics;
import com.mapbox.android.core.location.LocationEngine;
import com.mapbox.api.directions.v5.DirectionsCriteria;
import com.mapbox.api.directions.v5.models.DirectionsRoute;
import com.mapbox.api.directions.v5.models.LegStep;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.services.android.navigation.ui.v5.NavigationViewOptions;
import com.mapbox.services.android.navigation.ui.v5.OnNavigationReadyCallback;
import com.mapbox.services.android.navigation.ui.v5.listeners.NavigationListener;
import com.mapbox.services.android.navigation.v5.location.replay.ReplayRouteLocationEngine;
import com.mapbox.services.android.navigation.v5.milestone.Milestone;
import com.mapbox.services.android.navigation.v5.milestone.MilestoneEventListener;
import com.mapbox.services.android.navigation.v5.navigation.MapboxNavigationOptions;
import com.mapbox.services.android.navigation.v5.navigation.NavigationConstants;
import com.mapbox.services.android.navigation.v5.routeprogress.ProgressChangeListener;
import com.mapbox.services.android.navigation.v5.routeprogress.RouteProgress;
import com.mapboxweather.kamleshsahu.mapboxdemo.R;
import com.mapboxweather.kamleshsahu.mapboxdemo.Views.myNavigationView;
import com.mapboxweather.kamleshsahu.mapboxdemo.WeatherService_Navigation.Interface.NextMilestoneSetter;
import com.mapboxweather.kamleshsahu.mapboxdemo.WeatherService_Navigation.Interface.WeatherServiceListener;
import com.mapboxweather.kamleshsahu.mapboxdemo.WeatherService_Navigation.Models.StepCorrection;
import com.mapboxweather.kamleshsahu.mapboxdemo.WeatherService_Navigation.Models.mPoint;
import com.mapboxweather.kamleshsahu.mapboxdemo.WeatherService_Navigation.Models.mStep;
import com.mapboxweather.kamleshsahu.mapboxdemo.WeatherService_Navigation.UIutils.weatherIconMap;
import com.mapboxweather.kamleshsahu.mapboxdemo.WeatherService_Navigation.UIutils.weatherUI_utils;
import com.mapboxweather.kamleshsahu.mapboxdemo.WeatherService_Navigation.WeatherUpdateService;
import com.mapboxweather.kamleshsahu.mapboxdemo.models.NavigationLauncher;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import io.fabric.sdk.android.Fabric;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.mapboxweather.kamleshsahu.mapboxdemo.Methods.DisplayError.displayError;
import static com.mapboxweather.kamleshsahu.mapboxdemo.WeatherService_Navigation.Methods.myUtils.getCorrection;
import static com.mapboxweather.kamleshsahu.mapboxdemo.WeatherService_Navigation.Methods.myUtils.mycustomMilestone;




/**
 * Serves as a launching point for the custom drop-in UI, {@link NavigationView}.
 * <p>
 * Demonstrates the proper setup and usage of the view, including all lifecycle methods.
 */
public class MapboxNavigationActivity extends AppCompatActivity
        implements
        OnNavigationReadyCallback,
        MilestoneEventListener,
        NavigationListener,
        ProgressChangeListener,
        NextMilestoneSetter,
        WeatherServiceListener

{

  private myNavigationView navigationView;

  int nextMilestone;

  public static weatherUI_utils weatherUtils;

  String timezone;
  String travelmode;
  long jstarttime;
  long interval=50000;

  private FloatingActionButton weatherRefreshButton;

  public static List<String> layeridlist;

  public static  Map<Integer, mStep> msteps;

  WeatherUpdateService weatherUpdateService;

  DirectionsRoute directionsRoute;

  public MapboxNavigationActivity() {
    nextMilestone = 0;
  }
  public static Activity activity;
  ProgressBar weather_progressbar;

//  SharedPreferences sp;
//  public static String MAP_THEME;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    weatherUtils=new weatherUI_utils();
    activity=this;
    setTheme(com.mapbox.services.android.navigation.ui.v5.R.style.Theme_AppCompat_NoActionBar);

    super.onCreate(savedInstanceState);
    Fabric.with(this, new Crashlytics());


    setContentView(R.layout.activity_navigation);

    navigationView = findViewById(R.id.navigationView);



 
    weather_progressbar=findViewById(R.id.weather_progressbar);



  //  navigationView.setListener(this);


    navigationView.onCreate(savedInstanceState);
 //   navigationView.setActivity(this);
    initialize();


    initialiseWeatherVariables();

    weatherRefreshButton=findViewById(R.id.weatherRefreshButton);
    weatherRefreshButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        updateNextMilestone(0);
      }
    });


  }



  void initialiseWeatherVariables(){
    timezone= Calendar.getInstance().getTimeZone().getID();
    travelmode= DirectionsCriteria.PROFILE_DRIVING_TRAFFIC;
    interval = PreferenceManager.getDefaultSharedPreferences(this).getInt("10",50)*1000;
    layeridlist=new ArrayList<>();
  }


  @Override
  public void onStart() {
    super.onStart();
    navigationView.onStart();
  }

  @Override
  public void onResume() {
    super.onResume();
    navigationView.onResume();
  }

  @Override
  public void onLowMemory() {
    super.onLowMemory();
    navigationView.onLowMemory();
  }

  @Override
  public void onBackPressed() {
    // If the navigation view didn't need to do anything, call super
    if (!navigationView.onBackPressed()) {
      super.onBackPressed();
    }
  }

  @Override
  protected void onSaveInstanceState(Bundle outState) {
    navigationView.onSaveInstanceState(outState);
    super.onSaveInstanceState(outState);
  }

  @Override
  protected void onRestoreInstanceState(Bundle savedInstanceState) {
    super.onRestoreInstanceState(savedInstanceState);
    navigationView.onRestoreInstanceState(savedInstanceState);
  }

  @Override
  public void onPause() {
    super.onPause();
    navigationView.onPause();
  }

  @Override
  public void onStop() {
    super.onStop();
    navigationView.onStop();
  }

  @Override
  protected void onDestroy() {
    if(weatherUpdateService!=null)
    weatherUpdateService.unsubscribe();
    super.onDestroy();
    navigationView.onDestroy();
  }



  @Override
  public void onNavigationReady(boolean isRunning) {
    NavigationViewOptions.Builder options = NavigationViewOptions.builder();
    options.navigationListener(this);
    extractRoute(options);
    options.progressChangeListener(this);
    extractConfiguration(options);
    options.milestones(mycustomMilestone());
    options.milestoneEventListener(this);
    options.navigationOptions(MapboxNavigationOptions.builder().build());
    navigationView.startNavigation(options.build());

//    weatherUtils.setActivity(this);

  }



  @Override
  public void onCancelNavigation() {
    finishNavigation();
  }

  @Override
  public void onNavigationFinished() {
    finishNavigation();
  }

  @Override
  public void onNavigationRunning() {
    // Intentionally empty
  }

  private void initialize() {
    Parcelable position = getIntent().getParcelableExtra(NavigationConstants.NAVIGATION_VIEW_INITIAL_MAP_POSITION);
    if (position != null) {
      navigationView.initialize(this, (CameraPosition) position);
    } else {
      navigationView.initialize(this);
    }
  }

  List<LegStep> steps;
  private void extractRoute(NavigationViewOptions.Builder options) {
    DirectionsRoute route = NavigationLauncher.extractRoute(this);
    steps = route.legs().get(0).steps();
    options.directionsRoute(route);
    this.directionsRoute=route;
    travelmode=route.routeOptions().profile();
  }

  private void extractConfiguration(NavigationViewOptions.Builder options) {
   if(PreferenceManager.getDefaultSharedPreferences(this)
           .getBoolean("simulate", false)){
     LocationEngine engine=new ReplayRouteLocationEngine();
     ((ReplayRouteLocationEngine) engine).updateSpeed(100);
     ((ReplayRouteLocationEngine) engine).assign(directionsRoute);
     options.locationEngine(engine);
   }

    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
  //  options.shouldSimulateRoute(preferences.getBoolean(NavigationConstants.NAVIGATION_VIEW_SIMULATE_ROUTE, false));
    options.shouldSimulateRoute(false);
    String offlinePath = preferences.getString(NavigationConstants.OFFLINE_PATH_KEY, "");
    if (!offlinePath.isEmpty()) {
      options.offlineRoutingTilesPath(offlinePath);
    }
    String offlineVersion = preferences.getString(NavigationConstants.OFFLINE_VERSION_KEY, "");
    if (!offlineVersion.isEmpty()) {
      options.offlineRoutingTilesVersion(offlineVersion);
    }
  }

  private void finishNavigation() {
    NavigationLauncher.cleanUpPreferences(this);
    finish();
  }


  @Override
  public void onMilestoneEvent(RouteProgress routeProgress, String instruction, Milestone milestone) {

// &&
    if(milestone.getIdentifier()==1000){
      Log.d("new step :",""+routeProgress.currentLegProgress().stepIndex());
      if(routeProgress.currentLegProgress().currentStep().distance()<200) {
        setnextMilestone(200);
      }else{
        setnextMilestone(0);
      }
//     if(routeProgress.currentLegProgress().currentStep().distance()>1000)
//     updateWeather(routeProgress.directionsRoute(),
//             routeProgress.currentLegProgress().stepIndex(),
//             null);
    }

  }



  void setnextMilestone(int val){
    synchronized(this){
      this.nextMilestone = val;
    }
  }
   int getMilestone(){
    return nextMilestone;
  }

  @Override
  public void onProgressChange(Location location, RouteProgress routeProgress) {

    Log.d("dist travelled :",String.valueOf(routeProgress.currentLegProgress().currentStepProgress().distanceTraveled()));

    if(routeProgress.currentLegProgress().currentStepProgress().distanceTraveled()>=getMilestone()){
      setnextMilestone((int)routeProgress.currentLegProgress().currentStepProgress().distanceTraveled()+5000);

      updateWeather(routeProgress.directionsRoute()
              ,routeProgress.currentLegProgress().stepIndex(),
              getCorrection(location,routeProgress));
     }
    }

  public void updateWeather(DirectionsRoute route, int currStep, StepCorrection correction){
    disablerefresh();
    if(weatherUpdateService!=null)
      Log.d("asynctask status:",weatherUpdateService.getStatus().name());

    if(weatherUpdateService==null || weatherUpdateService.getStatus()!= AsyncTask.Status.RUNNING || weatherUpdateService.getStatus()!=AsyncTask.Status.PENDING) {

      Log.d("updating weather :",currStep+"");
      if (layeridlist.size() > 0) {
        weatherUtils.removeWeatherIcons(layeridlist);
      }

      msteps = null;
      jstarttime = Calendar.getInstance().getTimeInMillis();
      weatherUpdateService = new WeatherUpdateService(route, timezone, interval, jstarttime, travelmode, currStep,correction);
      weatherUpdateService.subscribe(this);
      weatherUpdateService.execute();
    }else{
      System.out.println("weather update is already running/pending or weatherobj!=null");
      enablerefresh();
    }
  }
  @Override
  public void updateNextMilestone(int val) {
    setnextMilestone(val);
  }


  @Override
  public void OnWeatherDataListReady(Map<Integer, mStep> msteps) {
          this.msteps=msteps;
          enablerefresh();
  }

  @Override
  public void onWeatherOfPointReady(int id, mPoint mpoint) {
    System.out.printf("\nReceived Weather of Point : %d:%s",id,mpoint);
    String pid =id+"";
    layeridlist.add(pid);

    weatherUtils.addMarkers(new weatherIconMap().getWeatherResource(mpoint.getWeather_data().getIcon()), pid, pid,
            Point.fromLngLat(mpoint.getPoint().longitude(), mpoint.getPoint().latitude()), pid, pid);
  }

  @Override
  public void onWeatherOfStepReady(int step_id, mStep mstep) {
    System.out.printf("\nReceived Weather of Step : %d:%s",step_id,mstep);
    String id=step_id+"";
    layeridlist.add(id);
    weatherUtils.addMarkers(new weatherIconMap().getWeatherResource(mstep.getWeatherdata().getIcon()), id, id, mstep.getStep_StartPoint(), id, id);
  }

  @Override
  public void onWeatherDataListProgressChange(int progress) {

  }

  @Override
  public void onError(String etitle, String emsg) {
  //  System.out.printf("%s :%s",etitle,emsg);
    if(weather_progressbar!=null)
     enablerefresh();
    displayError(this,etitle,emsg);

  }



  void enablerefresh(){
    new Handler().postDelayed(new Runnable() {
      @Override
      public void run() {
        weatherRefreshButton.setEnabled(true);
        weather_progressbar.setVisibility(GONE);
        weatherRefreshButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(com.mapbox.services.android.navigation.ui.v5.R.color.mapbox_navigation_view_color_banner_background)));
      }
    }, 1000);


  }


  void disablerefresh(){
    weather_progressbar.setVisibility(VISIBLE);
    weatherRefreshButton.setEnabled(false);
    weatherRefreshButton.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#BDBDBD")));
  }



}
