package com.app.binancealarm;

import android.widget.SearchView;

public class SearchViewOnQueryTextListener
       implements SearchView.OnQueryTextListener {

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        FragmentMain fm = FragmentUtils.getCurrentFragment();
        if (fm != null) fm.getAdapterData().getFilter().filter(s);
        return false;
    }
}
