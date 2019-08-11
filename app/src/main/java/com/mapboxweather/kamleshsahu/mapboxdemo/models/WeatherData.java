package com.mapboxweather.kamleshsahu.mapboxdemo.models;

public class WeatherData {
    String imgurl;
    String wtime;
    String wx;
    String temp;

    public WeatherData(String wtime, String wx, String temp, String imgurl) {
    this.imgurl=imgurl;
    this.temp=temp;
    this.wtime=wtime;
    this.wx=wx;
    }

    public WeatherData() {
        super();
    }

    public String getWtime() {
        return wtime;
    }

    public String getImgurl() {
        return imgurl;
    }

    public String getTemp() {
        return temp;
    }

    public String getWx() {
        return wx;
    }
}
