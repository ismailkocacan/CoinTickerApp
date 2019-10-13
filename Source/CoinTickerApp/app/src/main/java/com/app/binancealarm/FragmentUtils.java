package com.app.binancealarm;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.util.List;

public class FragmentUtils {


    public static void commitFragment(int containerViewId,FragmentManager fm,Fragment fragment){
        fm.beginTransaction().
                replace(containerViewId, fragment).
                commit();
    }

    public static void commitFragment(FragmentManager fm,Fragment fragment){
        commitFragment(R.id.frameLayout,fm,fragment);
    }

    public static void commitFragmentWithAdd(FragmentManager fm, Fragment fragment, String tag){
        fm.beginTransaction().
                add(R.id.frameLayout, fragment,tag).
                addToBackStack(null).
                hide(fragment).commit();
    }

    public static void show(FragmentManager fm,String tag){
        Fragment fragment = fm.findFragmentByTag(tag);
        if (fragment == null) return;
        if (!fragment.isAdded()) return;
        fm.beginTransaction().show(fragment).commit();
    }

    public static void hide(FragmentManager fm,String tag){
        Fragment fragment = fm.findFragmentByTag(tag);
        if (fragment == null) return;
        if (!fragment.isAdded()) return;
        fm.beginTransaction().hide(fragment).commit();
    }

    public static void visible(FragmentManager fm,String tag){
        List<Fragment> fragments = fm.getFragments();
        for (Fragment fragment:fragments){
            if (!(fragment instanceof FragmentMain)) continue;
            FragmentMain fragmentMain = (FragmentMain)fragment;
            if (!fragmentMain.isAdded()) continue;
            if (fragmentMain.getSymbolName().contains(tag)){
                fm.beginTransaction().show(fragmentMain).commit();
                fragmentCurrent = fragmentMain;
            } else {
                fm.beginTransaction().hide(fragmentMain).commit();
            }
        }
    }

    private static FragmentMain fragmentCurrent;
    public static FragmentMain getCurrentFragment(){
        return fragmentCurrent;
    }
}
