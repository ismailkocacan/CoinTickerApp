package com.app.binancealarm;

class AlarmRuleBuy extends AlarmRule{

    public AlarmRuleBuy(){

    }

    public AlarmRuleBuy(Setting setting){
        try{
            setValues(setting);
            if (setting.getValue3().trim().length() > 0)
                setValueAsDouble(Double.parseDouble(setting.getValue3().trim()));
        }catch (Exception e){
            App.log(e);
        }
    }
}

