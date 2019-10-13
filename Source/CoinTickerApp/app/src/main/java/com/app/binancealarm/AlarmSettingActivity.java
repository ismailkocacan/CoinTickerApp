package com.app.binancealarm;

import android.os.Bundle;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.view.View;
import android.widget.TextView;

import java.util.List;

public class AlarmSettingActivity
        extends AppCompatActivity
        implements ServiceResponseSingleBinance,
        View.OnClickListener,
        TabLayout.OnTabSelectedListener {

    public final static int TAB_POSITION_LASTORDER = 0;
    public final static int TAB_POSITION_BUY = 1;
    public final static int TAB_POSITION_ASK = 2;
    public final static int TAB_POSITION_HIGH_LOW = 3;

    String symbolName;
    FloatingActionButton fab;

    FragmentManager fm;
    FragmentAlarmBase fragment;
    private FragmentFactory fragmentFactory = FragmentFactory.getInstance();

    private void initAdvertise() {
        try {
            AdView adView = findViewById(R.id.adView2);
            AdRequest adRequest = new AdRequest.Builder().build();
            adView.loadAd(adRequest);
        } catch (Exception e) {
            App.log(e);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_setting);

        WebSocketServiceClient.getInstance().
                addResponseHandler(this);

        if (getSupportFragmentManager() != null) {
            fm = getSupportFragmentManager();
        }


        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            symbolName = bundle.getString(AppConst.SYMBOL_NAME);
            setTitle(symbolName);
            FragmentUtils.commitFragment(R.id.frameLayoutAlarmDetail,fm, getFragment(TAB_POSITION_LASTORDER));
        }
 
        fab = findViewById(R.id.fab);
        fab.setOnClickListener(this);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.addOnTabSelectedListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        App.log("AlarmSettingActivity.onResume");
        initAdvertise();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        WebSocketServiceClient.getInstance().
                removeResponseHandler(this);
    }

    private FragmentAlarmBase getCurrentFragment(){
        FragmentAlarmBase currentFragment = null;
        if (fm != null) {
            List<Fragment> fragments = fm.getFragments();
            if (!fragments.isEmpty())
                currentFragment = (FragmentAlarmBase)fragments.get(0);
        }
        return currentFragment;
    }

    FragmentAlarmBase getFragment(int position){
        fragment = fragmentFactory.create(position);
        fragment.setSymbol(symbolName);
        return fragment;
    }

    @Override
    public void onClick(View view) {
        FragmentAlarmBase currentFragment = getCurrentFragment();
        FragmentDialogAlarm fragment = new FragmentDialogAlarm(currentFragment);
        String title = currentFragment.getAlarmPriceType().getText();
        fragment.setDialogTitle(title);
        fragment.setCancelable(true);
        fragment.setStyle(DialogFragment.STYLE_NORMAL, 0);
        fragment.setShowsDialog(true);
        fragment.show(fm, "fragment_dialog_alarm");
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        try {
            int position = tab.getPosition();
            if (position == TAB_POSITION_HIGH_LOW) {
                fab.hide();
            } else {
                fab.show();
            }
            FragmentUtils.commitFragment(R.id.frameLayoutAlarmDetail, fm, getFragment(position));
        } catch (Exception e) {
            App.log(e);
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    private void setTitle(String symbolName){
        TextView textViewTitle = findViewById(R.id.title);
        String title = SymbolNameParser.format(symbolName) + " Alarm";
        textViewTitle.setText(title);
    }

    @Override
    public void onResponse(final TickerBinance ticker) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (symbolName.equals(ticker.getSymbol())){
                    View coinView = findViewById(R.id.detailRow);
                    ViewData.setData(coinView, ticker,symbolName);
                    setTitle(ticker.getSymbol());
                }
            }
        });
    }
}