package com.app.binancealarm;

import android.os.Build;

import androidx.annotation.RequiresApi;

public class AlarmRuleStrategyLastOrder implements AlarmRuleStrategy {
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public boolean execute(AlarmRule alarmRule, TickerBinance ticker) {
        boolean result = false;

        AlarmRuleLastOrder alarmRuleLastOrder = (AlarmRuleLastOrder)alarmRule;

        if (alarmRule.getAlarmOperator().equals(AlarmOperator.GREATER_THAN)){
            if (alarmRuleLastOrder.getValueAsDouble() != null)
                result = ticker.getLastPrice() > alarmRuleLastOrder.getValueAsDouble();

            if (alarmRuleLastOrder.getPriceChange() != null)
                result = ticker.getPriceChangePercent() > alarmRuleLastOrder.getPriceChange();
        }

        if (alarmRule.getAlarmOperator().equals(AlarmOperator.LESS_THEN)){
            if (alarmRuleLastOrder.getValueAsDouble() != null)
                result = ticker.getLastPrice() < alarmRuleLastOrder.getValueAsDouble();

            if (alarmRuleLastOrder.getPriceChange() != null)
                result = ticker.getPriceChangePercent() < alarmRuleLastOrder.getPriceChange();
        }

        if (result) App.notify(ticker,alarmRule);
        return result;
    }
}