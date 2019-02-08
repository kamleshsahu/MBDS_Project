package com.mapboxweather.kamleshsahu.mapboxdemo.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.mapbox.api.directions.v5.DirectionsCriteria;
import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.PlaceAutocomplete;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.model.PlaceOptions;
import com.mapboxweather.kamleshsahu.mapboxdemo.Adapter.RouteListAdapter;
import com.mapboxweather.kamleshsahu.mapboxdemo.Methods.RouteFinder;
import com.mapboxweather.kamleshsahu.mapboxdemo.Methods.TimeZoneOfOrigin;
import com.mapboxweather.kamleshsahu.mapboxdemo.Models.TimeZoneApiResponse;
import com.mapboxweather.kamleshsahu.mapboxdemo.R;

import java.util.Calendar;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.mapboxweather.kamleshsahu.mapboxdemo.Constants.ErrorHead_StartDest_NotFilled;
import static com.mapboxweather.kamleshsahu.mapboxdemo.Constants.ErrorHeading;
import static com.mapboxweather.kamleshsahu.mapboxdemo.Constants.ErrorMsg_StartDest_NotFilled;
import static com.mapboxweather.kamleshsahu.mapboxdemo.Constants.MapboxKey;
import static com.mapboxweather.kamleshsahu.mapboxdemo.Constants.REQUEST_CODE_AUTOCOMPLETE1;
import static com.mapboxweather.kamleshsahu.mapboxdemo.Constants.REQUEST_CODE_AUTOCOMPLETE2;
import static com.mapboxweather.kamleshsahu.mapboxdemo.Constants.month;

public class MainActivity extends AppCompatActivity {


     static RecyclerView recyclerView;
     static TextView tv_source, tv_dstn;
     static Point sp=null,dp=null;

     public static int selectedroute=0;
     static DirectionsResponse directionapiresp;


    int tempYear,tempMonth,tempDay;
    public static int  DistanceUnit = 0;
    static int mYear, mMonth, mDay, mHour, mMinute;
    static String travelmode=DirectionsCriteria.PROFILE_DRIVING_TRAFFIC;
//    static boolean HIGHWAYS=false;
////    static boolean TOLLS=false;
////    static boolean FERRIES=false;
    String avoid=null;
    static long jstart_date_millis, jstart_time_millis;
    static String timezone;

     TextView departAt;

     ProgressDialog progress;

    static android.app.AlertDialog.Builder bld;
    final MainActivity cont=MainActivity.this;

    CheckBox tolls;
    CheckBox ferries;
    CheckBox highway;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel_with);
        tv_source=findViewById(R.id.source);
        tv_dstn=findViewById(R.id.destination);
        recyclerView = findViewById(R.id.recycler);
        progress=new ProgressDialog(this);
        departAt = findViewById(R.id.date1);
      
        final Calendar c = Calendar.getInstance();
        timezone=c.getTimeZone().getID();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);
        jstart_date_millis=c.getTimeInMillis()-((mHour*60+mMinute)*60*1000);
        jstart_time_millis=(mHour*60+mMinute)*60*1000;


        ferries = findViewById(R.id.ferries);
        highway = findViewById(R.id.highway);
        tolls = findViewById(R.id.tolls);
