package com.app.binancealarm;

public interface AlarmRuleStrategy {
    boolean execute(AlarmRule alarmRule, TickerBinance ticker);
}