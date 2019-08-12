package com.mapboxweather.kamleshsahu.mapboxdemo.Activity;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.google.gson.Gson;
import com.mapbox.api.directions.v5.DirectionsCriteria;
import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.PlaceAutocomplete;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.model.PlaceOptions;
import com.mapbox.mapboxsdk.plugins.places.picker.PlacePicker;
import com.mapbox.mapboxsdk.plugins.places.picker.model.PlacePickerOptions;
import com.mapboxweather.kamleshsahu.mapboxdemo.R;
import com.mapboxweather.kamleshsahu.mapboxdemo.ViewModels.MainActivityViewModel;
import com.mapboxweather.kamleshsahu.mapboxdemo.databinding.ActivityMainNewBinding;
import com.mapboxweather.kamleshsahu.mapboxdemo.models.Form;
import com.mapboxweather.kamleshsahu.mapboxdemo.models.MLocation;
import com.mapboxweather.kamleshsahu.mapboxdemo.models.MTime;

import io.fabric.sdk.android.Fabric;

import static com.mapboxweather.kamleshsahu.mapboxdemo.Methods.AskPermission.askPermission;
import static com.mapboxweather.kamleshsahu.mapboxdemo.Methods.AskPermission.displayLocationSettingsRequest;
import static com.mapboxweather.kamleshsahu.mapboxdemo.WeatherService_Navigation.Constants.MapboxKey;
import static com.mapboxweather.kamleshsahu.mapboxdemo.WeatherService_Navigation.Constants.REQUEST_CODE_AUTOCOMPLETE1;
import static com.mapboxweather.kamleshsahu.mapboxdemo.WeatherService_Navigation.Constants.REQUEST_CODE_AUTOCOMPLETE2;


public class MainActivity_new extends AppCompatActivity {

    ActivityMainNewBinding activityMainNewBinding;
    MainActivityViewModel viewModel;
   
    
    
    CheckBox tolls, ferries, highway;
    ImageView car, bike, walk;
    TextView option;
    
    SharedPreferences prefs;
    ImageView IVcurrentLocaction;
    public static LatLng currentLocation;

