package com.app.binancealarm;

import android.content.Context;
import android.content.SharedPreferences;

public class FavoriteManager {

    private final static String SHARED_PREF_NAME = "favorites";

    private SharedPreferences sharedPrefFavorites;
    private static FavoriteManager instance;

    private FavoriteManager(){
        Context mContext = CustomApplication.getInstance().getApplicationContext();
        sharedPrefFavorites = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
    }

    public static FavoriteManager getInstance(){
        if (instance == null){
            instance = new FavoriteManager();
        }
        return instance;
    }

    public boolean add(String symbolName) {
        boolean result = false;
        if (!sharedPrefFavorites.contains(symbolName)) {
            SharedPreferences.Editor editor = sharedPrefFavorites.edit();
            editor.putString(symbolName, symbolName);
            result =  editor.commit();
        }
        return result;
    }

    public boolean remove(String symbolName){
        boolean result = false;
        if (sharedPrefFavorites.contains(symbolName)){
            SharedPreferences.Editor editor = sharedPrefFavorites.edit();
            editor.remove(symbolName);
            result = editor.commit();
        }
        return result;
    }

    public int getFavoriteCount(){
        return sharedPrefFavorites.getAll().size();
    }

    public boolean isFavoriteAdded(String symbolName){
        return sharedPrefFavorites.contains(symbolName.trim());
    }
}
