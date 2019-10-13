package com.app.binancealarm;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import java.text.DecimalFormat;

public class TickerBinance {

    @SerializedName("e")
    private String eventType;

    @SerializedName("E")
    private String eventTime;

    /** Koin kodu.(Symbol) */
    @SerializedName("s")
    private String symbol;


    @SerializedName("p")
    private double priceChange;

    /** Fark %(Price change percent) */
    @UIField(fieldName="changeRate")
    @SerializedName("P")
    private double priceChangePercent; // +


    /** Ort.Fiyat(Weighted average price) */
    @UIField(fieldName="avg")
    @SerializedName("w")
    private double weightedAveragePrice; // +

    @SerializedName("x")
    private double firstTradePrice;

    @UIField(fieldName="lastOrder")
    /** Son Fiyat(Last price)*/
    @SerializedName("c")
    private double lastPrice; // +

    @SerializedName("Q")
    private double lastQuantity;

    /** Alış Fiyat(Bid) */
    @UIField(fieldName="buy")
    @SerializedName("b")
    private double bestBidPrice; // +

    @SerializedName("B")
    private double bestBidQuantity;

    /** Satış Fiyat(Ask) */
    @UIField(fieldName="ask")
    @SerializedName("a")
    private double bestAskPrice; // +

    @SerializedName("A")
    private double bestAskQuantity;

    @SerializedName("o")
    private double openPrice;

    /** En Yüksek Fiyat(High price) */
    @UIField(fieldName="high")
    @SerializedName("h")
    private double highPrice; // +

    /** En Düşük Fiyat(Low price) */
    @UIField(fieldName="low")
    @SerializedName("l")
    private double lowPrice; // +

    @UIField(fieldName="volume")
    @SerializedName("v")
    private double totalTradedBaseAssetVolume;

    @SerializedName("q")
    private double totalTradedQuoteAssetVolume;

    @SerializedName("O")
    private String statisticsOpenTime;

    @SerializedName("C")
    private String statisticsCloseTime;

    @SerializedName("F")
    private String firstTradeID;

    @SerializedName("L")
    private String lastTradeId;

    @SerializedName("n")
    private String totalNumberOfTrades;

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getEventTime() {
        return eventTime;
    }

    public void setEventTime(String eventTime) {
        this.eventTime = eventTime;
    }

