package com.app.binancealarm;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class App{
    private static DbSetting dbSetting;
    private static DbUserSetting dbUserSetting;
    private static long refreshPeriod = 10000;
    private static AlarmRuleMap map = new AlarmRuleMap();

    private static Timer timer;
    private static TimerTask timerTask;

    public static AppTableChanged appTableChanged;
    public static AppTableChanged getAppTableChanged() {
        return appTableChanged;
    }

    static {
      try{
          refreshAlarmList();
          appTableChanged = new AppTableChanged() {
              @Override
              public void onChanged(String tableName) {
                  refreshAlarmList();
              }
          };
          App.log("App.static");
      }catch (Exception e){
          App.log(e);
      }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void notify(TickerBinance ticker, AlarmRule alarmRule){
        String title = AppFmt.formatMessage(alarmRule);
        App.log(title+"\n"+ticker.toString());
        App.notification(title,ticker.toString());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void notifyHighLow(AlarmRule alarmRule,double value1,double value2){
        String symbolName = SymbolNameParser.format(alarmRule.getSymbol());
        String title = String.format(App.getString(R.string.appConstFormatHighExceed), symbolName);
        if (alarmRule.getAlarmOperator().equals(AlarmOperator.LESS_THEN)) {
            title = String.format(App.getString(R.string.appConstFormatLowExceed), symbolName);
        }
        String tabName = SymbolNameParser.parse(alarmRule.getSymbol());
        DecimalFormat df = !tabName.contains(AppConst.TAB_USD) ? AppFmt.DECIMAL_FMT_DIGIT8 : null;
        String message = AppFmt.format(df,value1)+" --> "+
                         AppFmt.format(df,value2);
        App.notification(title,message);
    }

    public static String getString(int resourceId){
        return CustomApplication.getInstance().getString(resourceId);
    }

    public static void scheduleTimer(final long refreshPeriod){
        if (timerTask != null){
            timerTask.cancel();
        }
        if (timer != null){
            timer.cancel();
            timer.purge();
        }
        final Handler handler = new Handler();
        timerTask = new TimerTask() {
            @Override
            public void run() {

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        App.log("timer is running ("+Long.toString(refreshPeriod)+")");
                        // buraya servis çalıştıran kod yazılacak
                    }
                });
            }
        };
        timer = new Timer();
        timer.schedule(timerTask,1000L,refreshPeriod);
    }

    public static void refreshAlarmList(){
        map.clear();
        map = getDbSetting().getListAlarm();
    }

    public static AlarmRuleMap getAlarmList(){
        return map;
    }

    public static DbSetting getDbSetting() {
      if (dbSetting == null) {
          Context context = CustomApplication.getInstance().getApplicationContext();
          dbSetting =  new DbSetting(context);
      }
      return dbSetting;
    }

    public static DbUserSetting getDbUserSetting() {
        if (dbUserSetting == null){
            Context context = CustomApplication.getInstance().getApplicationContext();
            dbUserSetting =  new DbUserSetting(context);
        }
        return dbUserSetting;
    }

    public static synchronized long getRefreshPeriod() {
        return refreshPeriod;
    }

    public static synchronized void setRefreshPeriod(long refreshPeriod) {
        App.refreshPeriod = refreshPeriod;
    }

    public static void showMessage(String message){
        Toast.makeText(CustomApplication.getInstance().getApplicationContext(),message,Toast.LENGTH_LONG).show();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void notification(String title,String message){

        Context mContext = CustomApplication.getInstance().getApplicationContext();

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(mContext.getApplicationContext(),
                                               "notify_001");
        Intent intent = new Intent(mContext.getApplicationContext(), MainActivity.class);
        int requestCode = (int) System.currentTimeMillis();
        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, requestCode, intent, 0);

        NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();
        bigText.bigText(message);
        //bigText.setBigContentTitle(title);

        bigText.setSummaryText(App.getString(R.string.appConstNotificationName));
        mBuilder.setAutoCancel(true);
        mBuilder.setContentIntent(pendingIntent);
        mBuilder.setSmallIcon(R.mipmap.ic_launcher_round);
        mBuilder.setContentTitle(title);
        mBuilder.setContentText(message);
        mBuilder.setPriority(Notification.PRIORITY_MAX);
        mBuilder.setStyle(bigText);

        NotificationManager mNotificationManager =  (NotificationManager)
                         mContext.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = AppConst.NOTIFICATION_CHANNEL_ID_BINANCE;
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    App.getString(R.string.appConstNotificationChannelName),
                    NotificationManager.IMPORTANCE_DEFAULT);
            mNotificationManager.createNotificationChannel(channel);
            mBuilder.setChannelId(channelId);
        }
        mNotificationManager.notify(requestCode, mBuilder.build());
    }

    public static void log(Exception e){
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        String sStackTrace = sw.toString(); // stack trace as a string
        Log.e(AppConst.TAG_DEBUG,e.getClass().getName()+":"+e.getMessage()+"\n"+sStackTrace);
    }

    public static void log(String message){
        Log.i(AppConst.TAG_DEBUG,message);
    }


    public static String getCurrentTime(){
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        return formatter.format(date);
    }
}
