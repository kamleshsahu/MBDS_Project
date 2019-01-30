package com.mapboxweather.kamleshsahu.mapboxdemo.Activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.geometry.LatLng;
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
     CardView date_holder;

    static ProgressDialog progress;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel_with);
        tv_source=findViewById(R.id.source);
        tv_dstn=findViewById(R.id.destination);
        recyclerView = findViewById(R.id.recycler);

        tv_source.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              Intent intent=new Intent(MainActivity.this,SelectPlaceActivity.class);
              intent.putExtra("src",1);
              startActivity(intent);
            }
        });

        tv_dstn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,SelectPlaceActivity.class);
                intent.putExtra("src",0);
                startActivity(intent);
            }
        });


        findViewById(R.id.submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new RouteFinder(sp,dp,travelmode,avoid,responseCallback).find();
            }
        });


        date_holder = findViewById(R.id.card_date);
        departAt = findViewById(R.id.date1);
        date_holder.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                datePicker();

            }
        });

        final Calendar c = Calendar.getInstance();
        timezone=c.getTimeZone().getID();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);
        jstart_date_millis=c.getTimeInMillis()-((mHour*60+mMinute)*60*1000);
        jstart_time_millis=(mHour*60+mMinute)*60*1000;


        String sHour = mHour < 10 ? "0" + mHour : "" + mHour;
        String sMinute = mMinute < 10 ? "0" + mMinute : "" + mMinute;
        String curr_time = sHour + ":" + sMinute;

        departAt.setText(curr_time+","+mDay+" "+month[mMonth]+" "+String.valueOf(mYear).substring(2));


        ((ImageView) (findViewById(R.id.a))).setImageResource(R.drawable.car_on);

        ((ImageView) (findViewById(R.id.c))).setImageResource(R.drawable.walk_off);
        ((ImageView) (findViewById(R.id.d))).setImageResource(R.drawable.bike_off);


        findViewById(R.id.swap).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(sp!=null && dp!=null) {
                    resetresult();
                    Point temp = sp;
                    sp = dp;
                    dp = temp;

                    new TimeZoneOfOrigin(sp, new Callback<TimeZoneApiResponse>() {
                        @Override
                        public void onResponse(Call<TimeZoneApiResponse> call, Response<TimeZoneApiResponse> response) {
                            timezone=response.body().getTimeZoneId();

                            if(timezone!=null) {
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
                              //  displayError(emsgHead,emsg);
                            }
                        }

                        @Override
                        public void onFailure(Call<TimeZoneApiResponse> call, Throwable t) {
                                  t.printStackTrace();
                        }
                    }).getTimeZone();

                    String srctemp = tv_source.getText().toString();
                    tv_source.setText(tv_dstn.getText());
                    tv_dstn.setText(srctemp);

                }else{
                 //   displayError("Start/Destination Address no filled","Please fill Start and Destination to find routes");
                    //Toast.makeText(context,"start or end Address is null",Toast.LENGTH_LONG).show();
                }
            }
        });

        findViewById(R.id.a).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  radioGroup.check(R.id._1);
                ((ImageView) (findViewById(R.id.a))).setImageResource(R.drawable.car_on);
                ((ImageView) (findViewById(R.id.c))).setImageResource(R.drawable.walk_off);
                ((ImageView) (findViewById(R.id.d))).setImageResource(R.drawable.bike_off);
                ((CheckBox) findViewById(R.id.highway)).setVisibility(View.VISIBLE);
                ((CheckBox) findViewById(R.id.tolls)).setVisibility(View.VISIBLE);
                ((CheckBox) findViewById(R.id.ferries)).setVisibility(View.VISIBLE);
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
                ((CheckBox) findViewById(R.id.highway)).setVisibility(View.GONE);
                ((CheckBox) findViewById(R.id.tolls)).setVisibility(View.GONE);
                ((CheckBox) findViewById(R.id.ferries)).setVisibility(View.GONE);
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
                ((CheckBox) findViewById(R.id.highway)).setVisibility(View.GONE);
                ((CheckBox) findViewById(R.id.tolls)).setVisibility(View.GONE);
                ((CheckBox) findViewById(R.id.ferries)).setVisibility(View.VISIBLE);
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


        ((CheckBox) findViewById(R.id.highway))
                .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                      //  HIGHWAYS=isChecked;
                        avoid=DirectionsCriteria.EXCLUDE_MOTORWAY;
                        resetresult();
                    }
                });
        ((CheckBox) findViewById(R.id.tolls))
                .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                       // TOLLS=isChecked;
                        avoid=DirectionsCriteria.EXCLUDE_TOLL;
                        resetresult();
                    }
                });

        ((CheckBox) findViewById(R.id.ferries))
                .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                       // FERRIES=isChecked;
                        avoid=DirectionsCriteria.EXCLUDE_FERRY;
                        resetresult();
                    }
                });





    }

    Callback<DirectionsResponse> responseCallback= new Callback<DirectionsResponse>() {
            @Override
            public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {

                directionapiresp = response.body();
                LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext());
                recyclerView.setLayoutManager(manager);
                recyclerView.setHasFixedSize(true);

                RouteListAdapter adapterList = new RouteListAdapter(getApplicationContext(), directionapiresp);

                recyclerView.setAdapter(adapterList);
            }

            @Override
            public void onFailure(Call<DirectionsResponse> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
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
 //               directionapi=null;
                recyclerView.setAdapter(null);
 //               requestDirection();
//                Toast.makeText(this, "Retrying...", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.Subscription:
//                Intent intent=new Intent(getApplicationContext(), Subscription.class);
//                startActivity(intent);

                //               Toast.makeText(this, "Retrying...", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_clr:
//                Toast.makeText(this, "clear", Toast.LENGTH_SHORT).show();

                directionapiresp=null;
                selectedroute=0;

 //               travelmode=0;
 //               HIGHWAYS=false;TOLLS=false;FERRIES=false;
                travelmode=null;
                avoid=null;
                sp = null;
                dp = null;

                finish();
                startActivity(getIntent());

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void datePicker() {

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

//    static void displayError(String title, String msg){
//
//        bld.setMessage(msg);
//        bld.setNeutralButton("OK", null);
//        bld.setTitle(title);
//        Log.d("TAG", "Showing alert dialog: " + msg);
//        Dialog dialog=bld.create();
//        //   dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
//        dialog.show();
//    };


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


}
