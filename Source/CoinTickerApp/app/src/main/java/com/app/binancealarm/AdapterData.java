package com.app.binancealarm;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.ImageView;

public class AdapterData
       extends ArrayAdapter<TickerBinance>
       implements View.OnClickListener {

    private static final String SEPERATOR = "_";

    private Context mContext;
    private Tickers mList,mListTemp;
    private String mSymbol;
    private FilterMain filterMain = new FilterMain();

    public AdapterData(Context context, int resource, Tickers list, String symbol) {
        super(context, resource, list);
        mContext = context;
        mList = list;
        mListTemp = list;
        mSymbol = symbol;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View row = inflater.inflate(R.layout.data_table, parent, false);
        TickerBinance ticker = mList.get(position);
        ViewData.setData(row,ticker,mSymbol);
        Button buttonAlarm = row.findViewById(R.id.btnAlarm);
        buttonAlarm.setTag(ticker.getSymbol());


        String tagValue = ticker.getSymbol()+ SEPERATOR +position;

        Button btnFavoriteRemove = row.findViewById(R.id.btnFavoriteRemove);
        btnFavoriteRemove.setTag(tagValue);
        btnFavoriteRemove.setOnClickListener(this);

        ImageView imgFavorite = row.findViewById(R.id.imgFavorite);
        imgFavorite.setTag(tagValue);

        Button btnFavoriteAdd = row.findViewById(R.id.btnFavoriteAdd);
        btnFavoriteAdd.setTag(imgFavorite);
        btnFavoriteAdd.setOnClickListener(this);

        btnFavoriteRemove.setVisibility(View.GONE);
        btnFavoriteAdd.setVisibility(View.GONE);
        if (mSymbol.contains(AppConst.TAB_FAVORITES)){
            btnFavoriteRemove.setVisibility(View.VISIBLE);
            imgFavorite.setVisibility(View.GONE);
        } else {
            if (!FavoriteManager.getInstance().isFavoriteAdded(ticker.getSymbol())){
                btnFavoriteAdd.setVisibility(View.VISIBLE);
            } else {
                setFavorite(imgFavorite,true);
            }
        }
        return row;
    }

    private void setFavorite(ImageView imgFavorite,boolean visible){
        imgFavorite.setVisibility(visible ? View.VISIBLE : View.GONE);
        imgFavorite.setBackgroundResource(R.drawable.ic_star_24dp);
    }

    public void favoriteAdd(View sender){
        try{
            if (!(sender.getTag() instanceof  ImageView))
                throw new Exception("sender is not ImageView class type!");
            ImageView imageFavorite = (ImageView)sender.getTag();
            String symbolName = (String)imageFavorite.getTag();
            int index = Integer.parseInt(symbolName.split(SEPERATOR)[1]);
            symbolName = symbolName.split(SEPERATOR)[0];
            if (FavoriteManager.getInstance().add(symbolName)){
                sender.setVisibility(View.GONE);
                setFavorite(imageFavorite,true);
                addToFavorite(index);
            }
        } catch (Exception e){
            App.log(e);
            App.showMessage(e.getMessage());
        }
    }

    private void addToFavorite(int index) {
        TickerBinance ticker = this.mList.get(index);
        FragmentFactory factory = FragmentFactory.getInstance();
        FragmentMain favorites = factory.getFragments()[AppConst.TAB_POSITION_FAVORITES];
        favorites.getTickers().add(ticker);
        favorites.notifyDataSetChanged();
    }

    public void favoriteRemove(View sender){
        try{
            String symbolName = (String)sender.getTag();
            int position = Integer.parseInt(symbolName.split("_")[1]);
            symbolName = symbolName.split(SEPERATOR)[0];
            if (FavoriteManager.getInstance().remove(symbolName)){
                mList.remove(position);
                notifyDataSetChanged();
            }
        } catch (Exception e){
            App.log(e);
            App.showMessage(e.getMessage());
        }
    }

    @Override
    public void onClick(View sender) {
        if (sender.getId() == R.id.btnFavoriteRemove){
            favoriteRemove(sender);
        }

        if (sender.getId() == R.id.btnFavoriteAdd){
            favoriteAdd(sender);
        }
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    public void sortTickers(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            mList.sort(SorterTickerSymbolAndVolume.getInstance());
        }
    }

    @Override
    public Filter getFilter() {
        return filterMain;
    }

    class FilterMain extends Filter{
        private Tickers foundTickers = new Tickers();
        private FilterResults filterResults = new FilterResults();

        @Override
        protected FilterResults performFiltering(CharSequence searchChars) {
            String symbol,searchText;
            if (searchChars != null){
                if (searchChars.length() > 0){
                    foundTickers.clear();
                    searchText = searchChars.toString().toUpperCase();
                    App.log(searchText);
                    for (int i = 0; i < mListTemp.size(); i++){
                        TickerBinance ticker = mListTemp.get(i);
                        symbol = ticker.getSymbol().toUpperCase();
                        if (symbol.contains(searchText)){
                            foundTickers.add(ticker);
                            App.log(ticker.getSymbol()+" bulundu");
                        }
                    }
                    setFilterResults(foundTickers);
                }
            } else {
                setFilterResults(mListTemp);
            }
            return filterResults;
        }

        private void setFilterResults(Tickers tickers){
            filterResults.count = tickers.size();
            filterResults.values = tickers;
        }

        @Override
        protected void publishResults(CharSequence searchChars, FilterResults filterResults) {
            App.log("publishResults: "+searchChars);
            if (filterResults.count > 0){
                Tickers tickers = (Tickers)filterResults.values;
                mList = tickers;
            }
            if (searchChars.length() == 0) {
                mList = mListTemp;
            }
            notifyDataSetChanged();
        }
    }
}