package com.mapboxweather.kamleshsahu.mapboxdemo.WeatherService_Navigation.Methods;


import com.mapbox.api.matrix.v1.MapboxMatrix;
import com.mapbox.api.matrix.v1.models.MatrixResponse;
import com.mapbox.geojson.Point;
import com.mapboxweather.kamleshsahu.mapboxdemo.WeatherService_Navigation.Interface.PointMatrixListener;
import com.mapboxweather.kamleshsahu.mapboxdemo.WeatherService_Navigation.Models.mPoint;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.mapboxweather.kamleshsahu.mapboxdemo.WeatherService_Navigation.Constants.MapboxKey;
import static com.mapboxweather.kamleshsahu.mapboxdemo.WeatherService_Navigation.Constants.getMax_API_Count;


public class PointMatrix {
    private Point origin=null;
    private long jstarttime;
    private String timezoneid;
    private long aft_duration;
    private Map<Integer, mPoint> interms;
    MatrixResponse distanceMatrix;
    private long dep_time_millis;
    private String travelmode;

    int max_Api_Count;
    int step_id;

    PointMatrix(int step_id, Point origin, Map<Integer, mPoint> interms, String travelmode, String timezoneid, long jstarttime, long aft_duration) {
      this.origin=origin;
      this.jstarttime=jstarttime;
      this.timezoneid=timezoneid;
      this.aft_duration=aft_duration;
      this.interms=interms;
      this.travelmode=travelmode;
      this.step_id=step_id;
     }

     PointMatrixListener matrixListener=null;

     void calc() {

        dep_time_millis = jstarttime + aft_duration * 1000;

        max_Api_Count = getMax_API_Count(travelmode);

        List<List<Point>> intermsInParts= SplitInParts.split_in_parts(interms,max_Api_Count);


        for (int i = 0; i < intermsInParts.size(); i++) {

            int finalI = i;

            Integer arr[] = new Integer[intermsInParts.get(finalI).size()];
            for (int k = 0; k < intermsInParts.get(finalI).size(); k++) {
                arr[k] = k + 1;
            }

            MapboxMatrix matrixcall = MapboxMatrix.builder()
                    .accessToken(MapboxKey)
                    .profile(travelmode)
                    .coordinate(origin)
                    .coordinates(intermsInParts.get(finalI))
                    .sources(0)
                    .destinations(arr)
                    .clientAppName(step_id+ "." +finalI)
                    .build();



            matrixcall.enqueueCall(listener);

           }


    }

    public void setMatrixListener(PointMatrixListener matrixListener) {
        this.matrixListener = matrixListener;
    }

    Callback<MatrixResponse> listener=new Callback<MatrixResponse>() {
        @Override
        public void onResponse(Call<MatrixResponse> call, Response<MatrixResponse> response) {

//            System.out.println("call");
//            System.out.println(new Gson().toJson(call));
//            System.out.println("resp");
//            System.out.println(new Gson().toJson(response));


            String id= response.raw().request().headers().value(0).split(" ")[0];


            int finalI=Integer.parseInt(id.split("\\.")[1]);
            int step_id=Integer.parseInt(id.split("\\.")[0]);




            if (response.isSuccessful()) {

                distanceMatrix = response.body();
                for (int k = 0; k < distanceMatrix.destinations().size(); k++) {


                    long duration = distanceMatrix.durations().get(0)[k].intValue();

                    long arrival_time_millis = dep_time_millis + duration * 1000;

                    final int FinalK = k+1;

                    int curr=step_id+finalI*max_Api_Count+FinalK;

                            /* check for data match*/
                    interms.get(curr).setDs_arr_time(TimeFormatter.getSDFtime(arrival_time_millis,timezoneid));
                    interms.get(curr).setLocation_name(distanceMatrix.destinations().get(k).name());

                    if(matrixListener!=null)
                        matrixListener.OnPointMatrixCalculated(curr);

                }
            } else {

            }
        }

        @Override
        public void onFailure(Call<MatrixResponse> call, Throwable t) {
            if(matrixListener!=null)
                matrixListener.onError("Point Matrix",t.getLocalizedMessage());
        }
    };
}
