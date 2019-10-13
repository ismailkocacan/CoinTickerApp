package com.app.binancealarm;

import java.util.HashMap;
import java.util.Map;

public class AlarmRuleStrategyExecute
       implements ServiceResponseSingleBinance{

    private Map<AlarmPriceType, AlarmRuleStrategy> map = new HashMap<>();

    public AlarmRuleStrategyExecute(){
        map.put(AlarmPriceType.LAST_ORDER,new AlarmRuleStrategyLastOrder());
        map.put(AlarmPriceType.BUY,new AlarmRuleStrategyBuy());
        map.put(AlarmPriceType.ASK,new AlarmRuleStrategyAsk());
        map.put(AlarmPriceType.HIGH_LOW,new AlarmRuleStrategyHighLow());
    }

    private AlarmRuleStrategy getStrategy(AlarmPriceType alarmPriceType){
        return map.get(alarmPriceType);
    }

    @Override
    public synchronized void onResponse(TickerBinance ticker) {
        if (ticker == null) return;
        AlarmRuleMap map = App.getAlarmList();
        if (map == null) return;
        AlarmRules alarmRules = map.get(ticker.getSymbol());
        if (alarmRules == null) return;
        for (AlarmRule alarmRule: alarmRules){
            if (alarmRule == null) continue;
            AlarmRuleStrategy strategy = getStrategy(alarmRule.getAlarmPriceType());
            if (strategy == null) continue;
            boolean result = strategy.execute(alarmRule,ticker);
            if (!result) continue;
            if (!alarmRule.getAlarmPriceType().equals(AlarmPriceType.HIGH_LOW)){
                map.get(ticker.getSymbol()).remove(alarmRule);
                App.getDbSetting().deleteById(alarmRule.getId());
            }
        }
    }
}