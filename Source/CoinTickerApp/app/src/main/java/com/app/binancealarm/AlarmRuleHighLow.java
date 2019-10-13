package com.app.binancealarm;

class AlarmRuleHighLow extends AlarmRule{

    public AlarmRuleHighLow(){

    }
    public AlarmRuleHighLow(Setting setting){
        try{
            setValues(setting);
            if (setting.getValue3().trim().length() > 0)
                setValueAsBool(Boolean.parseBoolean(setting.getValue3().trim().trim()));
        }catch (Exception e){
            App.log(e);
        }
    }
}