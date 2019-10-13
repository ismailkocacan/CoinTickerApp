package com.app.binancealarm;

import java.util.Comparator;

/** Symbo ve Volume değerlerine göre sıralama */

public class SorterTickerSymbolAndVolume implements Comparator<TickerBinance> {

    private static SorterTickerSymbolAndVolume mInstance;

    @Override
    public int compare(TickerBinance left, TickerBinance right) {
        int result = left.getSymbol().compareTo(right.getSymbol());
        if (result != 0 ) return result;

        Double valueLeft = left.getTotalTradedQuoteAssetVolume();
        Double valueRight = right.getTotalTradedQuoteAssetVolume();
        return valueLeft.compareTo(valueRight);
    }

    private SorterTickerSymbolAndVolume(){

    }

    public static SorterTickerSymbolAndVolume getInstance() {
        if (mInstance == null) {
            mInstance = new SorterTickerSymbolAndVolume();
        }
        return mInstance;
    }
}
