package com.app.binancealarm;


public class FragmentFactory {

    private static FragmentFactory instance;
    private FragmentMain[] fragmentMains;

    private FragmentFactory(){

    }

    public static FragmentFactory getInstance(){
        if (instance == null) {
            instance = new FragmentFactory();
        }
        return instance;
    }

    public FragmentAlarmBase create(int position){
        FragmentAlarmBase fragment = null;
        if (position == 0) fragment = new FragmentAlarmLastOrder();
        if (position == 1) fragment = new FragmentAlarmBuy();
        if (position == 2) fragment = new FragmentAlarmAsk();
        if (position == 3) fragment = new FragmentAlarmHighLow();
        return fragment;
    }

    public void initFragments(){
        if (this.fragmentMains != null) return;
        FragmentMain[] fragmentMains = {
                new FragmentMain(AppConst.TAB_FAVORITES),
                new FragmentMain(AppConst.TAB_BNB),
                new FragmentMain(AppConst.TAB_BTC),
                new FragmentMain(AppConst.TAB_ALTS),
                new FragmentMain(AppConst.TAB_USD) };
        this.fragmentMains = fragmentMains;
    }

    public FragmentMain[] getFragments(){
        return this.fragmentMains;
    }
}
