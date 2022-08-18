package com.app.binancealarm;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.HashMap;
import java.util.Map;
import android.os.Build;
import androidx.annotation.RequiresApi;

interface AlarmRuleStrategy {
    boolean execute(AlarmRule alarmRule, TickerBinance ticker);
}

class AlarmRuleStrategyAsk implements AlarmRuleStrategy {
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

class AlarmRuleStrategyBuy implements AlarmRuleStrategy {
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public boolean execute(AlarmRule alarmRule, TickerBinance ticker) {
        boolean result = false;
        if (alarmRule.getAlarmOperator().equals(AlarmOperator.GREATER_THAN))
            result = ticker.getBestAskPrice() > alarmRule.getValueAsDouble();

        if (alarmRule.getAlarmOperator().equals(AlarmOperator.LESS_THEN))
            result = ticker.getBestAskPrice() < alarmRule.getValueAsDouble();

        if (result) App.notify(ticker,alarmRule);
        return result;
    }
}

class AlarmRuleStrategyHighLow implements AlarmRuleStrategy {

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public boolean execute(AlarmRule alarmRule, TickerBinance ticker) {
        boolean result = false;
        try{
            if (alarmRule.getValueAsBool() == null) return result;
            if (!alarmRule.getValueAsBool()) return result;

            String symbolName = ticker.getSymbol();
            Map<String,TickerData> map = WebSocketServiceClient.getInstance().getData();

            if (alarmRule.getAlarmOperator().equals(AlarmOperator.GREATER_THAN)){
                if (map.containsKey(symbolName)){
                    map.get(symbolName).getHigh().enYuksekfiyat = ticker.getHighPrice();
                    if (map.get(symbolName).getHigh().flag){
                        if (map.get(symbolName).getHigh().enYuksekfiyat >
                                map.get(symbolName).getHigh().enYuksek){
                            result = true;
                            App.notifyHighLow(alarmRule,map.get(symbolName).getHigh().enYuksek,
                                    map.get(symbolName).getHigh().enYuksekfiyat);
                            map.get(symbolName).getHigh().enYuksek = map.get(symbolName).getHigh().enYuksekfiyat;
                        }
                    }else{
                        map.get(symbolName).getHigh().enYuksek = map.get(symbolName).getHigh().enYuksekfiyat;
                        map.get(symbolName).getHigh().flag = true;
                    }
                }
            }

            if (alarmRule.getAlarmOperator().equals(AlarmOperator.LESS_THEN)){
                if (map.containsKey(symbolName)){
                    map.get(symbolName).getLow().enDusukFiyat = ticker.getLowPrice();
                    if (map.get(symbolName).getLow().flag){
                        if (map.get(symbolName).getLow().enDusukFiyat <
                                map.get(symbolName).getLow().enDusuk){
                            result = true;
                            App.notifyHighLow(alarmRule,map.get(symbolName).getLow().enDusuk,
                                    map.get(symbolName).getLow().enDusukFiyat);
                            map.get(symbolName).getLow().enDusuk = map.get(symbolName).getLow().enDusukFiyat;
                        }
                    }else{
                        map.get(symbolName).getLow().enDusuk = map.get(symbolName).getLow().enDusukFiyat;
                        map.get(symbolName).getLow().flag = true;
                    }
                }
            }
        }catch (Exception e){
            App.log(e);
        }
        return result;
    }
}

class AlarmRuleStrategyLastOrder implements AlarmRuleStrategy {
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

public class AlarmRuleStrategyExecute implements ServiceResponseSingleBinance{

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