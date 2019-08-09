package com.mapboxweather.kamleshsahu.mapboxdemo;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.location.Location;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.mapbox.android.core.location.LocationEngine;
import com.mapbox.android.core.location.LocationEngineCallback;
import com.mapbox.android.core.location.LocationEngineProvider;
import com.mapbox.android.core.location.LocationEngineRequest;
import com.mapbox.android.core.location.LocationEngineResult;
import com.mapbox.api.directions.v5.DirectionsCriteria;
import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.core.constants.Constants;
import com.mapbox.core.utils.TextUtils;
import com.mapbox.geojson.LineString;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.exceptions.InvalidLatLngBoundsException;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.geometry.LatLngBounds;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.style.sources.Source;
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncherOptions;
import com.mapbox.services.android.navigation.v5.navigation.NavigationRoute;
import com.mapbox.services.android.navigation.v5.utils.LocaleUtils;
import com.mapboxweather.kamleshsahu.mapboxdemo.Adapter.RouteListAdapter_new;
import com.mapboxweather.kamleshsahu.mapboxdemo.WeatherService_Navigation.Interface.WeatherServiceListener;
import com.mapboxweather.kamleshsahu.mapboxdemo.WeatherService_Navigation.Models.mPoint;
import com.mapboxweather.kamleshsahu.mapboxdemo.WeatherService_Navigation.Models.mStep;
import com.mapboxweather.kamleshsahu.mapboxdemo.WeatherService_Navigation.UIutils.weatherIconMap;
import com.mapboxweather.kamleshsahu.mapboxdemo.WeatherService_Navigation.UIutils.weatherUI_utils;
import com.mapboxweather.kamleshsahu.mapboxdemo.WeatherService_Navigation.WeatherService;
import com.mapboxweather.kamleshsahu.mapboxdemo.databinding.ActivityNavigationLauncherBinding;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

import static android.os.Environment.getExternalStoragePublicDirectory;
import static com.mapboxweather.kamleshsahu.mapboxdemo.WeatherService_Navigation.Constants.MapboxKey;


public class NavigationLauncherActivity_Simulate extends AppCompatActivity
        implements OnMapReadyCallback,
  MapboxMap.OnMapLongClickListener,
        WeatherServiceListener, MapboxMap.OnMapClickListener, selectedRouteChangedListener, routeChangedinList {
     Form form;

     int interval=5000;
    public static ProgressDialog progress;
    Boolean AlreadyGotError=false;

    weatherUI_utils customLayer;

    static String layerids[];
//    public static String[] linelayerids;
    List<String> layeridlist;
    public static Boolean layeridCreated;
    List<Source> markersourcelist;
    private Style style;

    AlertDialog.Builder bld;
    Map<Integer, mStep> msteps;

    int totalsteps=0;

   private int selectedroute=0;

  private static final int CAMERA_ANIMATION_DURATION = 1000;
  private static final int DEFAULT_CAMERA_ZOOM = 16;
  private static final int CHANGE_SETTING_REQUEST_CODE = 1;
  private static final int INITIAL_ZOOM = 16;
  private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 1000;
  private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = 500;

  private final NavigationLauncherLocationCallback callback = new NavigationLauncherLocationCallback(this);
  private LocationEngine locationEngine;
//  private NavigationMapRoute mapRoute;
    private MPolyline myPolyline;
  private MapboxMap mapboxMap;
  private Marker currentMarker;
  private Point currentLocation;
  private Point destination;
 // private DirectionsRoute route;
  private LocaleUtils localeUtils;
  private boolean locationFound;

//  @BindView(R.id.mapView)
  MapView mapView;
//  @BindView(R.id.launch_route_btn)
  Button launchRouteBtn;
//  @BindView(R.id.loading)
  ProgressBar loading;
//  @BindView(R.id.launch_btn_frame)
  FrameLayout launchBtnFrame;

  ActivityNavigationLauncherBinding activityNavigationLauncherBinding;

    RecyclerView recyclerView;
    DirectionsResponse directionsResponse;
  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Mapbox.getInstance(this,MapboxKey);
    setContentView(R.layout.activity_navigation_launcher);
//    ButterKnife.bind(this);



      activityNavigationLauncherBinding= DataBindingUtil.setContentView(this,R.layout.activity_navigation_launcher);


      mapView = activityNavigationLauncherBinding.mapView;
      loading=activityNavigationLauncherBinding.loading;
      launchBtnFrame=activityNavigationLauncherBinding.launchBtnFrame;
      launchRouteBtn=activityNavigationLauncherBinding.launchRouteBtn;

    mapView.onCreate(savedInstanceState);
    mapView.getMapAsync(this);
    localeUtils = new LocaleUtils();

      markersourcelist=new ArrayList<>();
      layeridCreated = false;
//      linelayerids=new String[directionsResponse.routes().size()];
      layeridlist=new ArrayList<>();
 //     mpoints = new HashMap<>();
      msteps = new HashMap<>();
//      polylineOptionsList = new ArrayList<>();
 //     markersSteps = new ArrayList<>();
 //     markersInterm = new ArrayList<>();
 //     polylines = new ArrayList<>();
      AlreadyGotError=false;
      progress=new ProgressDialog(this);
    if(getIntent().getParcelableExtra("form")!=null){

      form=getIntent().getParcelableExtra("form");

    }

  }



  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.navigation_view_activity_menu, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
