package com.app.binancealarm;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FragmentAlarmHighLow extends FragmentAlarmBase {

    CheckBox cBoxHighExceed;
    CheckBox cBoxLowExceed;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setAlarmPriceType(AlarmPriceType.HIGH_LOW);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_alarm_highlow, container, false);
        return root;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        cBoxHighExceed = getView().findViewById(R.id.cBoxHighExceed);
        cBoxLowExceed =  getView().findViewById(R.id.cBoxLowExceed);

        if (getSymbol() != null) {
            List<AlarmRule> list =  App.getDbSetting().
                    getListAlarmRule(getSymbol(),getAlarmPriceType());
            for (AlarmRule alarmRule:list){
                if (alarmRule.getAlarmOperator().equals(AlarmOperator.GREATER_THAN)){
                    cBoxHighExceed.setChecked(alarmRule.getValueAsBool());
                }
                if (alarmRule.getAlarmOperator().equals(AlarmOperator.LESS_THEN)){
                    cBoxLowExceed.setChecked(alarmRule.getValueAsBool());
                }
            }
        }

        cBoxHighExceed.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean value) {
                Setting setting = new Setting();
                setting.setKeyName(getSymbol());
                setting.setValue1(AlarmOperator.GREATER_THAN.name());
                setting.setValue2(AlarmPriceType.HIGH_LOW.name());
                setting.setValue3(Boolean.toString(value));

                Map<String,String> whereArgs = new HashMap<>();
                whereArgs.put(DbSetting.KEY_NAME,setting.getKeyName());
                whereArgs.put(DbSetting.VALUE1,setting.getValue1());
                whereArgs.put(DbSetting.VALUE2,setting.getValue2());
                Boolean result = App.getDbSetting().post(setting,whereArgs);
                if (!result){
                    App.showMessage("Değerler güncellemedi");
                }
            }
        });

        cBoxLowExceed.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean value) {
                Setting setting = new Setting();
                setting.setKeyName(getSymbol());
                setting.setValue1(AlarmOperator.LESS_THEN.name());
                setting.setValue2(AlarmPriceType.HIGH_LOW.name());
                setting.setValue3(Boolean.toString(value));

                Map<String,String> whereArgs = new HashMap<>();
                whereArgs.put(DbSetting.KEY_NAME,setting.getKeyName());
                whereArgs.put(DbSetting.VALUE1,setting.getValue1());
                whereArgs.put(DbSetting.VALUE2,setting.getValue2());
                Boolean result = App.getDbSetting().post(setting,whereArgs);
                if (!result){
                    App.showMessage("Değerler güncellemedi");
                }
            }
        });

    }
}