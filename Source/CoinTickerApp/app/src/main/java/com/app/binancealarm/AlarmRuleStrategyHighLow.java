package com.app.binancealarm;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.HashMap;
import java.util.Map;

public class AlarmRuleStrategyHighLow implements AlarmRuleStrategy {

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
