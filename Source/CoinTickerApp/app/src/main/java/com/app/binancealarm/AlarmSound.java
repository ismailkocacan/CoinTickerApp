package com.app.binancealarm;

import android.media.MediaPlayer;

public class AlarmSound {

    private static MediaPlayer mediaPlayer;

    static {
        mediaPlayer = MediaPlayer.create(CustomApplication.getInstance().
                      getApplicationContext(),R.raw.sound);
    }

    public static void play(){
        mediaPlayer.start();
    }
}
