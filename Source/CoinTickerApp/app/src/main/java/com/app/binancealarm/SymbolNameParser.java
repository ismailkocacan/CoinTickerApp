package com.app.binancealarm;

public class SymbolNameParser {

    public static String parse(TickerBinance ticker,String tabSymbolName){
        String result = "";
        String symbol = ticker.getSymbol();
        String symbolParsed3 = symbol.substring(symbol.length()-3);
        String symbolParsed4 = symbol.substring(symbol.length()-4);
        if (tabSymbolName.contains(AppConst.TAB_BNB) &&
            symbolParsed3.contains(AppConst.TAB_BNB)){
            result = AppConst.TAB_BNB;
        } else  if (tabSymbolName.contains(AppConst.TAB_BTC) &&
                    symbolParsed3.contains(AppConst.TAB_BTC)){
            result = AppConst.TAB_BTC;
        } else if (tabSymbolName.contains(AppConst.TAB_ALTS) &&
                   (symbolParsed3.contains(AppConst.TAB_ETH) ||
                   symbolParsed3.contains(AppConst.TAB_TRX) ||
                   symbolParsed3.contains(AppConst.TAB_XRP))){
             result = AppConst.TAB_ALTS;
         }else if (tabSymbolName.contains(AppConst.TAB_USD) &&
                   (symbolParsed3.contains(AppConst.TAB_PAX) ||
                   symbolParsed4.contains(AppConst.TAB_USDT) ||
                   symbolParsed4.contains(AppConst.TAB_TUSD) ||
                   symbolParsed4.contains(AppConst.TAB_USDC) ||
                   symbolParsed4.contains(AppConst.TAB_BUST))){
            result = AppConst.TAB_USD;
        }
        return result;
    }

    public static String parse(TickerBinance ticker){
        return parse(ticker.getSymbol());
    }

    public static String parse(String symbolName){
        String result = "";
        String symbol = symbolName;
        String symbolParsed3 = symbol.substring(symbol.length()-3);
        String symbolParsed4 = symbol.substring(symbol.length()-4);
        if (symbolParsed3.contains(AppConst.TAB_BNB)){
            result = AppConst.TAB_BNB;
        } else  if (symbolParsed3.contains(AppConst.TAB_BTC)){
            result = AppConst.TAB_BTC;
        } else if ((symbolParsed3.contains(AppConst.TAB_ETH) ||
                symbolParsed3.contains(AppConst.TAB_TRX) ||
                symbolParsed3.contains(AppConst.TAB_XRP))){
            result = AppConst.TAB_ALTS;
        }else if ((symbolParsed3.contains(AppConst.TAB_PAX) ||
                symbolParsed4.contains(AppConst.TAB_USDT) ||
                symbolParsed4.contains(AppConst.TAB_TUSD) ||
                symbolParsed4.contains(AppConst.TAB_USDC) ||
                symbolParsed4.contains(AppConst.TAB_BUST))){
            result = AppConst.TAB_USD;
        }
        return result;
    }

    public static String format(String symbolName){
        String result = symbolName;
        try{
            String fmt = "%s/%s";
            String symbol = symbolName;
            String symbolParsed3 = symbol.substring(symbol.length()-3);
            String symbolParsed4 = symbol.substring(symbol.length()-4);
            if (symbolParsed3.contains(AppConst.TAB_BNB)){
                result = String.format(fmt,symbol.replace(symbolParsed3,""),symbolParsed3);
            } else  if (symbolParsed3.contains(AppConst.TAB_BTC)){
                result = String.format(fmt,symbol.replace(symbolParsed3,""),symbolParsed3);
            } else if ((symbolParsed3.contains(AppConst.TAB_ETH) ||
                    symbolParsed3.contains(AppConst.TAB_TRX) ||
                    symbolParsed3.contains(AppConst.TAB_XRP))){
                result = String.format(fmt,symbol.replace(symbolParsed3,""),symbolParsed3);
            } else if (symbolParsed3.contains(AppConst.TAB_PAX)){
                result = String.format(fmt,symbol.replace(symbolParsed3,""),symbolParsed3);
            } else if (symbolParsed4.contains(AppConst.TAB_USDT) ||
                    symbolParsed4.contains(AppConst.TAB_TUSD) ||
                    symbolParsed4.contains(AppConst.TAB_USDC) ||
                    symbolParsed4.contains(AppConst.TAB_BUST)){
                result = String.format(fmt,symbol.replace(symbolParsed4,""),symbolParsed4);
            }
        }catch (Exception e){
            App.log(e);
        }
        return result;
    }

    public static String format(TickerBinance ticker){
       return format(ticker.getSymbol());
    }
}
