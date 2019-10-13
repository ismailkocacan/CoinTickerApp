package com.app.binancealarm;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.List;

public class FragmentAlarmBuy extends FragmentAlarmBase {

    private ListView mListView;
    private AdapterAlarmRuleBuy mAdapter;
    private List<AlarmRule> mList = new ArrayList<AlarmRule>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setAlarmPriceType(AlarmPriceType.BUY);

        if (getSymbol() != null){
            mList = App.getDbSetting().
                    getListAlarmRule(getSymbol(),getAlarmPriceType());
        }

        setHandler(new FragmentDialogAlarmHandler() {
            @Override
            public boolean onPost(Bundle bundle) {
                boolean result = false;
                try{
                    Setting row = new Setting(bundle);
                    if (App.getDbSetting().insert(row)){
                        mList.add(new AlarmRuleBuy(row));
                        mAdapter.notifyDataSetChanged();
                        showSnackMessage(App.getString(R.string.appConstMessageAlarmInserted));
                        result = true;
                    }
                }catch (Exception e){
                    App.log(e);
                }
                return result;
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_alarm_buy, container, false);
        return root;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mListView = getView().findViewById(R.id.listview);
        mAdapter = new AdapterAlarmRuleBuy(mListView.getContext(), R.layout.listview_rowitem_alarmrule,mList);
        mListView.setAdapter(mAdapter);
    }
}