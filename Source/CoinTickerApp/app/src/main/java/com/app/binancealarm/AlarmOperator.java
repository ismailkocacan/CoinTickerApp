package com.app.binancealarm;

/** Alarm Karşılaştırma Operatörleri */
public enum AlarmOperator {
    GREATER_THAN,
    LESS_THEN;

    int getImageResourceId(){
        if (this == GREATER_THAN)
            return R.drawable.icon_alarmrule_green48;
        return R.drawable.icon_alarmrule_red48;
    }


    String getText(){
        return OPERATOR_TEXTS[this.ordinal()];
    }

    public static final String[] OPERATOR_TEXTS = new String[] {
            App.getString(R.string.operatorGreaterThen),
            App.getString(R.string.operatorLessThen)
    };
}