package com.mapboxweather.kamleshsahu.mapboxdemo;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.google.gson.Gson;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.api.directions.v5.DirectionsCriteria;
import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.PlaceAutocomplete;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.model.PlaceOptions;
import com.mapboxweather.kamleshsahu.mapboxdemo.Activity.SettingsActivity;
import com.mapboxweather.kamleshsahu.mapboxdemo.ViewModels.MainActivityViewModel;
import com.mapboxweather.kamleshsahu.mapboxdemo.databinding.ActivityMainNewBinding;

import java.util.ArrayList;
import java.util.List;

import io.fabric.sdk.android.Fabric;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static com.mapboxweather.kamleshsahu.mapboxdemo.AskPermission.askPermission;
import static com.mapboxweather.kamleshsahu.mapboxdemo.AskPermission.displayLocationSettingsRequest;
import static com.mapboxweather.kamleshsahu.mapboxdemo.WeatherService_Navigation.Constants.MapboxKey;
import static com.mapboxweather.kamleshsahu.mapboxdemo.WeatherService_Navigation.Constants.REQUEST_CODE_AUTOCOMPLETE1;
import static com.mapboxweather.kamleshsahu.mapboxdemo.WeatherService_Navigation.Constants.REQUEST_CODE_AUTOCOMPLETE2;


public class MainActivity_new extends AppCompatActivity implements PermissionsListener {

