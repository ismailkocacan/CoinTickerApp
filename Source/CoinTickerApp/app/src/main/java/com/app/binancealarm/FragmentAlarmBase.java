package com.app.binancealarm;

import androidx.fragment.app.Fragment;
import com.google.android.material.snackbar.Snackbar;

public class FragmentAlarmBase extends Fragment {

    private FragmentDialogAlarmHandler handler;
    private AlarmPriceType alarmPriceType;
    private String symbol;

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public FragmentDialogAlarmHandler getHandler() {
        return handler;
    }

    public void setHandler(FragmentDialogAlarmHandler handler) {
        this.handler = handler;
    }

    public AlarmPriceType getAlarmPriceType() {
        return alarmPriceType;
    }

    public void setAlarmPriceType(AlarmPriceType alarmPriceType) {
        this.alarmPriceType = alarmPriceType;
    }

    public void showSnackMessage(String message){
        Snackbar.make(getView(), message, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }
}