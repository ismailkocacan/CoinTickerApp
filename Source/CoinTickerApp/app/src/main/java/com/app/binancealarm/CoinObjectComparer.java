package com.app.binancealarm;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
/**
 * Ticker sınıfı içindeki verileri karşılaştırarak,
 * değişen verilerin hangi alanlarda olduğunu bulmayı sağlar.
 * */
public class CoinObjectComparer{

    private List<String> changedFields = new ArrayList<>();

    public boolean compare(){
        changedFields.clear();
        /// to do
        boolean hasChangedFields = hasChangedFields();
        if (hasChangedFields){
            AlarmSound.play();
        }
        return hasChangedFields;
    }

    public List<String> getChangedFields() {
        return changedFields;
    }

    public boolean hasChangedFields(){
        return changedFields.size() > 0;
    }
}
