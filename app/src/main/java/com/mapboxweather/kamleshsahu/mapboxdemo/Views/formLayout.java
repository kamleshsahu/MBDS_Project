package com.mapboxweather.kamleshsahu.mapboxdemo.Views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mapboxweather.kamleshsahu.mapboxdemo.R;

public class formLayout extends RelativeLayout {


    TextView start;


    public formLayout(Context context) {
        super(context);
        inflate(getContext(), R.layout.form_layout,this);
    }

    public formLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflate(getContext(),R.layout.form_layout,this);
    }

    public formLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflate(getContext(),R.layout.form_layout,this);

        start=findViewById(R.id.start_address);


    }




}
