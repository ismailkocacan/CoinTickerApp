package com.app.binancealarm;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.TextView;

class BounceInterpolator implements Interpolator {
    private double myAmplitude = 1;
    private double myFrequencey = 10;

    public BounceInterpolator(double amplitude, double frequencey) {
        myAmplitude = amplitude;
        myFrequencey = frequencey;
    }

    @Override
    public float getInterpolation(float time) {
        return (float) (-1 * Math.pow(Math.E, -time / myAmplitude) * Math.cos(myFrequencey * time) + 1);
    }
}

public class ViewUtils {

    private static Context mContext;
    private static BounceInterpolator bounceInterpolator = new BounceInterpolator(0.2,20);

    static {
        mContext = CustomApplication.getInstance().getApplicationContext();
    }

    public static void setStyleRed(TextView view) {
        view.setTextAppearance(mContext, R.style.table_cell_text_red);
        //view.setBackgroundResource(R.drawable.bg_row_red);
    }


    public static void setStyleGreen(TextView view) {
        view.setTextAppearance(mContext, R.style.table_cell_text_green);
       // view.setBackgroundResource(R.drawable.bg_row_green);
    }

    public static Animation setAnimation(final View view) {
        Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.bounce);
        animation.setInterpolator(bounceInterpolator);
        view.clearAnimation();
        view.setAnimation(animation);
        return animation;
    }
}
