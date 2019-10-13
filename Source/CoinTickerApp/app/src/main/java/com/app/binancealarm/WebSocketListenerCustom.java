package com.app.binancealarm;

import android.os.Build;
import android.os.Handler;

import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

public class WebSocketListenerCustom extends WebSocketListener {

    private Handler handler = new Handler();

    @Override
    public void onOpen(WebSocket webSocket, Response response) {
        super.onOpen(webSocket, response);
        App.log("onOpen");
    }

    @Override
    public void onMessage(WebSocket webSocket,String text) {
        super.onMessage(webSocket, text);
        //App.log("onMessage");
        WebSocketServiceClient.getInstance().
                setTickersFromJson(text);
    }

    @Override
    public void onFailure(WebSocket webSocket, Throwable t, final Response response) {
        super.onFailure(webSocket, t, response);
        handler.post(new Runnable() {
            @Override
            public void run() {
                WebSocketServiceClient.getInstance().createNewWebSocket();
            }
        });
    }

    @Override
    public void onClosing(WebSocket webSocket, int code, final String reason) {
        super.onClosing(webSocket, code, reason);
        App.log("onClosing");
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    App.notification("WebSocket onClosing:",reason);
                }
            }
        });
    }

    @Override
    public void onClosed(WebSocket webSocket, int code, final String reason) {
        super.onClosed(webSocket, code, reason);
        App.log("onClosing");
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    App.notification("WebSocket onClosing:",reason);
                }
            }
        });
    }
}