////                        ((CheckBox) findViewById(R.id.tolls)).setChecked(false);
////                        ((CheckBox) findViewById(R.id.highway)).setChecked(false);


        String sHour = mHour < 10 ? "0" + mHour : "" + mHour;
        String sMinute = mMinute < 10 ? "0" + mMinute : "" + mMinute;
        String curr_time = sHour + ":" + sMinute;

        departAt.setText(curr_time+","+mDay+" "+month[mMonth]+" "+String.valueOf(mYear).substring(2));


        ((ImageView) (findViewById(R.id.a))).setImageResource(R.drawable.car_on);

        ((ImageView) (findViewById(R.id.c))).setImageResource(R.drawable.walk_off);
        ((ImageView) (findViewById(R.id.d))).setImageResource(R.drawable.bike_off);


        findViewById(R.id.a).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  radioGroup.check(R.id._1);
                ((ImageView) (findViewById(R.id.a))).setImageResource(R.drawable.car_on);
                ((ImageView) (findViewById(R.id.c))).setImageResource(R.drawable.walk_off);
                ((ImageView) (findViewById(R.id.d))).setImageResource(R.drawable.bike_off);

                highway.setVisibility(View.VISIBLE);
                tolls.setVisibility(View.VISIBLE);
                ferries.setVisibility(View.VISIBLE);

                travelmode=DirectionsCriteria.PROFILE_DRIVING_TRAFFIC;
                resetresult();
                resetrestrictions();
                System.out.println("travelmode :"+travelmode);
            }
        });

        findViewById(R.id.c).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //   radioGroup.check(R.id._3);
                ((ImageView) (findViewById(R.id.a))).setImageResource(R.drawable.car_off);
                ((ImageView) (findViewById(R.id.c))).setImageResource(R.drawable.walk_on);
                ((ImageView) (findViewById(R.id.d))).setImageResource(R.drawable.bike_off);

                highway.setVisibility(View.INVISIBLE);
                tolls.setVisibility(View.INVISIBLE);
                ferries.setVisibility(View.INVISIBLE);

                travelmode=DirectionsCriteria.PROFILE_WALKING;
                resetrestrictions();
                resetresult();
                System.out.println("travelmode :"+travelmode);
            }
        });
        findViewById(R.id.d).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                ((ImageView) (findViewById(R.id.a))).setImageResource(R.drawable.car_off);
                ((ImageView) (findViewById(R.id.c))).setImageResource(R.drawable.walk_off);
                ((ImageView) (findViewById(R.id.d))).setImageResource(R.drawable.bike_on);

                highway.setVisibility(View.INVISIBLE);
                tolls.setVisibility(View.INVISIBLE);
                ferries.setVisibility(View.VISIBLE);

                travelmode=DirectionsCriteria.PROFILE_CYCLING;
                resetrestrictions();
                resetresult();
                System.out.println("travelmode :"+travelmode);
            }
        });

        findViewById(R.id.option).setOnClickListener(new View.OnClickListener() {
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

//        ((RadioGroup) findViewById(R.id.distance_unit)).check(R.id.automatic);
//        ((RadioGroup) findViewById(R.id.distance_unit)).setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup radioGroup, int i) {
//                int selectedId = radioGroup.getCheckedRadioButtonId();
//                DistanceUnit = Integer.parseInt(findViewById(selectedId).getTag().toString());
//                System.out.println(DistanceUnit);
//                resetresult();
//            }
//        });


//        ((CheckBox) findViewById(R.id.highway))
//                .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                    @Override
//                    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
//                      //  HIGHWAYS=isChecked;
//                        avoid=DirectionsCriteria.EXCLUDE_MOTORWAY;
//                        resetresult();
//
//                        ((CheckBox) findViewById(R.id.highway)).setChecked(isChecked);
////                        ((CheckBox) findViewById(R.id.ferries)).setChecked(false);
////                        ((CheckBox) findViewById(R.id.tolls)).setChecked(false);
//                    }
//                });
//        ((CheckBox) findViewById(R.id.tolls))
//                .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                    @Override
//                    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
//                       // TOLLS=isChecked;
//                        avoid=DirectionsCriteria.EXCLUDE_TOLL;
//                        resetresult();
//
//                        ((CheckBox) findViewById(R.id.tolls)).setChecked(!isChecked);
////                       ((CheckBox) findViewById(R.id.highway)).setChecked(false);
////                        ((CheckBox) findViewById(R.id.ferries)).setChecked(false);
//
//
//                    }
//                });
//
//        ((CheckBox) findViewById(R.id.ferries))
//                    .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                    @Override
//                    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
//                       // FERRIES=isChecked;
//                        avoid=DirectionsCriteria.EXCLUDE_FERRY;
//
//                        ((CheckBox) findViewById(R.id.ferries)).setChecked(!isChecked);
////                        ((CheckBox) findViewById(R.id.tolls)).setChecked(false);
////                        ((CheckBox) findViewById(R.id.highway)).setChecked(false);
//
//                        resetresult();
//                    }
//                });


        highway.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetresult();
                if(highway.isChecked()) {
                    avoid = DirectionsCriteria.EXCLUDE_MOTORWAY;
                }else avoid=null;
                if(tolls.isChecked())tolls.setChecked(false);
                if(ferries.isChecked())ferries.setChecked(false);
            }
        });

        tolls.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetresult();
                if(tolls.isChecked()) {
                    avoid = DirectionsCriteria.EXCLUDE_TOLL;
                }else avoid=null;
                if(highway.isChecked())highway.setChecked(false);
                if(ferries.isChecked())ferries.setChecked(false);
            }
        });



        ferries.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetresult();
                if(ferries.isChecked()) {
                    avoid=DirectionsCriteria.EXCLUDE_FERRY;
                }else avoid=null;
                if(highway.isChecked())highway.setChecked(false);
                if(tolls.isChecked())tolls.setChecked(false);
            }
        });


        bld = new AlertDialog.Builder(cont);


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


            progress.setTitle("Fetching TimeZone...");
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.setIndeterminate(true);
            progress.getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            progress.show();

            new TimeZoneOfOrigin(sp, timeZoneApiRespCallback).getTimeZone();



        }else if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_AUTOCOMPLETE2) {
            CarmenFeature feature = PlaceAutocomplete.getPlace(data);
            System.out.println("feature text :"+feature.text());

            Toast.makeText(this, feature.text(), Toast.LENGTH_LONG).show();


            MainActivity.tv_dstn.setText(feature.placeName());
            MainActivity.dp=feature.center();



        }
    }

    public void findRoute_onClick(View view) {
        if(sp!=null && dp!=null) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

            progress.setTitle("Loading Routes...");
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.setIndeterminate(true);
            progress.getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            progress.show();

            new RouteFinder(sp,dp,travelmode,avoid,RouteRespCallback).find();
        }else{
            displayError(ErrorHead_StartDest_NotFilled,ErrorMsg_StartDest_NotFilled);

        }
    }

    Callback<TimeZoneApiResponse> timeZoneApiRespCallback=new Callback<TimeZoneApiResponse>() {
        @Override
        public void onResponse(Call<TimeZoneApiResponse> call, Response<TimeZoneApiResponse> response) {
            progress.dismiss();
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

            if(response.isSuccessful()) {
                timezone=response.body().getTimeZoneId();
                final Calendar c = Calendar.getInstance();
                c.setTimeZone(TimeZone.getTimeZone(timezone));
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);
                jstart_date_millis = c.getTimeInMillis() - ((mHour * 60 + mMinute) * 60 * 1000);
                jstart_time_millis = (mHour * 60 + mMinute) * 60 * 1000;


                String sHour = mHour < 10 ? "0" + mHour : "" + mHour;
                String sMinute = mMinute < 10 ? "0" + mMinute : "" + mMinute;
                String curr_time = sHour + ":" + sMinute;
                //       time.setText(curr_time);
                departAt.setText(curr_time + "," + mDay + " " + month[mMonth] + " " + String.valueOf(mYear).substring(2));
            }else{
                  displayError(ErrorHeading,response.message());
            }
        }

        @Override
        public void onFailure(Call<TimeZoneApiResponse> call, Throwable t) {
            progress.dismiss();
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            t.printStackTrace();
            displayError(ErrorHeading,t.getMessage());
        }
    };

    Callback<DirectionsResponse> RouteRespCallback= new Callback<DirectionsResponse>() {
            @Override
            public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {

                progress.dismiss();
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                if(response.isSuccessful()) {
                    directionapiresp = response.body();
                    LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext());
                    recyclerView.setLayoutManager(manager);
                    recyclerView.setHasFixedSize(true);

                    RouteListAdapter adapterList = new RouteListAdapter(getApplicationContext(), directionapiresp);

                    recyclerView.setAdapter(adapterList);

                }else {
                    displayError(ErrorHeading,response.message());
                }
            }

            @Override
            public void onFailure(Call<DirectionsResponse> call, Throwable t) {
                progress.dismiss();
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                t.printStackTrace();
                displayError(ErrorHeading,t.getMessage());
   //             Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }


        };

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
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void datePicker(View view) {
        // Get Current Date


        final DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                        departAt.setText("00:00,"+dayOfMonth + " " + month[monthOfYear] + " " + String.valueOf(year).substring(2));
                        Calendar cal = Calendar.getInstance();
                        cal.setTimeZone(TimeZone.getTimeZone(timezone));
                        cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        cal.set(Calendar.MONTH, monthOfYear);
                        cal.set(Calendar.YEAR, year);
                        cal.set(Calendar.HOUR_OF_DAY, 0);
                        cal.set(Calendar.MINUTE, 0);
                        tempDay=dayOfMonth;
                        tempMonth=monthOfYear;
                        tempYear=year;

                        jstart_date_millis = cal.getTimeInMillis();

                        timePicker();

                        //*************Call Time Picker Here ********************

                        resetresult();
                    }
                }, mYear, mMonth, mDay);

        datePickerDialog.show();
    }

    private void timePicker() {
        // Get Current Time


        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                        mHour = hourOfDay;
                        mMinute = minute;

                        String sHour = mHour < 10 ? "0" + mHour : "" + mHour;
                        String sMinute = mMinute < 10 ? "0" + mMinute : "" + mMinute;
                        String set_time = sHour + ":" + sMinute;

                        departAt.setText(set_time + "," +tempDay + " " + month[tempMonth] + " " + String.valueOf(tempYear).substring(2));

                        jstart_time_millis = (mHour * 60 + mMinute) * 60 * 1000;

                    }
                }, mHour, mMinute, true);

        timePickerDialog.show();

    }

    static void resetresult(){
        directionapiresp=null;
        recyclerView.setAdapter(null);
    };

    void resetrestrictions(){
//        HIGHWAYS=false;
//        TOLLS=false;
//        FERRIES=false;
        avoid=null;
        ((CheckBox) findViewById(R.id.tolls)).setChecked(false);
        ((CheckBox) findViewById(R.id.highway)).setChecked(false);
        ((CheckBox) findViewById(R.id.ferries)).setChecked(false);
    }

    static void displayError(String title, String msg){

        bld.setMessage(msg);
        bld.setNeutralButton("OK", null);
        bld.setTitle(title);
        Log.d("TAG", "Showing alert dialog: " + msg);
        Dialog dialog=bld.create();
        //   dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        dialog.show();
    };


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        DistanceUnit = 0;
        directionapiresp=null;
        selectedroute=0;
        travelmode=DirectionsCriteria.PROFILE_DRIVING_TRAFFIC;
        avoid=null;
