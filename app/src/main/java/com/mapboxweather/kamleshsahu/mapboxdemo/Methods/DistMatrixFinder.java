package com.mapboxweather.kamleshsahu.mapboxdemo.Methods;


import com.google.gson.Gson;
import com.mapbox.api.directions.v5.DirectionsCriteria;
import com.mapbox.api.matrix.v1.MapboxMatrix;
import com.mapbox.api.matrix.v1.models.MatrixResponse;
import com.mapbox.geojson.Point;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;




import static com.mapboxweather.kamleshsahu.mapboxdemo.Constants.MapboxKey;

public class DistMatrixFinder {

    private Point origin;
    private long time;
    private String travelmode;
    private List<Point> interms;
    private retrofit2.Callback<MatrixResponse> matrixResponseCallback;



     public DistMatrixFinder(Point origin, List<Point> interms,String travelmode, long time, retrofit2.Callback<MatrixResponse> matrixResponseCallback) {
        this.origin=origin;
        this.interms=interms;
        this.time=time;
        this.travelmode=travelmode;
        this.matrixResponseCallback=matrixResponseCallback;
    }


    public static void main(String... args) {

        Point sp = Point.fromLngLat(-105.2705, 40.015);
        Point dp = Point.fromLngLat(-104.9653, 39.7348);
        Point dp1 = Point.fromLngLat(-104.9653, 40.7348);
        List<Point> interms=new ArrayList<Point>();
        interms.add(dp);
        interms.add(dp1);
       // MatrixResponse response = new DistMatrixFinder(sp,interms,1).getMatrix();
       // System.out.println(new Gson().toJson(response));
    }


    void getMatrix() {

         int count=10;
         int currsize=interms.size();
         while(currsize>0) {

             if(currsize<10)
               count=interms.size()%10;

             currsize-=10;

             Integer arr[] = new Integer[count];
             for (int k = 0; k < count; k++) {
                 arr[k] = k + 1;
             }

             List<Point> sublist=interms.subList(0,count-1);
             interms.removeAll(sublist);
             MapboxMatrix matrixcall = MapboxMatrix.builder()
                     .accessToken(MapboxKey)
                     .profile(travelmode)
                     .coordinate(origin)
                     .coordinates(sublist)
                     .destinations(arr)
                     .build();

             matrixcall.enqueueCall(matrixResponseCallback);
         }
      //  MatrixResponse matrixResponse = null;

//        try {
//            return matrixcall.executeCall().body();
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }



    }


//        GeoApiContext context = new GeoApiContext.Builder().apiKey(Main.googleMapsKey)
//                .build();
//
//        final DistanceMatrixApiRequest apiRequest =new DistanceMatrixApiRequest(context);
//        apiRequest.origins(origin);
//        apiRequest.destinations(list);
//
//
//        switch (travelmode){
//            case 0 :apiRequest.mode(TravelMode.DRIVING);
//            break;
//            case 1: apiRequest.mode(TravelMode.BICYCLING);
//            break;
//            case 2: apiRequest.mode(TravelMode.WALKING);
//            break;
//        }
//
//
//        switch(DistanceUnit) {
//           case 0 :break;
//           case 1:apiRequest.units(Unit.IMPERIAL);break;
//           case 2:apiRequest.units(Unit.METRIC);break;
//           default:
//
//        }
//
//
//       if(time > System.currentTimeMillis())
//           apiRequest.departureTime(new DateTime(time));
//
//
//
//        try {
//            DistanceMatrix dtmat=apiRequest.await();
//            return dtmat;
//        }catch (Exception e){
//            e.printStackTrace();
//            return null;
//        }





}