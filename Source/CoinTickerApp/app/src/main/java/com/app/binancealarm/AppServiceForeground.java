package com.app.binancealarm;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import androidx.core.app.NotificationCompat;

public class AppServiceForeground extends Service {
    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try{
            String input = intent.getStringExtra("inputExtra");

            Intent notificationIntent = new Intent(this, NotificationBroadcastReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(
                    this,
                    1,
                    notificationIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT
            );
            Notification notification = new NotificationCompat.Builder(this, AppConst.NOTIFICATION_CHANNEL_ID)
                    .setContentTitle("Binance Alarm Uygulaması")
                    .setContentText("Bu bildirimi gizlemek için tıklayın.\n\r Bildirimleri göster'i kapatın.")
                    .setSmallIcon(R.drawable.ic_access_alarm_24dp)
                    .setContentIntent(pendingIntent)
                    .build();

            startForeground(1, notification);

            //do heavy work on a background thread
            WebSocketServiceClient.getInstance().createNewWebSocket();
        }catch (Exception e){
            App.log(e);
        }
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}