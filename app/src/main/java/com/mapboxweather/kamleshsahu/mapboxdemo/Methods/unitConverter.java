package com.mapboxweather.kamleshsahu.mapboxdemo.Methods;

import java.util.concurrent.TimeUnit;

public class unitConverter {

    public unitConverter() {
        super();
    }

    public String metertoMiles(long distance){
        return String.format("%.2f Miles",distance*0.00062137);
    }
    public String metertoMiles2(double distance){
        return String.format("%.2f mi",distance*0.00062137);
    }

    public String durationBeautify(long duration){

        if(duration>=3600) {
            return String.format("%02d Hours %02d mins", TimeUnit.SECONDS.toHours(duration),
                    TimeUnit.SECONDS.toMinutes(duration) - TimeUnit.HOURS.toMinutes(TimeUnit.SECONDS.toHours(duration)));
        }else
          return String.format("%02d Mins", TimeUnit.SECONDS.toMinutes(duration));


    }


}
