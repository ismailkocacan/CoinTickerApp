package com.app.binancealarm;

import java.util.concurrent.TimeUnit;

public class AppThread extends Thread{

    private boolean doStop = false;

    public synchronized void doStop() {
        this.doStop = true;
    }

    private synchronized boolean keepRunning() {
        return this.doStop == false;
    }

    public AppThread(){
        App.log("Thread new alloc.");
    }

    @Override
    public void run() {
        while (keepRunning()){
            try {
                App.log("Thread is running... (ThreadID:"+Long.toString(this.getId())+")  "+ App.getCurrentTime());
                // Buraya servis çalıştıran kod yazılacak.
                //TimeUnit.SECONDS.sleep(5);
                TimeUnit.MILLISECONDS.sleep(App.getRefreshPeriod());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        App.log("Thread end (ThreadID:"+Long.toString(this.getId())+")");
    }
}