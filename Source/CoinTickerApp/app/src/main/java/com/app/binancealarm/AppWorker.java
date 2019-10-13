package com.app.binancealarm;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import java.util.concurrent.TimeUnit;

public class AppWorker extends Worker {

    AppThread appThread;
    public AppWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        App.log("AppWorker new instance ("+getId().toString()+")");
    }

    @NonNull
    @Override
    public Result doWork() {
        App.log("doWork ("+getId().toString()+") "+ App.getCurrentTime());
        try{
            // buraya servis çalıştıran kod
            appThread = new AppThread();
            appThread.start();
        }catch (Exception e){
            App.log(e);
        }
        return Result.retry();
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        App.log("AppWorker finalize ("+getId().toString()+")");
        if (appThread != null){
            appThread.doStop();
        }
    }
}