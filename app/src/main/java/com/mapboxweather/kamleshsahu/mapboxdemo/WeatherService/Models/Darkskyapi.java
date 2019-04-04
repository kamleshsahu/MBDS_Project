
package com.mapboxweather.kamleshsahu.mapboxdemo.WeatherService.Models;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Darkskyapi implements Serializable
{

    @SerializedName("latitude")
    @Expose
    private Float latitude;
    @SerializedName("longitude")
    @Expose
    private Float longitude;
    @SerializedName("timezone")
    @Expose
    private String timezone;
    @SerializedName("currently")
    @Expose
    private Currently currently;
    @SerializedName("offset")
    @Expose
    private String offset;
    private final static long serialVersionUID = -1669912100435209634L;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Darkskyapi() {
    }

    /**
     * 
     * @param timezone
     * @param currently
     * @param longitude
     * @param latitude
     * @param offset
     */
    public Darkskyapi(Float latitude, Float longitude, String timezone, Currently currently, String offset) {
        super();
        this.latitude = latitude;
        this.longitude = longitude;
        this.timezone = timezone;
        this.currently = currently;
        this.offset = offset;
    }

    public Float getLatitude() {
        return latitude;
    }

    public void setLatitude(Float latitude) {
        this.latitude = latitude;
    }

    public Float getLongitude() {
        return longitude;
    }

    public void setLongitude(Float longitude) {
        this.longitude = longitude;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public Currently getCurrently() {
        return currently;
    }

    public void setCurrently(Currently currently) {
        this.currently = currently;
    }

    public String getOffset() {
        return offset;
    }

    public void setOffset(String offset) {
        this.offset = offset;
    }

}
