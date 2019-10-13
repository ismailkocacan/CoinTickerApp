package com.app.binancealarm;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class AppFmt {

    public static DecimalFormat DECIMAL_FMT_INT = new DecimalFormat("###,###.00");
    public static DecimalFormat DECIMAL_FMT_DIGIT2 = new DecimalFormat("#0.00");
    public static DecimalFormat DECIMAL_FMT_DIGIT8 = new DecimalFormat("#0.00000000");

    static {
        DecimalFormatSymbols symbols = DECIMAL_FMT_INT.getDecimalFormatSymbols();
        symbols.setGroupingSeparator(',');
        symbols.setDecimalSeparator('.');
        DECIMAL_FMT_INT.setDecimalFormatSymbols(symbols);
    }

    public static String formatMessage(AlarmRule alarmRule){
        String result = "";
        try {
            String symbolName = SymbolNameParser.format(alarmRule.getSymbol());
            String operatorName = alarmRule.getAlarmOperator().getText();
            String priceType = alarmRule.getAlarmPriceType().getText();
            if (alarmRule.getAlarmPriceType().equals(AlarmPriceType.LAST_ORDER)){
                AlarmRuleLastOrder alarmRuleLastOrder = (AlarmRuleLastOrder)alarmRule;
                if (alarmRuleLastOrder.getPriceChange() != null){
                    String change = format(DECIMAL_FMT_DIGIT2,
                                                  alarmRuleLastOrder.getPriceChange(),
                                                  AppConst.SIGN_PERCENT);
                    result = String.format(App.getString(R.string.appConstFormatTitle2),
                            symbolName,priceType,operatorName, change);
                } else {
                    String value = formatPrice(alarmRule);
                    result = String.format(App.getString(R.string.appConstFormatTitle1),
                            symbolName,priceType,operatorName,value);
                }
            } else {
                String value = formatPrice(alarmRule);
                result = String.format(App.getString(R.string.appConstFormatTitle1),
                        symbolName,priceType,operatorName,value);
            }
        }catch (Exception e){
            App.log(e);
        }
        return result;
    }

    public static String format(DecimalFormat df, double value){
        return format(df,value,null);
    }

    public static String format(DecimalFormat df,double value,String sign){
        return (df == null ? Double.toString(value) : df.format(value))+" "+
               (sign == null ? "" : sign);
    }

    public static String formatPrice(AlarmRule alarmRule) {
        String value = "";
        if (alarmRule.getValueAsDouble() == null) return value;
        String tabName = SymbolNameParser.parse(alarmRule.getSymbol());
        DecimalFormat df = !tabName.contains(AppConst.TAB_USD) ? DECIMAL_FMT_DIGIT8 : null;
        value = format(df, alarmRule.getValueAsDouble());
        return value;
    }
}