    public final int PLACEPICKER_REQUEST_CODE1 = 5201;
    public final int PLACEPICKER_REQUEST_CODE2 = 5202;
    Switch currentLocation_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        Mapbox.getInstance(this, MapboxKey);
        setContentView(R.layout.activity_main_new);
        activityMainNewBinding = DataBindingUtil.setContentView(this, R.layout.activity_main_new);
        viewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);
        bindview();

        askPermission(this);


        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        if (!prefs.getString("start", "").isEmpty()) {
            viewModel.setStart(new Gson().fromJson(prefs.getString("start", ""), MLocation.class));
        }
        if (!prefs.getString("dstn", "").isEmpty()) {
            viewModel.setDstn(new Gson().fromJson(prefs.getString("dstn", ""), MLocation.class));
        }

        // Check for location permission
        //   location_permission();

        displayLocationSettingsRequest(this);

        viewModel.getmTimeMutableLiveData().observe(this, new Observer<MTime>() {
            @Override
            public void onChanged(@Nullable MTime mTime) {
                activityMainNewBinding.setMtime(mTime);
            }
        });

        viewModel.getStartLiveData().observe(this, new Observer<MLocation>() {
            @Override
            public void onChanged(@Nullable MLocation mLocation) {

                activityMainNewBinding.setStart(mLocation);
            }
        });

        viewModel.getDstnLiveData().observe(this, new Observer<MLocation>() {
            @Override
            public void onChanged(@Nullable MLocation mLocation) {
                activityMainNewBinding.setDstn(mLocation);
            }
        });


        car.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  radioGroup.check(R.id._1);
                car.setImageResource(R.drawable.car_on);
                walk.setImageResource(R.drawable.walk_off);
                bike.setImageResource(R.drawable.bike_off);

                highway.setVisibility(View.VISIBLE);
                tolls.setVisibility(View.VISIBLE);
                ferries.setVisibility(View.VISIBLE);

                viewModel.travelmode.setValue(DirectionsCriteria.PROFILE_DRIVING_TRAFFIC);

            }
        });

        walk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                car.setImageResource(R.drawable.car_off);
                walk.setImageResource(R.drawable.walk_on);
                bike.setImageResource(R.drawable.bike_off);

                highway.setVisibility(View.INVISIBLE);
                tolls.setVisibility(View.INVISIBLE);
                ferries.setVisibility(View.INVISIBLE);

                viewModel.travelmode.setValue(DirectionsCriteria.PROFILE_WALKING);

            }
        });
        bike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                car.setImageResource(R.drawable.car_off);
                walk.setImageResource(R.drawable.walk_off);
                bike.setImageResource(R.drawable.bike_on);

                highway.setVisibility(View.INVISIBLE);
                tolls.setVisibility(View.INVISIBLE);
                ferries.setVisibility(View.VISIBLE);

                viewModel.travelmode.setValue(DirectionsCriteria.PROFILE_CYCLING);
               
            }
        });

        option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int a = findViewById(R.id.option_list).getVisibility();
                if (a == 0) {
                    findViewById(R.id.option_list).setVisibility(View.GONE);
                } else {
                    findViewById(R.id.option_list).setVisibility(View.VISIBLE);
                }
            }
        });


        highway.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (highway.isChecked()) {
                    viewModel.avoid.setValue(DirectionsCriteria.EXCLUDE_MOTORWAY);
                } else viewModel.avoid.setValue(null);
                if (tolls.isChecked()) tolls.setChecked(false);
                if (ferries.isChecked()) ferries.setChecked(false);
            }
        });

        tolls.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (tolls.isChecked()) {
                    viewModel.avoid.setValue(DirectionsCriteria.EXCLUDE_TOLL);
                } else viewModel.avoid.setValue(null);
                if (highway.isChecked()) highway.setChecked(false);
                if (ferries.isChecked()) ferries.setChecked(false);
            }
        });


        ferries.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ferries.isChecked()) {
                    viewModel.avoid.setValue(DirectionsCriteria.EXCLUDE_FERRY);
                } else viewModel.avoid.setValue(null);
                if (highway.isChecked()) highway.setChecked(false);
                if (tolls.isChecked()) tolls.setChecked(false);
            }
        });



        currentLocation_view.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked
                        && currentLocation != null) {
                    viewModel.setStart(new MLocation("Your Current Location",
                            Point.fromLngLat(currentLocation.getLongitude(), currentLocation.getLatitude()), true));
                }else if(isChecked && currentLocation==null){
                    currentLocation_view.setChecked(false);
                    Snackbar.make(findViewById(android.R.id.content), "Turn on Location Services to get Current Location", Snackbar.LENGTH_LONG)
                            .show();
                    displayLocationSettingsRequest(MainActivity_new.this);
                }
                else {
                    if(viewModel.getStartLiveData().getValue()!=null &&
                            viewModel.getStartLiveData().getValue().isCurrentLocation)
                    {
                        viewModel.setStart(null);
                    }

                }
            }
        });


        LocationManager mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

