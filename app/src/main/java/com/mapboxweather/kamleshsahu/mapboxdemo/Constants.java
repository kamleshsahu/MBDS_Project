package com.mapboxweather.kamleshsahu.mapboxdemo;

import com.mapbox.api.directions.v5.DirectionsCriteria;

import java.util.HashMap;
import java.util.Map;

public class Constants {

   public final static String[] month = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
   public final static String[] strDays = new String[] { "Sun", "Mon", "Tue", "Wed", "Thu","Fri", "Sat" };
   public final static  String MapboxKey="pk.eyJ1IjoibXNvZnR0ZXhhc2NvbnN1bHRhbmN5IiwiYSI6ImNqcXBpaDdvdDAxcmQ0MnJ0OGxxODJyMG0ifQ.yWdirT8JDDeACaSfA9NF3A";
   public final static  String DarkskyKey="4baf68d693488da5be0ec56717dd5be2";
   public final static String GoogleKey="AIzaSyCv_imK5ydtkdWnGJP1Dbt-DT07UdvyDeo";

   public static final String STEP_MANEUVER_MODIFIER_UTURN = "uturn";
   public static final String STEP_MANEUVER_MODIFIER_SHARP_RIGHT = "sharp right";
   public static final String STEP_MANEUVER_MODIFIER_RIGHT = "right";
   public static final String STEP_MANEUVER_MODIFIER_SLIGHT_RIGHT = "slight right";
   public static final String STEP_MANEUVER_MODIFIER_STRAIGHT = "straight";
   public static final String STEP_MANEUVER_MODIFIER_SLIGHT_LEFT = "slight left";
   public static final String STEP_MANEUVER_MODIFIER_LEFT = "left";
   public static final String STEP_MANEUVER_MODIFIER_SHARP_LEFT = "sharp left";


   public static final String WEATHER_CLEAR_DAY = "clear-day";
   public static final String WEATHER_CLOUDY = "cloudy";
   public static final String WEATHER_CLEAR_NIGHT = "clear-night";
   public static final String WEATHER_FOG = "fog";
   public static final String WEATHER_HAIL = "hail";
   public static final String WEATHER_PARTLY_CLOUDY_DAY = "partly-cloudy-day";
   public static final String WEATHER_PARTLY_CLOUDY_NIGHT = "partly-cloudy-night";
   public static final String WEATHER_RAIN = "rain";
   public static final String WEATHER_SLEET = "sleet";
   public static final String WEATHER_SNOW = "snow";
   public static final String WEATHER_THUNDERSTORM= "thunderstorm";

   public static final String WEATHER_TORNADO = "tornado";
   public static final String WEATHER_WIND = "wind";


   public static final String fastest_route="Fastest route,then usual traffic";

   public static String SKU_INFINITE_GAS_MONTHLY = "monthly_01";
   public static String SKU_INFINITE_GAS_QUATERLY = "quarterly_03";
   public static String SKU_INFINITE_GAS_HALFYEARLY = "halfyearly_06";
   public static String SKU_INFINITE_GAS_YEARLY = "yearly_12";

   public static String TRIALY_APP_KEY = "CNXFXUSWNXNREPZN6FW"; //TODO: Replace with your app key, which can be found on your Trialy developer dashboard
   public static String TRIALY_SKU = "premium_test"; //TODO: Replace with a trial SKU, which can be found on your Trialy developer dashboard. Each app can have multiple trials
   public static String base64EncodedPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAsGAwg6yWWzv0sh77t3Ba+xkYOGxM2pfueFWaaVDt6eVLqbh2xbMpnThRWhuMAkFrEkLdN1DJWDZu1UuQZkUq7J4oUff3fpJny87yG8ATjBhxe1eT7bNXHyL66J/BVDW4rCC98bW4LE/ApsIdllFDxzxif5YM1wL8+E1X1J6liwnbwcjFv7mUWT/eCqMXe6OYegcc3d4Rvb/tinDTxDqAwWxK4SqwmRpFFzNnVen0X/Y+AG3iAWiTEmrF1MZuVn19iD+PxTSAE2e1Xsrp5l6TZlLWBMCTLXBXau2fMC6nAF9HJ63kLzJ604q713gP834mz/8fOUWjM2UN1QyFCMhLKwIDAQAB" ;


   public static final String ErrorHeading="Error";
   public static final String ErrorDesc="";

   public static final String ErrorHead_StartDest_NotFilled="Start/Destination Address not filled";
   public static final String ErrorMsg_StartDest_NotFilled="Please Select Start and Destination to find routes";

   public static final String ErrorHead_Weather="Error fetching Weather";
   public static final String ErrorHead_STEP="Error in Step Activity";
   public static final String ErrorHead_MainFunction="Error in Main Function";
   public static final String ErrorHead_IntermFunction="Error in Interm_Point Function";

   public static final String DarkSky_BaseURL="https://api.darksky.net/";

   public static final int REQUEST_CODE_AUTOCOMPLETE1=1001;
   public static final int REQUEST_CODE_AUTOCOMPLETE2=1002;

//   public static Map<String,Integer> max_Api_Count=new HashMap<>();
//
//   public Constants() {
//      max_Api_Count.put(DirectionsCriteria.PROFILE_DRIVING_TRAFFIC,9);
//      max_Api_Count.put(DirectionsCriteria.PROFILE_DRIVING,24);
//      max_Api_Count.put(DirectionsCriteria.PROFILE_CYCLING,24);
//      max_Api_Count.put(DirectionsCriteria.PROFILE_WALKING,24);
//   }

 public static int getMax_API_Count(String travelmode){

      switch (travelmode){
         case DirectionsCriteria.PROFILE_DRIVING_TRAFFIC :return 9;
         case DirectionsCriteria.PROFILE_DRIVING :return 24;
         case DirectionsCriteria.PROFILE_CYCLING :return 24;
         case DirectionsCriteria.PROFILE_WALKING :return 24;

         default:return 9;
      }

   }
}
