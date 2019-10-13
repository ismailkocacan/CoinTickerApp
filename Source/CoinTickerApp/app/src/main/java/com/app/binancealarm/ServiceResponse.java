package com.app.binancealarm;

public interface ServiceResponse<T> {
    void onResponse(T tickers);
}