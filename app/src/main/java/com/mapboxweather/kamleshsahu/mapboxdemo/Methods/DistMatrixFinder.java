//package com.mapboxweather.kamleshsahu.mapboxdemo.Methods;
//
//
//import android.os.Handler;
//import android.os.Message;
//
//import com.google.gson.Gson;
//import com.mapbox.api.directions.v5.DirectionsCriteria;
//import com.mapbox.api.matrix.v1.MapboxMatrix;
//import com.mapbox.api.matrix.v1.models.MatrixResponse;
//import com.mapbox.geojson.Point;
//import com.mapboxweather.kamleshsahu.mapboxdemo.Activity.SimpleMapViewActivity;
//import com.mapboxweather.kamleshsahu.mapboxdemo.Models.MyDistMatrixResponse;
//import com.mapboxweather.kamleshsahu.mapboxdemo.Models.Resp;
//import com.mapboxweather.kamleshsahu.mapboxdemo.Models.mError;
//
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//import java.util.TimeZone;
//
//
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//
//import static com.mapboxweather.kamleshsahu.mapboxdemo.Constants.ErrorHead_IntermFunction;
//import static com.mapboxweather.kamleshsahu.mapboxdemo.Constants.MapboxKey;
//
//public class DistMatrixFinder {
//
//    private Point origin;
//    private long time;
//    private String travelmode;
//    private List<Point> interms;
//    private retrofit2.Callback<MatrixResponse> matrixResponseCallback;
//    private Handler handler;
//    private int IntermInPartPos;
//
//
//    //     public DistMatrixFinder(Point origin, List<Point> interms,String travelmode, long time, retrofit2.Callback<MatrixResponse> matrixResponseCallback) {
////        this.origin=origin;
////        this.interms=interms;
////        this.time=time;
////        this.travelmode=travelmode;
////        this.matrixResponseCallback=matrixResponseCallback;
////    }
//    public DistMatrixFinder(int IntermInPartPos, Point origin, List<Point> interms, String travelmode, long time, Handler handler) {
//        this.origin = origin;
//        this.interms = interms;
//        this.time = time;
//        this.travelmode = travelmode;
//        //      this.matrixResponseCallback=matrixResponseCallback;
//        this.handler = handler;
//        this.IntermInPartPos = IntermInPartPos;
//    }
//
//    public static void main(String... args) {
//
//        Point sp = Point.fromLngLat(-105.2705, 40.015);
//        Point dp = Point.fromLngLat(-104.9653, 39.7348);
//        Point dp1 = Point.fromLngLat(-104.9653, 40.7348);
//        List<Point> interms = new ArrayList<Point>();
//        interms.add(dp);
//        interms.add(dp1);
//        // MatrixResponse response = new DistMatrixFinder(sp,interms,1).getMatrix();
//        // System.out.println(new Gson().toJson(response));
//    }
//
////
////    void getMatrix() {
////
////        Integer arr[] = new Integer[interms.size()];
////        for (int k = 0; k < interms.size(); k++) {
////            arr[k] = k + 1;
////        }
////
////        MapboxMatrix matrixcall = MapboxMatrix.builder()
////                     .accessToken(MapboxKey)
////                     .profile(travelmode)
////                     .coordinate(origin)
////                     .coordinates(interms)
////                     .destinations(arr)
////                     .build();
////
////             matrixcall.enqueueCall(matrixResponseCallback);
////         }
//
////    @Override
////    public void run() {
////        Integer arr[] = new Integer[interms.size()];
////        for (int k = 0; k < interms.size(); k++) {
////            arr[k] = k + 1;
////        }
////
////        MapboxMatrix matrixcall = MapboxMatrix.builder()
////                .accessToken(MapboxKey)
////                .profile(travelmode)
////                .coordinate(origin)
////                .coordinates(interms)
////                .sources(0)
////                .destinations(arr)
////                .build();
////
////        matrixcall.enqueueCall(matrixResponseCallback);
////
////    }
//
//
//    //    @Override
//    public void run() {
//        Integer arr[] = new Integer[interms.size()];
//        for (int k = 0; k < interms.size(); k++) {
//            arr[k] = k + 1;
//        }
//
//        MapboxMatrix matrixcall = MapboxMatrix.builder()
//                .accessToken(MapboxKey)
//                .profile(travelmode)
//                .coordinate(origin)
//                .coordinates(interms)
//                .sources(0)
//                .destinations(arr)
//                .build();
//
//
//        matrixcall.enqueueCall(new retrofit2.Callback<MatrixResponse>() {
//            @Override
//            public void onResponse(Call<MatrixResponse> call, Response<MatrixResponse> response) {
//
//
//                if (response.isSuccessful()) {
//                    System.out.println(response.code());
//                    System.out.println(response.raw());
//                    MatrixResponse distanceMatrix = response.body();
//                    for (int k = 0; k < distanceMatrix.destinations().size(); k++) {
//
//
//                        long duration = distanceMatrix.durations().get(0)[k].intValue();
//
//                        long arrival_time_millis = dep_time_millis + duration * 1000;
//                        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
//                        //  System.out.println("timezone id :"+timezoneid);
//                        //   if(timezoneid!=null)
//
//                        sdf.setTimeZone(TimeZone.getTimeZone(timezoneid));
//                        String time = sdf.format(arrival_time_millis);
//                        final int FinalK = k;
//
//
//                        new WeatherFinder(FinalK, interms.get(FinalK), distanceMatrix, time).fetchWeather();
//
//
//                        //  return new Item(point, resp.getCurrently(), stn_arrtime, distanceMatrix.rows[0].elements[k].DistanceCalculator.humanReadable, distanceMatrix.destinationAddresses[k].toString());
//                    }
//                } else {
//                    Message message = new Message();
//                    message.obj = new Resp(new mError(ErrorHead_IntermFunction, response.message()));
//                    SimpleMapViewActivity.myItemhandler.sendMessage(message);
//
//                }
//            }
//
//            @Override
//            public void onFailure(Call<MatrixResponse> call, Throwable t) {
//                t.printStackTrace();
//                Message message = new Message();
//                message.obj = new Resp(new mError(ErrorHead_IntermFunction, t.getMessage()));
//                SimpleMapViewActivity.myItemhandler.sendMessage(message);
//            }
//        });
//
//    }
//}
//
//
//
//
//