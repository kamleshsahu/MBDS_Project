package com.mapboxweather.kamleshsahu.mapboxdemo.Methods;

/**
 * Created by k on 3/27/2019.
 */

public class myStopwatch {

   static long starttime=0,endtime=0;
    public myStopwatch() {

    }
    static void startTimer(){
        starttime=System.currentTimeMillis();
    }

    static void stopTimer(){
        endtime=System.currentTimeMillis();
        System.out.printf("runtime: %d\n",endtime-starttime);
    }
}