//        HIGHWAYS=false;
//        TOLLS=false;
//        FERRIES=false;
        sp = null;
        dp = null;
    }

   void resetInputs(){
       selectedroute=0;
       travelmode=DirectionsCriteria.PROFILE_DRIVING_TRAFFIC;
       avoid=null;
       sp = null;
       dp = null;
    }


    public void source_onclick(View view) {
//        Intent intent=new Intent(MainActivity.this,SelectPlaceActivity.class);
//        intent.putExtra("src",1);
//        startActivity(intent);
        PlaceOptions placeOptions=PlaceOptions
                .builder()
                .hint("Start Address...")
                .backgroundColor(getResources().getColor(R.color.loo_pre))
                .build();

        Intent intent = new PlaceAutocomplete.IntentBuilder()
                .accessToken(MapboxKey)
                .placeOptions(placeOptions)
                .build(MainActivity.this);

        startActivityForResult(intent, REQUEST_CODE_AUTOCOMPLETE1);
        resetresult();
    }

    public void destination_onclick(View view) {
//        Intent intent=new Intent(MainActivity.this,SelectPlaceActivity.class);
//        intent.putExtra("src",0);
//        startActivity(intent);

        PlaceOptions placeOptions=PlaceOptions
                .builder()
                .hint("Destination Address...")
                .backgroundColor(getResources().getColor(R.color.loo_pre))
                .build();

        Intent intent = new PlaceAutocomplete.IntentBuilder()
                .accessToken(MapboxKey)
                .placeOptions(placeOptions)
                .build(MainActivity.this);

        startActivityForResult(intent, REQUEST_CODE_AUTOCOMPLETE2);
        resetresult();
    }


    public void swap_onClick(View view) {
        if(sp!=null && dp!=null) {
            resetresult();
            Point temp = sp;
            sp = dp;
            dp = temp;


            progress.setTitle("Fetching TimeZone...");
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.setIndeterminate(true);
            progress.getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            progress.show();

            new TimeZoneOfOrigin(sp, timeZoneApiRespCallback).getTimeZone();

            String srctemp = tv_source.getText().toString();
            tv_source.setText(tv_dstn.getText());
            tv_dstn.setText(srctemp);

        }else{
            displayError(ErrorHead_StartDest_NotFilled,ErrorMsg_StartDest_NotFilled);        }
    }
}
