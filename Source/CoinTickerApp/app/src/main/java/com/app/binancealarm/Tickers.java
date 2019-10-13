package com.app.binancealarm;

import java.util.ArrayList;

public class Tickers extends ArrayList<TickerBinance> {

    private static final int ARRAY_ELEMENT_DOES_NOT_EXIST = -1;

    public static Tickers create(){
        return new Tickers();
    }

    public void addOrUpdate(TickerBinance ticker){
        int index = this.indexOf(ticker);
        if (index == ARRAY_ELEMENT_DOES_NOT_EXIST){
            this.add(ticker);
            return;
        }
        this.set(index,ticker);
    }
}
