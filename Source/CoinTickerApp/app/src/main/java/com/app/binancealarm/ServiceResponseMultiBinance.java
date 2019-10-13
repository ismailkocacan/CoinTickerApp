package com.app.binancealarm;

public interface ServiceResponseMultiBinance
       extends ServiceResponse<Tickers> {
    void onResponse(Tickers tickers);
}