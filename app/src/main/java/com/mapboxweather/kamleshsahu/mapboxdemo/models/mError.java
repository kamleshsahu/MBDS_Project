package com.mapboxweather.kamleshsahu.mapboxdemo.models;

public class mError{
    String heading;
    String message;

    public mError(String heading,String message) {
        this.heading=heading;
        this.message=message;
    }

    public String getHeading() {
        return heading;
    }

    public String getMessage() {
        return message;
    }
}
