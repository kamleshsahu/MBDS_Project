package com.mapboxweather.kamleshsahu.mapboxdemo;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.Parcel;
import android.os.Parcelable;

import com.mapbox.geojson.Point;

public class MLocation extends BaseObservable implements Parcelable {


    private String name;
    private Point point;

    String s_point;

    public MLocation(String name, Point point) {
        this.name = name;
        this.point = point;
        this.s_point=point.toJson();
    }

    public MLocation() {
    }


    protected MLocation(Parcel in) {
        name = in.readString();
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

    @Bindable
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Point getPoint() {
        return point;
    }

    public void setPoint(Point point) {
        this.point = point;
    }


    public Point getS_point() {
       return Point.fromJson(s_point);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(s_point);
    }
}
