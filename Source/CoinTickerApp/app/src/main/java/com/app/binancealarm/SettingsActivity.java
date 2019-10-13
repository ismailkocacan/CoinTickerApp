package com.app.binancealarm;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.CheckBoxPreference;
import androidx.preference.EditTextPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceFragmentCompat;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class SettingsActivity extends AppCompatActivity {

    /*
    static Map<CoinType,Boolean> coinOldStates = new HashMap<>();
    static Map<CoinType,Boolean> coinNewStates = new HashMap<>();
    */

    public static class SettingsFragment extends PreferenceFragmentCompat
                                         implements Preference.OnPreferenceChangeListener {

        EditTextPreference edtPeriod;
        PreferenceCategory coins;

        @Override
        public void onViewCreated(View view, Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);

            /*
            coinOldStates.clear();
            coinNewStates.clear();
            coins = getPreferenceManager().findPreference("coins");
            CoinType[] coinTypes =  CoinType.values();
            for (CoinType coinType:coinTypes){
                CheckBoxPreference checkBoxPreference = new CheckBoxPreference(this.getContext());
                checkBoxPreference.setKey(coinType.name());
                checkBoxPreference.setTitle(AppConst.COIN_LONG_NAMES[coinType.ordinal()]);
                checkBoxPreference.setOnPreferenceChangeListener(this);
                coins.addPreference(checkBoxPreference);
                boolean bValue;
                if (App.getDbUserSetting().isKeyExist(coinType.name())){
                    String sValue = App.getDbUserSetting().getValue(coinType.name());
                    bValue = Boolean.valueOf(sValue);
                    checkBoxPreference.setChecked(bValue);
                }else{
                    checkBoxPreference.setChecked(true);
                    bValue = true;
                }
                coinOldStates.put(coinType,bValue);
                coinNewStates.put(coinType,bValue);
            }
            */

            /*
            edtPeriod = getPreferenceManager().findPreference("edtPeriod");
            edtPeriod.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    String value = newValue.toString();
                    if (value.length() > 0) {
                        App.getDbUserSetting().setValue(AppConst.KEY_REFRESH_PERIOD, value);
                        long lValue = Long.parseLong(value);
                        App.scheduleTimer(lValue);
                    }
                    return true;
                }
            });

            String value = App.getDbUserSetting().getValue(AppConst.KEY_REFRESH_PERIOD);
            if (value.isEmpty()){
                edtPeriod.setText(Long.toString(App.getRefreshPeriod()));
            }else {
                edtPeriod.setText(value);
            }
            */
        }

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
        }

        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            /*
            App.getDbUserSetting().setValue(preference.getKey(),newValue.toString());
            Boolean bValue = Boolean.valueOf(newValue.toString());
            CoinType coinType = CoinType.valueOf(preference.getKey().trim());
            coinNewStates.put(coinType,bValue);
            */
            return true;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings, new SettingsFragment())
                .commit();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    boolean isAnyCheckBoxPreferenceChanged(){
        boolean result = false;
        /*
        for (Map.Entry<CoinType,Boolean> entry :coinOldStates.entrySet()){
            if (coinNewStates.containsKey(entry.getKey())){
                boolean newState = coinNewStates.get(entry.getKey());
                if (newState != entry.getValue()){
                    return true;
                }
            }
        }*/
        return result;
    }

    @Override
    protected void onStop() {
        super.onStop();
        Bundle bundle = getIntent().getExtras();
        if (bundle == null) return;
        if (!bundle.containsKey("refreshCoins")) return;
        if (!isAnyCheckBoxPreferenceChanged()) return;

        Serializable serializable = bundle.getSerializable("refreshCoins");
        if (serializable instanceof Refreshable){
            Refreshable refreshable = ((Refreshable)bundle.getSerializable("refreshCoins"));
            refreshable.doRefresh();
        }
    }
}