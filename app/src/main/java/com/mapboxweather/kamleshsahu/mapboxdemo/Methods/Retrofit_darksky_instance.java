package com.mapboxweather.kamleshsahu.mapboxdemo.Methods;

import com.mapboxweather.kamleshsahu.mapboxdemo.Interface.ApiInterface;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.mapboxweather.kamleshsahu.mapboxdemo.Constants.DarkSky_BaseURL;

/**
 * Created by k on 3/27/2019.
 */

public class Retrofit_darksky_instance {
    public static ApiInterface apiService;
    public Retrofit_darksky_instance() {
        super();

    }

    public static void createInstance(){
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                //.addInterceptor(loggingInterceptor)
                //.addNetworkInterceptor(networkInterceptor)
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DarkSky_BaseURL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiService = retrofit.create(ApiInterface.class);
    }

    public static ApiInterface getApiServiceInstance() {
        if(apiService==null)
            createInstance();
        return apiService;
    }
}
