package com.app.binancealarm;

/** Son fiyat için alarm kuralı verisini tutan sınıftır.
 *  Sadece son fiyat'da priceChange değeri olduğu için bu şekilde
 *  alt sınıf olarak yazılmıştır.
 * */
class  AlarmRuleLastOrder extends  AlarmRule{
    private Double priceChange;

    public AlarmRuleLastOrder(){

    }

    public AlarmRuleLastOrder(Setting setting){
        try{
            setValues(setting);
            if (setting.getValue3() != null){
                if (setting.getValue3().length() > 0)
                    setValueAsDouble(Double.parseDouble(setting.getValue3()));
            }
            if (setting.getValue4() != null){
                if (setting.getValue4().length() > 0)
                    setPriceChange(Double.parseDouble(setting.getValue4()));
            }
        }catch (Exception e){
            App.log(e);
        }
    }

    public Double getPriceChange() {
        return priceChange;
    }

    public void setPriceChange(Double priceChange) {
        this.priceChange = priceChange;
    }
}