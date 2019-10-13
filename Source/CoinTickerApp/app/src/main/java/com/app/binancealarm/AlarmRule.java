package com.app.binancealarm;

/**
 * Alarm Kuralı verisini temsil eden sınıftır.
 *
 */
public class AlarmRule {
    private int id;
    private String symbol;
    private AlarmOperator alarmOperator;
    private AlarmPriceType alarmPriceType;

    private Object value;

    public AlarmRule(){

    }

    public void setValues(Setting setting){
        setId(setting.getId());
        setSymbol(setting.getKeyName().trim());
        setAlarmOperator(AlarmOperator.valueOf(setting.getValue1().trim()));
        if (setting.getValue2().trim().length() > 0)
            setAlarmPriceType(AlarmPriceType.valueOf(setting.getValue2().trim()));
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public AlarmOperator getAlarmOperator() {
        return alarmOperator;
    }

    public void setAlarmOperator(AlarmOperator alarmOperator) {
        this.alarmOperator = alarmOperator;
    }

    public AlarmPriceType getAlarmPriceType() {
        return alarmPriceType;
    }

    public void setAlarmPriceType(AlarmPriceType alarmPriceType) {
        this.alarmPriceType = alarmPriceType;
    }

    public void setValueAsBool(Boolean value){
        this.value = value;
    }

    public Boolean getValueAsBool(){
        return (Boolean) this.value;
    }

    public void setValueAsInteger(Integer valudbe){
        this.value = value;
    }

    public Integer getValueAsInteger(){
        return (Integer)this.value;
    }

    public void setValueAsDouble(Double value){
        this.value = value;
    }

    public Double getValueAsDouble(){
        return (Double) this.value;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        String result = "";
        result += "CoinType:"+symbol+"\n";
        result += "AlarmOperator:"+ alarmOperator.name()+"\n";
        result += "AlarmPriceType:"+alarmPriceType.name()+"\n";
        if (alarmPriceType.equals(AlarmPriceType.HIGH_LOW)){
            result += "Value:"+getValueAsBool().toString();
        } else {
            result += "Value:"+getValueAsDouble().toString();
        }
        return result;
    }
}

