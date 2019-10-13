package com.app.binancealarm;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class WebService {

    private static RequestQueue mRequestQueue;

    public static void sendRequest(Request request)
    {
        if (mRequestQueue == null) {
            Context context = CustomApplication.getInstance().getApplicationContext();
            mRequestQueue = Volley.newRequestQueue(context);
            mRequestQueue.start();
        }
        mRequestQueue.add(request);
    }
}
