package com.app.binancealarm;

import android.view.View;
import android.widget.TextView;
import java.text.DecimalFormat;


public class ViewData {

    public static final String BUY = "buy";
    public static final String ASK = "ask";
    public static final String HIGH = "high";
    public static final String LOW = "low";
    public static final String LAST_ORDER = "lastOrder";
    public static final String AVG = "avg";
    public static final String CHANGE_RATE = "changeRate";
    public static final String VOLUME = "volume";
    public static final String TAG_COIN_TITLE = "tag_cointitle";

    public static void setTextViewText(View coinView,String fieldTag,DecimalFormat df,double value){
       setTextViewText(coinView,fieldTag,df,value,null);
    }

    public static void setTextViewText(View coinView,String fieldTag,DecimalFormat df,double value,String sign){
        TextView textView = coinView.findViewWithTag(fieldTag);
        if (textView == null) return;
        String fmtValue = AppFmt.format(df,value,sign);
        textView.setText(fmtValue);
    }

    public static void setData(View coinView, TickerBinance ticker,String tabSymbolName){
        TextView textViewTitle = coinView.findViewWithTag(TAG_COIN_TITLE);
        if (textViewTitle != null){
            textViewTitle.setText(SymbolNameParser.format(ticker));
        }
        String tabName = SymbolNameParser.parse(ticker);

        DecimalFormat df = !tabName.contains(AppConst.TAB_USD)
                            ? AppFmt.DECIMAL_FMT_DIGIT8 : null;

        setTextViewText(coinView,BUY,df,ticker.getBestBidPrice()); // Alış
        setTextViewText(coinView,ASK,df,ticker.getBestAskPrice()); // Satış
        setTextViewText(coinView,HIGH,df,ticker.getHighPrice());
        setTextViewText(coinView,LOW,df,ticker.getLowPrice());
        setTextViewText(coinView,LAST_ORDER,df,ticker.getLastPrice());
        setTextViewText(coinView,AVG,df,ticker.getWeightedAveragePrice());
        setTextViewText(coinView,VOLUME, AppFmt.DECIMAL_FMT_INT,ticker.getTotalTradedQuoteAssetVolume());
        setTextViewText(coinView,CHANGE_RATE, AppFmt.DECIMAL_FMT_DIGIT2,
                                             ticker.getPriceChangePercent(),
                                             AppConst.SIGN_PERCENT);

        if (ticker.getPriceChangePercent() > 0.0){
            TextView textView = coinView.findViewWithTag(CHANGE_RATE);
            ViewUtils.setStyleGreen(textView);
        }
        if (ticker.getPriceChangePercent() < 0.0){
            TextView textView = coinView.findViewWithTag(CHANGE_RATE);
            ViewUtils.setStyleRed(textView);
        }
    }
}
