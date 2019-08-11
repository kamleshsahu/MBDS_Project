package com.mapboxweather.kamleshsahu.mapboxdemo.ViewModels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.mapboxweather.kamleshsahu.mapboxdemo.MLocation;
import com.mapboxweather.kamleshsahu.mapboxdemo.MTime;

public class MainActivityViewModel extends AndroidViewModel {
      MutableLiveData<MTime> mTimeMutableLiveData=new MutableLiveData<>();
      MutableLiveData<MLocation> startLiveData=new MutableLiveData<>();
      MutableLiveData<MLocation> dstnLiveData=new MutableLiveData<>();

      MTime mTime;


    public MainActivityViewModel(@NonNull Application application) {
        super(application);
         mTime=new MTime();
         updatemTimeMutableLiveData();
    }



    public void updatemTimeMutableLiveData() {
        mTimeMutableLiveData.setValue(mTime);
    }

    public MutableLiveData<MTime> getmTimeMutableLiveData() {
        return mTimeMutableLiveData;
    }


    public MutableLiveData<MLocation> getDstnLiveData() {
        return dstnLiveData;
    }


    public MutableLiveData<MLocation> getStartLiveData() {
        return startLiveData;
    }

    public void setStart(MLocation start) {
        startLiveData.setValue(start);
    }

    public void setDstn(MLocation dstn) {
        dstnLiveData.setValue(dstn);
    }

    public MTime getmTime() {
        return mTime;
    }


}
