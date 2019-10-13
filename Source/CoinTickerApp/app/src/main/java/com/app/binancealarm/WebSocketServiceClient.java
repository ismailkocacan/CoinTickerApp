package com.app.binancealarm;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;

public class WebSocketServiceClient {
    private Gson gson = new Gson();
    private Tickers tickers = Tickers.create();
    private Type typeToken = new TypeToken<Tickers>(){}.getType();

    private static final String URL_WS = "wss://stream.binance.com:9443/ws/!ticker@arr";

    private static WebSocketServiceClient mInstance;

    private List<ServiceResponseMultiBinance> mResponseMultiList = new ArrayList<>();
    private List<ServiceResponseSingleBinance> mResponseSingleList = new ArrayList<>();


    private TickerMap map = new TickerMap();

    private AlarmRuleStrategyExecute alarmRuleStrategyExecute = new AlarmRuleStrategyExecute();

    private WebSocketServiceClient(){

    }

    public synchronized TickerMap getData() {
        return map;
    }

    public synchronized void addResponseHandler(ServiceResponseMultiBinance serviceResponse){
        mResponseMultiList.add(serviceResponse);
    }

    public synchronized void removeResponseHandler(ServiceResponseMultiBinance serviceResponse){
        mResponseMultiList.remove(serviceResponse);
    }

    public synchronized void addResponseHandler(ServiceResponseSingleBinance serviceResponse){
        mResponseSingleList.add(serviceResponse);
    }

    public synchronized void removeResponseHandler(ServiceResponseSingleBinance serviceResponse){
        mResponseSingleList.remove(serviceResponse);
    }

    public synchronized List<ServiceResponseMultiBinance> getResponseList() {
        return mResponseMultiList;
    }

    public List<TickerBinance> getTickers() {
        return tickers;
    }

    public synchronized void setTickersFromJson(String json) {
        try {
            if (json == null) return;
            if (json.isEmpty()) return;
            tickers.clear();
            tickers  = gson.fromJson(json, typeToken);
            for (int i=0; i < tickers.size(); i++){
                TickerBinance ticker = tickers.get(i);
                if (ticker == null) continue;
                if (ticker.getSymbol() == null) continue;
                String symbol = ticker.getSymbol();
                if (symbol.isEmpty()) continue;
                updateMap(ticker, symbol);
                alarmRuleStrategyExecute.onResponse(ticker);
                postResponseSingleList(ticker);
            }
            postResponseMultiList();
        }catch (Exception e){
            App.log(e);
        }
    }

    private void updateMap(TickerBinance ticker, String symbol) {
        if (map.containsKey(symbol)){
            map.get(symbol).setTicker(ticker);
        } else {
            TickerData tickerData = new TickerData(ticker);
            map.put(symbol,tickerData);
        }
    }

    private void postResponseSingleList(TickerBinance ticker) {
        if (mResponseSingleList.size() > 0){
            for (int j=0; j < mResponseSingleList.size(); j++){
                ServiceResponseSingleBinance response = mResponseSingleList.get(j);
                if (response == null) continue;
                response.onResponse(ticker);
            }
        }
    }

    private void postResponseMultiList() {
        if (mResponseMultiList.size() > 0){
            for (int i=0; i < mResponseMultiList.size(); i++){
                ServiceResponseMultiBinance response =  mResponseMultiList.get(i);
                if (response == null) continue;
                if (tickers.size() > 0)
                    response.onResponse(tickers);
            }
        }
    }

    public void createNewWebSocket(){
        Request request = new Request.Builder().url(URL_WS).build();
        WebSocketListenerCustom listener = new WebSocketListenerCustom();
        OkHttpClient client = new OkHttpClient();
        WebSocket ws = client.newWebSocket(request, listener);
        client.dispatcher().executorService().shutdown();
    }

    public static synchronized WebSocketServiceClient getInstance(){
        if (mInstance == null)
            mInstance = new WebSocketServiceClient();
        return mInstance;
    }
}
