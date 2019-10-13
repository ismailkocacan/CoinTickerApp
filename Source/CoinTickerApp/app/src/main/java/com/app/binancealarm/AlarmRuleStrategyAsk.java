package com.app.binancealarm;

import android.os.Build;
import androidx.annotation.RequiresApi;

public class AlarmRuleStrategyAsk implements AlarmRuleStrategy {
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public boolean execute(AlarmRule alarmRule, TickerBinance ticker) {
        boolean result = false;
        if (alarmRule.getAlarmOperator().equals(AlarmOperator.GREATER_THAN))
            result = ticker.getBestBidPrice() > alarmRule.getValueAsDouble();

        if (alarmRule.getAlarmOperator().equals(AlarmOperator.LESS_THEN))
            result = ticker.getBestBidPrice() < alarmRule.getValueAsDouble();

        if (result) App.notify(ticker,alarmRule);
        return result;
    }
}