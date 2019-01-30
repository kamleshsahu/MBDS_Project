package com.mapboxweather.kamleshsahu.mapboxdemo.Models;

public class Wdata{
    String imgurl;
    String wtime;
    String wx;
    String temp;

    public Wdata(String wtime,String wx,String temp,String imgurl) {
    this.imgurl=imgurl;
    this.temp=temp;
    this.wtime=wtime;
    this.wx=wx;
    }

    public Wdata() {
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
