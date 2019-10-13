package com.app.binancealarm;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;


public class FragmentMain
        extends Fragment
        implements Runnable, ServiceResponseMultiBinance {

    private String symbolName;
    private ListView listView;
    private AdapterData adapterData;
    private Tickers tickers = Tickers.create();
    private SymbolNameParser parserTicker = new SymbolNameParser();

    public FragmentMain(String symbolName) {
        this.symbolName = symbolName;
        WebSocketServiceClient.getInstance().addResponseHandler(this);
    }

    public String getSymbolName() {
        return symbolName;
    }

    public Tickers getTickers() {
        return this.tickers;
    }

    public AdapterData getAdapterData() {
        return adapterData;
    }

    private void initAdvertise() {
        try{
            AdView adView = getView().findViewById(R.id.adView);
            AdRequest adRequest = new AdRequest.Builder().build();
            adView.loadAd(adRequest);
        }catch (Exception e){
            App.log(e);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main, container, false);
        return root;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initAdvertise();
        listView = getView().findViewById(R.id.listview);
        TextView viewEmpty = getView().findViewById(R.id.empty_list_item);
        viewEmpty.setVisibility(View.VISIBLE);
        listView.setEmptyView(viewEmpty);
        if (symbolName.contains(AppConst.TAB_FAVORITES)) {
            if (FavoriteManager.getInstance().getFavoriteCount() > 0) {
                viewEmpty.setText(App.getString(R.string.appConstTextLoadingFavorites));
            } else {
                viewEmpty.setText(App.getString(R.string.appConstFavoritesEmpty));
            }
        } else {
            viewEmpty.setText(App.getString(R.string.appConstTextLoading));
        }
        adapterData = new AdapterData(listView.getContext(), R.layout.data_table, tickers, symbolName);
        listView.setAdapter(adapterData);
    }

    @Override
    public void onResponse(Tickers tickers) {
        parseTickers(tickers);
        notifyDataSetChanged();
    }

    private void parseTickers(Tickers tickers) {
        for (int i = 0; i < tickers.size(); i++) {
            TickerBinance ticker = tickers.get(i);
            String tabName = parserTicker.parse(ticker, getSymbolName());
            if (getSymbolName().contains(AppConst.TAB_FAVORITES)) {
                if (FavoriteManager.getInstance().
                    isFavoriteAdded(ticker.getSymbol())) {
                    updateArray(ticker);
                }
            } else {
                if (tabName.contains(AppConst.TAB_BNB)) updateArray(ticker);
                if (tabName.contains(AppConst.TAB_BTC))  updateArray(ticker);
                if (tabName.contains(AppConst.TAB_ALTS)) updateArray(ticker);
                if (tabName.contains(AppConst.TAB_USD)) updateArray(ticker);
            }
        }
    }

    private void updateArray(TickerBinance ticker) {
        this.tickers.addOrUpdate(ticker);
    }

    public void notifyDataSetChanged() {
        if (getActivity() == null) return;
        adapterData.sortTickers();
        getActivity().runOnUiThread(this);
    }

    @Override
    public void run() {
        adapterData.notifyDataSetChanged();
    }
}
