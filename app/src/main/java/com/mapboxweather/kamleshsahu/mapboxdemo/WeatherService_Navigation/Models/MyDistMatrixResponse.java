package com.mapboxweather.kamleshsahu.mapboxdemo.WeatherService_Navigation.Models;

import com.mapbox.api.matrix.v1.models.MatrixResponse;

import retrofit2.Response;

public class MyDistMatrixResponse {

    int listpos;
    Response<MatrixResponse> response;

    public MyDistMatrixResponse(int listpos,Response<MatrixResponse> resp) {
        this.response=resp;
        this.listpos=listpos;
    }

    public int getListpos() {
        return listpos;
    }

    public Response<MatrixResponse> getResponse() {
        return response;
    }
}
