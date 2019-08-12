package com.mapboxweather.kamleshsahu.mapboxdemo.Views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.mapboxweather.kamleshsahu.mapboxdemo.R;

public class formLayout extends RelativeLayout {


    public formLayout(Context context) {
        this(context,null);

    }

    public formLayout(Context context, AttributeSet attrs) {
        this(context, attrs,-1);
    }

    public formLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflate(getContext(),R.layout.form_layout,this);
    }

}