     ActivityMainNewBinding activityMainNewBinding;
     MainActivityViewModel mainActivityViewModel;
     String avoid=null;
     String TAG="LocationPermission";
    CheckBox tolls,ferries,highway;
    String travelmode= DirectionsCriteria.PROFILE_DRIVING;
    ImageView car,bike,walk;
    TextView option;
    private PermissionsManager permissionsManager;
    SharedPreferences prefs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());

        setContentView(R.layout.activity_main_new);
        activityMainNewBinding= DataBindingUtil.setContentView(this,R.layout.activity_main_new);
        mainActivityViewModel= ViewModelProviders.of(this).get(MainActivityViewModel.class);
        bindview();

        askPermission(this);


        prefs = getSharedPreferences("formdata", MODE_PRIVATE);

        if(!prefs.getString("start","").isEmpty()){
            mainActivityViewModel.setStart(new Gson().fromJson(prefs.getString("start",""),MLocation.class));
        }
        if(!prefs.getString("dstn","").isEmpty()){
            mainActivityViewModel.setDstn(new Gson().fromJson(prefs.getString("dstn",""),MLocation.class));
        }

        // Check for location permission
        //   location_permission();

        displayLocationSettingsRequest(this);

        mainActivityViewModel.getmTimeMutableLiveData().observe(this, new Observer<MTime>() {
            @Override
            public void onChanged(@Nullable MTime mTime) {
                     activityMainNewBinding.setMtime(mTime);
            }
        });

          mainActivityViewModel.getStartLiveData().observe(this, new Observer<MLocation>() {
              @Override
              public void onChanged(@Nullable MLocation mLocation) {
                  activityMainNewBinding.setStart(mLocation);
              }
          });

          mainActivityViewModel.getDstnLiveData().observe(this, new Observer<MLocation>() {
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

                travelmode= DirectionsCriteria.PROFILE_DRIVING_TRAFFIC;

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

                  travelmode=DirectionsCriteria.PROFILE_CYCLING;

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

                 travelmode=DirectionsCriteria.PROFILE_CYCLING;
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
                if( highway.isChecked()) {
                    avoid = DirectionsCriteria.EXCLUDE_MOTORWAY;
                }else  avoid=null;
                if( tolls.isChecked()) tolls.setChecked(false);
                if( ferries.isChecked()) ferries.setChecked(false);
            }
        });

        tolls.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if( tolls.isChecked()) {
                    avoid = DirectionsCriteria.EXCLUDE_TOLL;
                }else  avoid=null;
                if( highway.isChecked()) highway.setChecked(false);
                if( ferries.isChecked()) ferries.setChecked(false);
            }
        });



        ferries.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if( ferries.isChecked()) {
                    avoid=DirectionsCriteria.EXCLUDE_FERRY;
                }else  avoid=null;
                if( highway.isChecked()) highway.setChecked(false);
                if( tolls.isChecked()) tolls.setChecked(false);
            }
        });


    }


   void bindview(){

       tolls=activityMainNewBinding.included.tolls;
       ferries=activityMainNewBinding.included.ferries;
       highway=activityMainNewBinding.included.highway;
       car=activityMainNewBinding.included.car;
       bike=activityMainNewBinding.included.bike;
       walk=activityMainNewBinding.included.walk;
       option=activityMainNewBinding.included.option;

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
                        mainActivityViewModel.getmTime().setdate(year,monthOfYear,dayOfMonth);
                        mainActivityViewModel.updatemTimeMutableLiveData();
                        timePicker();
                    }
                }, mainActivityViewModel.getmTime().mYear, mainActivityViewModel.getmTime().mMonth, mainActivityViewModel.getmTime().mDay);
        datePickerDialog.show();
    }

    private void timePicker() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        mainActivityViewModel.getmTime().settime(hourOfDay,minute);
                        mainActivityViewModel.updatemTimeMutableLiveData();
                    }
                }, mainActivityViewModel.getmTime().mHour, mainActivityViewModel.getmTime().mMinute, true);
        timePickerDialog.show();
    }


    public void start_onclick(View view){
        PlaceOptions placeOptions=PlaceOptions
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
        Intent intent=new Intent(MainActivity_new.this,NavigationLauncherActivity_Simulate.class);
        Form form=new Form(mainActivityViewModel.getStartLiveData().getValue(),
                mainActivityViewModel.getDstnLiveData().getValue(),
                mainActivityViewModel.getmTimeMutableLiveData().getValue(),avoid,travelmode);
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
            mainActivityViewModel.setStart(new MLocation(feature.placeName(),feature.center()));
            prefs.edit().putString("start", new Gson().toJson(mainActivityViewModel.getStartLiveData().getValue())).apply();


        }else if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_AUTOCOMPLETE2) {
            CarmenFeature feature = PlaceAutocomplete.getPlace(data);
            //System.out.println("feature text :"+feature.text());
            Toast.makeText(this, feature.text(), Toast.LENGTH_LONG).show();
            mainActivityViewModel.setDstn(new MLocation(feature.placeName(),feature.center()));
            prefs.edit().putString("dstn", new Gson().toJson(mainActivityViewModel.getDstnLiveData().getValue())).apply();

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == 0) {
            permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
        } else {
            boolean granted = grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED;
            if (!granted) {
                Toast.makeText(this, "You didn't grant storage permissions.", Toast.LENGTH_LONG).show();
            } else {

            }
        }
    }

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {
        Toast.makeText(this, "This app needs location and storage permissions"
                + "in order to show its functionality.", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPermissionResult(boolean granted) {
        if (granted) {
            requestPermissionIfNotGranted(WRITE_EXTERNAL_STORAGE);
        } else {
            Toast.makeText(this, "You didn't grant location permissions.", Toast.LENGTH_LONG).show();
        }
    }

    public void location_permission(){
        permissionsManager = new PermissionsManager(this);
        if (!PermissionsManager.areLocationPermissionsGranted(this)) {
            permissionsManager.requestLocationPermissions(this);
        } else {
            requestPermissionIfNotGranted(WRITE_EXTERNAL_STORAGE);
        }
    }

    private void requestPermissionIfNotGranted(String permission) {
        List<String> permissionsNeeded = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            permissionsNeeded.add(permission);
            ActivityCompat.requestPermissions(this, permissionsNeeded.toArray(new String[0]), 10);
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
                resetresult();
                findRoute_onClick(null);
                return true;
            case R.id.Subscription:
//                Intent intent=new Intent(getApplicationContext(), Subscription.class);
//                startActivity(intent);
                return true;
            case R.id.action_clr:

                resetresult();
                resetInputs();
                finish();
                startActivity(getIntent());

                return true;
            case R.id.action_main_setting:
                Intent intent1=new Intent(getApplicationContext(), SettingsActivity.class);
                startActivity(intent1);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }



    //Reset Resulr
    static void resetresult(){

    };
//Reset Imputs
    void resetInputs(){

    }
//..................................................................................................

    @Override
    protected void onResume() {
        super.onResume();
    }
}