//      case R.id.settings:
//        showSettings();
//        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
//    if (requestCode == CHANGE_SETTING_REQUEST_CODE && resultCode == RESULT_OK) {
//      boolean shouldRefetch = data.getBooleanExtra(NavigationSettingsActivity.UNIT_TYPE_CHANGED, false)
//        || data.getBooleanExtra(NavigationSettingsActivity.LANGUAGE_CHANGED, false);
//      if (destination != null && shouldRefetch) {
//        fetchRoute();
//      }
//    }
  }

  @Override
  protected void onStart() {
    super.onStart();
    mapView.onStart();

  }

  @SuppressWarnings({"MissingPermission"})
  @Override
  public void onResume() {
    super.onResume();
    mapView.onResume();
    if (locationEngine != null) {
      locationEngine.requestLocationUpdates(buildEngineRequest(), callback, null);
    }
  }

  @Override
  public void onPause() {
    super.onPause();
    mapView.onPause();
    if (locationEngine != null) {
      locationEngine.removeLocationUpdates(callback);
    }
  }

  @Override
  public void onLowMemory() {
    super.onLowMemory();
    mapView.onLowMemory();
  }

  @Override
  protected void onStop() {
    super.onStop();
    mapView.onStop();
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

 // @OnClick(R.id.launch_route_btn)
  public void onRouteLaunchClick(View view) {
    launchNavigationWithRoute();
  }

  @Override
  public void onMapReady(MapboxMap mapboxMap) {
    this.mapboxMap = mapboxMap;


    mapboxMap.setStyle(Style.MAPBOX_STREETS, style -> {
      mapboxMap.addOnMapLongClickListener(this);
      mapboxMap.addOnMapClickListener(this);
        this.style=style;
      initializeLocationEngine();
      initializeLocationComponent(style);
      initializeMapRoute();
        customLayer = new weatherUI_utils(mapboxMap, this);
         if(form!=null) {
            fetchRoute();
         }

    });
  }

    @Override
    public boolean onMapClick(@NonNull LatLng point) {
        Log.d("map clicked", "map clicked");
        customLayer.mapOnClick(point,layeridlist.toArray(new String[layeridlist.size()]),msteps);
        myPolyline.mapOnClick(point);
        return false;
    }

  @Override
  public boolean onMapLongClick(@NonNull LatLng point) {
    destination = Point.fromLngLat(point.getLongitude(), point.getLatitude());
    launchRouteBtn.setEnabled(false);
    loading.setVisibility(View.VISIBLE);
    setCurrentMarkerPosition(point);
    if (currentLocation != null) {
      fetchRoute();
    }
    return false;
  }



  void updateCurrentLocation(Point currentLocation) {
    this.currentLocation = currentLocation;
  }

  void onLocationFound(Location location) {
    if (!locationFound) {
      animateCamera(new LatLng(location.getLatitude(), location.getLongitude()));
      Snackbar.make(mapView, R.string.explanation_long_press_waypoint, Snackbar.LENGTH_LONG).show();
      locationFound = true;
      hideLoading();
    }
  }

//  private void showSettings() {
//    startActivityForResult(new Intent(this, NavigationSettingsActivity.class), CHANGE_SETTING_REQUEST_CODE);
//  }

  @SuppressWarnings( {"MissingPermission"})
  private void initializeLocationEngine() {
    locationEngine = LocationEngineProvider.getBestLocationEngine(getApplicationContext());
    LocationEngineRequest request = buildEngineRequest();
    locationEngine.requestLocationUpdates(request, callback, null);
    locationEngine.getLastLocation(callback);
  }

  @SuppressWarnings( {"MissingPermission"})
  private void initializeLocationComponent(Style style) {
    LocationComponent locationComponent = mapboxMap.getLocationComponent();
    locationComponent.activateLocationComponent(this, style, locationEngine);
    locationComponent.setLocationComponentEnabled(true);
    locationComponent.setRenderMode(RenderMode.COMPASS);
  }

  private void initializeMapRoute() {
//    mapRoute = new NavigationMapRoute(mapView, mapboxMap);
//    mapRoute.setOnRouteSelectionChangeListener(this);

  }

  private void fetchRoute() {

    NavigationRoute.Builder builder = NavigationRoute.builder(this)
      .accessToken(Mapbox.getAccessToken())
      //.origin(currentLocation)
      .origin(form.start.getS_point())
      .destination(form.dstn.getS_point())
      .profile(getRouteProfileFromSharedPreferences())
      .alternatives(true);
    setFieldsFromSharedPreferences(builder);
    builder.build().getRoute(new Callback<DirectionsResponse>() {
      @Override
      public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
        if (validRouteResponse(response)) {
          hideLoading();

            directionsResponse = response.body();
            showOnRecyclerView();

          if (directionsResponse.routes().size()>0 && directionsResponse.routes().get(0).distance() > 25d) {
            launchRouteBtn.setEnabled(true);
          //  mapRoute.addRoutes(response.body().routes());
              myPolyline=new MPolyline(getApplicationContext(),mapboxMap,style,response.body(),0);
              myPolyline.setListener(NavigationLauncherActivity_Simulate.this);
            boundCameraToRoute();
          } else {
            Snackbar.make(mapView, R.string.error_select_longer_route, Snackbar.LENGTH_SHORT).show();
          }
        }
      }

      @Override
      public void onFailure(Call<DirectionsResponse> call, Throwable t) {

      }
    });
    loading.setVisibility(View.VISIBLE);
  }

  private void setFieldsFromSharedPreferences(NavigationRoute.Builder builder) {
    builder
      .language(getLanguageFromSharedPreferences())
      .voiceUnits(getUnitTypeFromSharedPreferences());
  }

  private String getUnitTypeFromSharedPreferences() {
    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
    String defaultUnitType = getString(R.string.default_unit_type);
    String unitType = sharedPreferences.getString(getString(R.string.unit_type_key), defaultUnitType);
    if (unitType.equals(defaultUnitType)) {
      unitType = localeUtils.getUnitTypeForDeviceLocale(this);
    }

    return unitType;
  }

  private Locale getLanguageFromSharedPreferences() {
    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
    String defaultLanguage = getString(R.string.default_locale);
    String language = sharedPreferences.getString(getString(R.string.language_key), defaultLanguage);
    if (language.equals(defaultLanguage)) {
      return localeUtils.inferDeviceLocale(this);
    } else {
      return new Locale(language);
    }
  }

  private boolean getShouldSimulateRouteFromSharedPreferences() {
    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
    return sharedPreferences.getBoolean(getString(R.string.simulate_route_key), false);
  }

  private String getRouteProfileFromSharedPreferences() {
    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
    return sharedPreferences.getString(
      getString(R.string.route_profile_key), DirectionsCriteria.PROFILE_DRIVING_TRAFFIC
    );
  }

  private String obtainOfflinePath() {
    File offline = getExternalStoragePublicDirectory("Offline");
    return offline.getAbsolutePath();
  }

  private String retrieveOfflineVersionFromPreferences() {
    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
    return sharedPreferences.getString(getString(R.string.offline_version_key), "");
  }

  private void launchNavigationWithRoute() {
    if (directionsResponse== null || directionsResponse.routes().size()<1) {
      Snackbar.make(mapView, R.string.error_route_not_available, Snackbar.LENGTH_SHORT).show();
      return;
    }

    NavigationLauncherOptions.Builder optionsBuilder = NavigationLauncherOptions.builder()
    //  .shouldSimulateRoute(getShouldSimulateRouteFromSharedPreferences());

            .shouldSimulateRoute(false);
    CameraPosition initialPosition = new CameraPosition.Builder()
      .target(new LatLng(currentLocation.latitude(), currentLocation.longitude()))
      .zoom(INITIAL_ZOOM)
      .build();
    optionsBuilder.initialMapCameraPosition(initialPosition);
    optionsBuilder.directionsRoute(directionsResponse.routes().get(selectedroute));
    String offlinePath = obtainOfflinePath();
    if (!TextUtils.isEmpty(offlinePath)) {
      optionsBuilder.offlineRoutingTilesPath(offlinePath);
    }
    String offlineVersion = retrieveOfflineVersionFromPreferences();
    if (!offlineVersion.isEmpty()) {
      optionsBuilder.offlineRoutingTilesVersion(offlineVersion);
    }
    NavigationLauncher.startNavigation(this, optionsBuilder.build());
  }

  private boolean validRouteResponse(Response<DirectionsResponse> response) {
    return response.body() != null && !response.body().routes().isEmpty();
  }

  private void hideLoading() {
    if (loading.getVisibility() == View.VISIBLE) {
      loading.setVisibility(View.INVISIBLE);
    }
  }

  public void boundCameraToRoute() {
    if (directionsResponse.routes().get(selectedroute) != null) {
      List<Point> routeCoords = LineString.fromPolyline(directionsResponse.routes().get(selectedroute).geometry(),
        Constants.PRECISION_6).coordinates();
      List<LatLng> bboxPoints = new ArrayList<>();
      for (Point point : routeCoords) {
        bboxPoints.add(new LatLng(point.latitude(), point.longitude()));
      }
      if (bboxPoints.size() > 1) {
        try {
          LatLngBounds bounds = new LatLngBounds.Builder().includes(bboxPoints).build();
          // left, top, right, bottom
          int topPadding = launchBtnFrame.getHeight() * 2;
          animateCameraBbox(bounds, CAMERA_ANIMATION_DURATION, new int[] {50, topPadding, 50, 100});
        } catch (InvalidLatLngBoundsException exception) {
          Toast.makeText(this, R.string.error_valid_route_not_found, Toast.LENGTH_SHORT).show();
        }
      }
    }
  }

  private void animateCameraBbox(LatLngBounds bounds, int animationTime, int[] padding) {
    CameraPosition position = mapboxMap.getCameraForLatLngBounds(bounds, padding);
    mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(position), animationTime);


  }

  private void animateCamera(LatLng point) {
    mapboxMap.animateCamera(CameraUpdateFactory.newLatLngZoom(point, DEFAULT_CAMERA_ZOOM), CAMERA_ANIMATION_DURATION);
  }

  private void setCurrentMarkerPosition(LatLng position) {
    if (position != null) {
      if (currentMarker == null) {
        MarkerOptions markerViewOptions = new MarkerOptions()
          .position(position);
        currentMarker = mapboxMap.addMarker(markerViewOptions);
      } else {
        currentMarker.setPosition(position);
      }
    }
  }

  @NonNull
  private LocationEngineRequest buildEngineRequest() {
    return new LocationEngineRequest.Builder(UPDATE_INTERVAL_IN_MILLISECONDS)
      .setPriority(LocationEngineRequest.PRIORITY_HIGH_ACCURACY)
      .setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS)
      .build();
  }



    @Override
    public void onSelectedRouteChanged(int id) {
        selectedroute=id;
    }

    @Override
    public void selectedRouteChangedInList(int id) {
        if(myPolyline!=null){
            myPolyline.updateRoutesinMap(id);
        }
    }

    private static class NavigationLauncherLocationCallback implements LocationEngineCallback<LocationEngineResult> {

    private final WeakReference<NavigationLauncherActivity_Simulate> activityWeakReference;

    NavigationLauncherLocationCallback(NavigationLauncherActivity_Simulate activity) {
      this.activityWeakReference = new WeakReference<>(activity);
    }

    @Override
    public void onSuccess(LocationEngineResult result) {
      NavigationLauncherActivity_Simulate activity = activityWeakReference.get();
      if (activity != null) {
        Location location = result.getLastLocation();
        if (location == null) {
          return;
        }
        activity.updateCurrentLocation(Point.fromLngLat(location.getLongitude(), location.getLatitude()));
        activity.onLocationFound(location);

      }
    }

    @Override
    public void onFailure(@NonNull Exception exception) {
      Timber.e(exception);
    }
  }


    private void showOnRecyclerView() {


        recyclerView =activityNavigationLauncherBinding.rv;
        RouteListAdapter_new adapter = new RouteListAdapter_new(this,directionsResponse);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter.setListener(this);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        adapter.notifyDataSetChanged();

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

        totalsteps=directionsResponse.routes().get(selectedroute).legs().get(0).steps().size();
        customLayer.removeWeatherIcons(layeridlist,markersourcelist);

        WeatherService weatherServiceCall;
        weatherServiceCall = new WeatherService(directionsResponse.routes().get(selectedroute),form.mTime.timezone,interval,form.mTime.gettime_millis(),form.travelmode);
        weatherServiceCall.setListener(this);
        weatherServiceCall.execute();

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

   //     link.setAdapter(new DragupListAdapter_weather(getApplicationContext(), maptolist(msteps)));
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

    void displayError(String title, String msg){
        if(!AlreadyGotError) {
            AlreadyGotError=true;
            int maxLength = (msg.length() < 40) ? msg.length() : 40;
            msg = msg.substring(0, maxLength);
            bld = new AlertDialog.Builder(this);
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

}
