package com.app.binancealarm;

public interface ServiceResponseSingleBinance
        extends ServiceResponse<TickerBinance> {
    void onResponse(TickerBinance ticker);
}