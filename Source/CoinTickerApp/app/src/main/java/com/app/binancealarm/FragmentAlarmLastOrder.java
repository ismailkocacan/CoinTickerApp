package com.app.binancealarm;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.List;

public class FragmentAlarmLastOrder extends FragmentAlarmBase{

    private ListView mListView;
    private AdapterAlarmRuleLastOrder mAdapter;
    private List<AlarmRuleLastOrder> mList = new ArrayList<AlarmRuleLastOrder>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setAlarmPriceType(AlarmPriceType.LAST_ORDER);

        if (getSymbol() != null){
            mList = App.getDbSetting().
                    getListAlarmRuleLastOrder(getSymbol());
        }

        setHandler(new FragmentDialogAlarmHandler() {
            @Override
            public boolean onPost(Bundle bundle) {
                return post(bundle);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_alarm_lastorder, container, false);
        return root;
    }

    /*
    * fiyat ve fark alanlarının her ikisi dolu ise, 2 satır kayıt olarak ekleniyor.
     * */
    public boolean post(Bundle bundle){
        boolean result = false;
        try{
            Setting row = new Setting(bundle);
            if (row.getValue3() != null && row.getValue4() != null){

                Setting rowFiyat = new Setting(row);
                rowFiyat.setValue4(null); // farkı boşalt
                Setting rowFark = new Setting(row);
                rowFark.setValue3(null); // fiyatı boşalt

                boolean resultFiyat = App.getDbSetting().insert(rowFiyat);
                boolean resultFark = App.getDbSetting().insert(rowFark);
                if (resultFiyat){
                    mList.add(new AlarmRuleLastOrder(rowFiyat));
                    resultFiyat = true;
                }
                if (resultFark){
                    mList.add(new AlarmRuleLastOrder(rowFark));
                    resultFark = true;
                }
                result = resultFiyat && resultFark;
                if (result){
                    mAdapter.notifyDataSetChanged();
                    showSnackMessage(App.getString(R.string.appConstMessageAlarmInserted));
                }
            }else{
                if (App.getDbSetting().insert(row)) {
                    mList.add(new AlarmRuleLastOrder(row));
                    mAdapter.notifyDataSetChanged();
                    showSnackMessage(App.getString(R.string.appConstMessageAlarmInserted));
                    result = true;
                }
            }

        }catch (Exception e){
            result = false;
            App.log(e);
        }
        return result;
    }

    @Override
    public void onViewCreated(View view,Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mListView = getView().findViewById(R.id.listview_LastOrder);
        mAdapter = new AdapterAlarmRuleLastOrder(mListView.getContext(), R.layout.listview_rowitem_alarmrule,mList);
        mListView.setAdapter(mAdapter);
    }
}