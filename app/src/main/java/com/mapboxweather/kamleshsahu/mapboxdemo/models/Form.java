package com.mapboxweather.kamleshsahu.mapboxdemo.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Form implements Parcelable {

    public MLocation start;
    public MLocation dstn;
    public MTime mTime;
    public String avoid;
    public String travelmode;
    public Form(MLocation start,MLocation dstn,MTime mTime,String avoid,String travelmode) {
         this.mTime=mTime;
         this.dstn=dstn;
         this.start=start;
         this.avoid=avoid;
         this.travelmode=travelmode;
    }


    protected Form(Parcel in) {
        start = in.readParcelable(MLocation.class.getClassLoader());
        dstn = in.readParcelable(MLocation.class.getClassLoader());
        mTime = in.readParcelable(MTime.class.getClassLoader());
        avoid = in.readString();
        travelmode = in.readString();
    }

    public static final Creator<Form> CREATOR = new Creator<Form>() {
        @Override
        public Form createFromParcel(Parcel in) {
            return new Form(in);
        }

        @Override
        public Form[] newArray(int size) {
            return new Form[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(start, flags);
        dest.writeParcelable(dstn, flags);
        dest.writeParcelable(mTime, flags);
        dest.writeString(avoid);
        dest.writeString(travelmode);
    }
}