//        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000f,
//                10000l, mLocationListener);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100l, 1000.0f, (android.location.LocationListener) mLocationListener);

    }




    private final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(final Location location) {
            //your code here
            currentLocation=new LatLng(location.getLatitude(),location.getLongitude());
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };




    void bindview() {

        tolls = activityMainNewBinding.included.tolls;
        ferries = activityMainNewBinding.included.ferries;
        highway = activityMainNewBinding.included.highway;
        car = activityMainNewBinding.included.car;
        bike = activityMainNewBinding.included.bike;
        walk = activityMainNewBinding.included.walk;
        option = activityMainNewBinding.included.option;
        IVcurrentLocaction=activityMainNewBinding.included.selectFromMap;
        currentLocation_view=activityMainNewBinding.included.currentLocation;


        car.setImageResource(R.drawable.car_on);
        walk.setImageResource(R.drawable.walk_off);
        bike.setImageResource(R.drawable.bike_off);
    }




    public void setTime(View view) {
        datePicker();
    }

    public void datePicker() {
        final DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        viewModel.getmTime().setdate(year, monthOfYear, dayOfMonth);
                        viewModel.updatemTimeMutableLiveData();
                        timePicker();
                    }
                }, viewModel.getmTime().mYear, viewModel.getmTime().mMonth, viewModel.getmTime().mDay);
        datePickerDialog.show();
    }

    private void timePicker() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        viewModel.getmTime().settime(hourOfDay, minute);
                        viewModel.updatemTimeMutableLiveData();
                    }
                }, viewModel.getmTime().mHour, viewModel.getmTime().mMinute, true);
        timePickerDialog.show();
    }


    public void start_onclick(View view) {

        PlaceOptions placeOptions;
        placeOptions= PlaceOptions
                .builder()
                .hint("Start Address...")
                .backgroundColor(getResources().getColor(R.color.loo_pre))
                        .build();


        Intent intent = new PlaceAutocomplete.IntentBuilder()

                .accessToken(MapboxKey)
                .placeOptions(placeOptions)
                .build(this);

        startActivityForResult(intent, REQUEST_CODE_AUTOCOMPLETE1);
    }

    public void destination_onclick(View view) {

        PlaceOptions placeOptions=PlaceOptions
                .builder()
                .hint("Destination Address...")
                .backgroundColor(getResources().getColor(R.color.loo_pre))

                .build();

        Intent intent = new PlaceAutocomplete.IntentBuilder()
                .accessToken(MapboxKey)
                .placeOptions(placeOptions)
                .build(this);

        startActivityForResult(intent, REQUEST_CODE_AUTOCOMPLETE2);

    }

    public void onOptionClick(View view) {
            int option = findViewById(R.id.option_list).getVisibility();
            if (option == 0) {
                findViewById(R.id.option_list).setVisibility(View.GONE);
            } else {
                findViewById(R.id.option_list).setVisibility(View.VISIBLE);
            }
    }

    public void findRoute_onClick(View view){

       if(viewModel.getStartLiveData().getValue()==null){
           Snackbar.make(findViewById(android.R.id.content), "Please Select Start Address...", Snackbar.LENGTH_LONG)
                   .show();
        return;
       }

       if(viewModel.getDstnLiveData().getValue()==null) {
           Snackbar.make(findViewById(android.R.id.content), "Please Select Destination Address...", Snackbar.LENGTH_LONG)
                   .show();
             return;
       }


        Intent intent=new Intent(MainActivity_new.this,NavigationLauncherActivity_Simulate.class);
        Form form=new Form(viewModel.getStartLiveData().getValue(),
                viewModel.getDstnLiveData().getValue(),
                viewModel.getmTimeMutableLiveData().getValue(),
                viewModel.avoid.getValue(),
                viewModel.travelmode.getValue());
        intent.putExtra("form",form);

        startActivity(intent);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_AUTOCOMPLETE1) {
            CarmenFeature feature = PlaceAutocomplete.getPlace(data);
            //System.out.println("feature text :"+feature.text());
            Toast.makeText(this, feature.text(), Toast.LENGTH_LONG).show();
            viewModel.setStart(new MLocation(feature.placeName(),feature.center()));
            prefs.edit().putString("start", new Gson().toJson(viewModel.getStartLiveData().getValue())).apply();
             if(currentLocation_view.isChecked())currentLocation_view.setChecked(false);

        }else if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_AUTOCOMPLETE2) {
            CarmenFeature feature = PlaceAutocomplete.getPlace(data);
            //System.out.println("feature text :"+feature.text());
            Toast.makeText(this, feature.text(), Toast.LENGTH_LONG).show();
            viewModel.setDstn(new MLocation(feature.placeName(),feature.center()));
            prefs.edit().putString("dstn", new Gson().toJson(viewModel.getDstnLiveData().getValue())).apply();


        }else if (requestCode == PLACEPICKER_REQUEST_CODE1 && resultCode == RESULT_OK) {

             CarmenFeature feature = PlaceAutocomplete.getPlace(data);

             Toast.makeText(this, feature.text(), Toast.LENGTH_LONG).show();
             viewModel.setStart(new MLocation( feature.placeName(), feature.center()));
             prefs.edit().putString("start", new Gson().toJson(viewModel.getStartLiveData().getValue())).apply();
            if(currentLocation_view.isChecked())currentLocation_view.setChecked(false);

        }else if (requestCode == PLACEPICKER_REQUEST_CODE2 && resultCode == RESULT_OK) {

            CarmenFeature feature = PlaceAutocomplete.getPlace(data);
            //System.out.println("feature text :"+feature.text());
            Toast.makeText(this, feature.text(), Toast.LENGTH_LONG).show();
            viewModel.setDstn(new MLocation(feature.placeName(),feature.center()));
            prefs.edit().putString("dstn", new Gson().toJson(viewModel.getDstnLiveData().getValue())).apply();


        }
    }