    public String getSymbol() {
        return symbol == null ? "" : symbol.trim();
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public double getPriceChange() {
        return priceChange;
    }

    public void setPriceChange(double priceChange) {
        this.priceChange = priceChange;
    }

    public double getPriceChangePercent() {
        return priceChangePercent;
    }

    public void setPriceChangePercent(double priceChangePercent) {
        this.priceChangePercent = priceChangePercent;
    }

    public double getWeightedAveragePrice() {
        return weightedAveragePrice;
    }

    public void setWeightedAveragePrice(double weightedAveragePrice) {
        this.weightedAveragePrice = weightedAveragePrice;
    }

    public double getFirstTradePrice() {
        return firstTradePrice;
    }

    public void setFirstTradePrice(double firstTradePrice) {
        this.firstTradePrice = firstTradePrice;
    }

    public double getLastPrice() {
        return lastPrice;
    }

    public void setLastPrice(double lastPrice) {
        this.lastPrice = lastPrice;
    }

    public double getLastQuantity() {
        return lastQuantity;
    }

    public void setLastQuantity(double lastQuantity) {
        this.lastQuantity = lastQuantity;
    }

    public double getBestBidPrice() {
        return bestBidPrice;
    }

    public void setBestBidPrice(double bestBidPrice) {
        this.bestBidPrice = bestBidPrice;
    }

    public double getBestBidQuantity() {
        return bestBidQuantity;
    }

    public void setBestBidQuantity(double bestBidQuantity) {
        this.bestBidQuantity = bestBidQuantity;
    }

    public double getBestAskPrice() {
        return bestAskPrice;
    }

    public void setBestAskPrice(double bestAskPrice) {
        this.bestAskPrice = bestAskPrice;
    }

    public double getBestAskQuantity() {
        return bestAskQuantity;
    }

    public void setBestAskQuantity(double bestAskQuantity) {
        this.bestAskQuantity = bestAskQuantity;
    }

    public double getOpenPrice() {
        return openPrice;
    }

    public void setOpenPrice(double openPrice) {
        this.openPrice = openPrice;
    }

    public double getHighPrice() {
        return highPrice;
    }

    public void setHighPrice(double highPrice) {
        this.highPrice = highPrice;
    }

    public double getLowPrice() {
        return lowPrice;
    }

    public void setLowPrice(double lowPrice) {
        this.lowPrice = lowPrice;
    }

    public double getTotalTradedBaseAssetVolume() {
        return totalTradedBaseAssetVolume;
    }

    public void setTotalTradedBaseAssetVolume(double totalTradedBaseAssetVolume) {
        this.totalTradedBaseAssetVolume = totalTradedBaseAssetVolume;
    }

    public double getTotalTradedQuoteAssetVolume() {
        return totalTradedQuoteAssetVolume;
    }

    public void setTotalTradedQuoteAssetVolume(double totalTradedQuoteAssetVolume) {
        this.totalTradedQuoteAssetVolume = totalTradedQuoteAssetVolume;
    }

    public String getStatisticsOpenTime() {
        return statisticsOpenTime;
    }

    public void setStatisticsOpenTime(String statisticsOpenTime) {
        this.statisticsOpenTime = statisticsOpenTime;
    }

    public String getStatisticsCloseTime() {
        return statisticsCloseTime;
    }

    public void setStatisticsCloseTime(String statisticsCloseTime) {
        this.statisticsCloseTime = statisticsCloseTime;
    }

    public String getFirstTradeID() {
        return firstTradeID;
    }

    public void setFirstTradeID(String firstTradeID) {
        this.firstTradeID = firstTradeID;
    }

    public String getLastTradeId() {
        return lastTradeId;
    }

    public void setLastTradeId(String lastTradeId) {
        this.lastTradeId = lastTradeId;
    }

    public String getTotalNumberOfTrades() {
        return totalNumberOfTrades;
    }

    public void setTotalNumberOfTrades(String totalNumberOfTrades) {
        this.totalNumberOfTrades = totalNumberOfTrades;
    }


    @NonNull
    @Override
    public String toString() {
        String result = "";
        String tabName = SymbolNameParser.parse(this);
        DecimalFormat df = !tabName.contains(AppConst.TAB_USD) ? AppFmt.DECIMAL_FMT_DIGIT8 : null;

        result += App.getString(R.string.txtRowBuy)+": " + AppFmt.format(df,bestBidPrice) + "\n\r";
        result += App.getString(R.string.txtRowAsk)+": " + AppFmt.format(df,bestAskPrice) + "\n\r";
        result += App.getString(R.string.txtRowHigh)+": " + AppFmt.format(df,highPrice) + "\n\r";
        result += App.getString(R.string.txtRowLow)+": " + AppFmt.format(df,lowPrice) + "\n\r";
        result += App.getString(R.string.txtRowLastOrder)+" : " + AppFmt.format(df,lastPrice) + "\n\r";
        result += App.getString(R.string.txtRowAvg)+": " + AppFmt.format(df,weightedAveragePrice) + "\n\r";
        result += App.getString(R.string.txtRowVolume)+": " + AppFmt.format(AppFmt.DECIMAL_FMT_INT,totalTradedQuoteAssetVolume) + "\n\r";
        result += App.getString(R.string.txtRowChange)+":  "+ AppFmt.format(AppFmt.DECIMAL_FMT_DIGIT2,priceChangePercent,AppConst.SIGN_PERCENT) + "\n\r";
        return result;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        return this.getSymbol().toLowerCase().equals(((TickerBinance)obj).getSymbol().toLowerCase());
    }
}