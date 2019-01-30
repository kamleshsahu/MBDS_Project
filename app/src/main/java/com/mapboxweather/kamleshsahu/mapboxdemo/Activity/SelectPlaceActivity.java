package com.mapboxweather.kamleshsahu.mapboxdemo.Activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.PlaceAutocomplete;
import com.mapboxweather.kamleshsahu.mapboxdemo.R;

import static com.mapboxweather.kamleshsahu.mapboxdemo.Constants.MapboxKey;


public class SelectPlaceActivity extends AppCompatActivity {
    int REQUEST_CODE_AUTOCOMPLETE1=1001;
    int REQUEST_CODE_AUTOCOMPLETE2=1002;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_place);


        if(getIntent().getIntExtra("src",1)==1) {
            Intent intent = new PlaceAutocomplete.IntentBuilder()
                    .accessToken(MapboxKey)
                    //     .placeOptions(placeOptions)
                    .build(SelectPlaceActivity.this);

            startActivityForResult(intent, REQUEST_CODE_AUTOCOMPLETE1);
        }else{
            Intent intent = new PlaceAutocomplete.IntentBuilder()
                    .accessToken(MapboxKey)
                    //     .placeOptions(placeOptions)
                    .build(SelectPlaceActivity.this);

            startActivityForResult(intent, REQUEST_CODE_AUTOCOMPLETE2);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_AUTOCOMPLETE1) {
            CarmenFeature feature = PlaceAutocomplete.getPlace(data);
            System.out.println("feature text :"+feature.text());

            Toast.makeText(this, feature.text(), Toast.LENGTH_LONG).show();

            MainActivity.tv_source.setText(feature.placeName());
            MainActivity.sp=feature.center();

            finish();

        }else if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_AUTOCOMPLETE2) {
            CarmenFeature feature = PlaceAutocomplete.getPlace(data);
            System.out.println("feature text :"+feature.text());

            Toast.makeText(this, feature.text(), Toast.LENGTH_LONG).show();


            MainActivity.tv_dstn.setText(feature.placeName());
            MainActivity.dp=feature.center();


            finish();
        }
    }

}