//menu items........................................................................................
@Override
public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.mainactivity_menu, menu);
    return true;
}
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_retry:

                return true;
            case R.id.Subscription:
//                Intent intent=new Intent(getApplicationContext(), Subscription.class);
//                startActivity(intent);
                return true;
            case R.id.action_clr:
                prefs.edit().putString("start", "").apply();
                prefs.edit().putString("dstn", "").apply();

                finish();
                startActivity(getIntent());

                return true;
            case R.id.action_main_setting:
//                Intent intent1=new Intent(getApplicationContext(), SettingsActivity.class);
//                startActivity(intent1);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }



//..................................................................................................

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void onClickCurrentLocation1(View view) {

        if(currentLocation!=null)
        startActivityForResult(
                new PlacePicker.IntentBuilder()
                        .accessToken(MapboxKey)
                        .placeOptions(PlacePickerOptions.builder()
                                .includeDeviceLocationButton(true)

                                .statingCameraPosition(new CameraPosition.Builder()
                                        .target(currentLocation).zoom(16).build())
                                .build())
                        .build(this), PLACEPICKER_REQUEST_CODE1);
         else{
            startActivityForResult(
                    new PlacePicker.IntentBuilder()
                            .accessToken(MapboxKey)
                            .placeOptions(PlacePickerOptions.builder().build())
                            .build(this), PLACEPICKER_REQUEST_CODE1);
        }

    }

    public void onClickCurrentLocation2(View view) {

        if(currentLocation!=null)
            startActivityForResult(
                    new PlacePicker.IntentBuilder()
                            .accessToken(MapboxKey)
                            .placeOptions(PlacePickerOptions.builder()
                                    .includeDeviceLocationButton(true)
                                    .statingCameraPosition(new CameraPosition.Builder()
                                            .target(currentLocation).zoom(16).build())
                                    .build())
                            .build(this), PLACEPICKER_REQUEST_CODE2);
        else{
            startActivityForResult(
                    new PlacePicker.IntentBuilder()
                            .accessToken(MapboxKey)
                            .placeOptions(PlacePickerOptions.builder().build())
                            .build(this), PLACEPICKER_REQUEST_CODE2);
        }

    }


    public void swap_onClick(MenuItem item) {

        MLocation temp=viewModel.getStartLiveData().getValue();
        viewModel.getStartLiveData().setValue(viewModel.getDstnLiveData().getValue());
        viewModel.getDstnLiveData().setValue(temp);
    }
}


