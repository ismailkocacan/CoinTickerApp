package com.app.binancealarm;

/** Alarm Fiyat Tipi */
public enum AlarmPriceType{
    LAST_ORDER,  // Son Fiyat
    BUY,         // Alış Fiyatı
    ASK,         // Satış Fiyatı
    HIGH_LOW;     // Yüksek Düşük


    String getText(){
        int resId = TEXTS_IDs[this.ordinal()];
        return CustomApplication.getInstance().getApplicationContext().getString(resId);
    }

    private static final int[] TEXTS_IDs = new int[] {
            R.string.tabTextLastOrder,
            R.string.tabTextBuy,
            R.string.tabTextAsk,
            R.string.tabTextHighLow};
}