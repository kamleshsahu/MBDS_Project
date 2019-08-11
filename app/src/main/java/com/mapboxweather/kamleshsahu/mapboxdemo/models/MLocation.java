package com.mapboxweather.kamleshsahu.mapboxdemo.models;

import android.databinding.BaseObservable;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;
import com.mapbox.geojson.Point;

public class MLocation extends BaseObservable implements Parcelable {


    public String name;
    public Point point;
    public boolean isCurrentLocation;

    String s_point;

    public MLocation(String name, Point point) {
        this.name = name;
        this.point = point;
        this.s_point=point.toJson();
    }
    public MLocation(String name, Point point,boolean isCurrentLocation) {
        this.name = name;
        this.point = point;
        this.s_point=point.toJson();
        this.isCurrentLocation=isCurrentLocation;
    }
    public MLocation() {
    }


    protected MLocation(Parcel in) {
        name = in.readString();
        isCurrentLocation = in.readByte() != 0;
        s_point = in.readString();
    }

    public static final Creator<MLocation> CREATOR = new Creator<MLocation>() {
        @Override
        public MLocation createFromParcel(Parcel in) {
            return new MLocation(in);
        }

        @Override
        public MLocation[] newArray(int size) {
            return new MLocation[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeByte((byte) (isCurrentLocation ? 1 : 0));
        dest.writeString(s_point);
    }

    public Point getPoint() {
        return point;
    }



    public String getName() {
        return name;
    }

    public void setPoint(Point point) {
        this.point = point;
    }

    public void setName(String name) {
        this.name = name;
    }

   public Point getS_point(){
        return new Gson().fromJson(s_point,Point.class);
   }

    public void setCurrentLocation(boolean currentLocation) {
        isCurrentLocation = currentLocation;
    }
}
