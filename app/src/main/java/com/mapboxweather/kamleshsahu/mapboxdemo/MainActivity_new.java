package com.mapboxweather.kamleshsahu.mapboxdemo;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.PlaceAutocomplete;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.model.PlaceOptions;
import com.mapboxweather.kamleshsahu.mapboxdemo.Activity.MainActivity;
import com.mapboxweather.kamleshsahu.mapboxdemo.Methods.TimeZoneOfOrigin;
import com.mapboxweather.kamleshsahu.mapboxdemo.ViewModels.MainActivityViewModel;
import com.mapboxweather.kamleshsahu.mapboxdemo.databinding.ActivityMainNewBinding;

import static com.mapboxweather.kamleshsahu.mapboxdemo.WeatherService.Constants.MapboxKey;
import static com.mapboxweather.kamleshsahu.mapboxdemo.WeatherService.Constants.REQUEST_CODE_AUTOCOMPLETE1;
import static com.mapboxweather.kamleshsahu.mapboxdemo.WeatherService.Constants.REQUEST_CODE_AUTOCOMPLETE2;

public class MainActivity_new extends AppCompatActivity {

    ActivityMainNewBinding activityMainNewBinding;
    MainActivityViewModel mainActivityViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_new);
        activityMainNewBinding= DataBindingUtil.setContentView(this,R.layout.activity_main_new);
        mainActivityViewModel= ViewModelProviders.of(this).get(MainActivityViewModel.class);

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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_AUTOCOMPLETE1) {
            CarmenFeature feature = PlaceAutocomplete.getPlace(data);
            //System.out.println("feature text :"+feature.text());
            Toast.makeText(this, feature.text(), Toast.LENGTH_LONG).show();
            mainActivityViewModel.setStart(new MLocation(feature.placeName(),feature.center()));

        }else if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_AUTOCOMPLETE2) {
            CarmenFeature feature = PlaceAutocomplete.getPlace(data);
            //System.out.println("feature text :"+feature.text());
            Toast.makeText(this, feature.text(), Toast.LENGTH_LONG).show();
            mainActivityViewModel.setDstn(new MLocation(feature.placeName(),feature.center()));
        }
    }

}
