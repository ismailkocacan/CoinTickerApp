package com.app.binancealarm;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.SearchView;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.material.tabs.TabLayout;


public class MainActivity
       extends AppCompatActivity
       implements TabLayout.OnTabSelectedListener{

    TabLayout tabs;
    boolean isInitDefaultTab;
    FragmentFactory factory = FragmentFactory.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            initFragments();
            initTabs();
            initService();
            initActionBar();
            initDefaultTab();
            isInitDefaultTab = false;
        }catch (Exception e){
            App.log(e);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        initDefaultTab();
    }

    private void initDefaultTab(){
        if (isInitDefaultTab) return;
        FragmentManager fm = getSupportFragmentManager();
        int favoritesCount= FavoriteManager.getInstance().getFavoriteCount();
        if (favoritesCount > 0){
            tabs.getTabAt(AppConst.TAB_POSITION_FAVORITES).select();
        } else {
            tabs.getTabAt(AppConst.TAB_POSITION_BNB).select();
        }
        FragmentUtils.visible(fm, favoritesCount > 0 ? AppConst.TAB_FAVORITES : AppConst.TAB_BNB);
        isInitDefaultTab = true;
    }

    /**
     * Android 8.0 üstü sürümlerde arkaplanda çalışan servisler belirli bir süreden sonra durduruluyor.
     * O nedenle WorkManager sınıfı(8.0 ve sonrası) kullanıldı.Work'ler Belirli periyodlarda çalıştığında,
     * bir thread oluşturularak, belirli sürelerde tekrar çalışması sağlanıyor.
     * */
    private void initService() {
        try{
            /*
            ServiceClient.getInstance().addResponseHandler(this);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                WorkManager mWorkManager = WorkManager.getInstance(this);

                Constraints constraints = new Constraints.Builder()
                        .setRequiredNetworkType(NetworkType.CONNECTED)
                        .build();

                ////Builder(AppWorker.class, MIN_PERIODIC_FLEX_MILLIS, TimeUnit.MILLISECONDS).build();
                PeriodicWorkRequest mRequest = new PeriodicWorkRequest
                        .Builder(AppWorker.class, 10, TimeUnit.SECONDS)
                        .setConstraints(constraints)
                        .build();

                mWorkManager.getWorkInfoByIdLiveData(mRequest.getId()).
                        observe(this, new Observer<WorkInfo>() {
                    @Override
                    public void onChanged(@Nullable WorkInfo workInfo) {
                        if (workInfo != null) {
                            WorkInfo.State state = workInfo.getState();
                            App.log(state.toString());
                        }
                    }
                });
                mWorkManager.enqueue(mRequest);
                App.log("Android 8.0 ve üzeri WorkManager çalışıyor.");
            } else {
                mServiceIntent = new Intent(MainActivity.this,AppService.class);
                startService(mServiceIntent);
                App.log("Android 8.0 daha düşük Service çalışıyor.");
            }
            */

            Intent serviceIntent = new Intent(this, AppServiceForeground.class);
            serviceIntent.putExtra("inputExtra", "test");
            ContextCompat.startForegroundService(this, serviceIntent);
        }catch (Exception e){
            App.log(e);
        }
    }


    private void initActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setLogo(R.drawable.ic_notifications);
            actionBar.setDisplayUseLogoEnabled(true);
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setTitle(Html.fromHtml("<font color='#FFC400'>"+App.getString(R.string.app_name)+"</font>"));
        }
    }

    private void initFragments(){
        try{
            factory.initFragments();
            final FragmentManager fm = getSupportFragmentManager();
            for (FragmentMain fragmentMain : factory.getFragments()){
                FragmentUtils.commitFragmentWithAdd(fm,fragmentMain,fragmentMain.getSymbolName());
            }
        }catch (Exception e){
            App.log(e);
        }
    }

    private void initTabs(){
        tabs = findViewById(R.id.tabs);
        LayoutInflater inflater = LayoutInflater.from(this);
        for (int i=0; i < tabs.getTabCount(); i++){
            View view = inflater.inflate(R.layout.tabitem_view,null);
            tabs.getTabAt(i).setCustomView(view);
        }
        tabs.addOnTabSelectedListener(this);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        try{
            int position = tab.getPosition();
            final FragmentManager fm = getSupportFragmentManager();
            FragmentMain fragmentMain = factory.getFragments()[position];
            FragmentUtils.visible(fm, fragmentMain.getSymbolName());
        }catch (Exception e){
            App.log(e);
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    public void btnAlarmOnClick(View sender){
        try{
            sender.setEnabled(false);
            String symbolName = (String)sender.getTag();
            Intent intent = new Intent(MainActivity.this, AlarmSettingActivity.class);
            intent.putExtra(AppConst.SYMBOL_NAME, symbolName);
            startActivity(intent);
            sender.setEnabled(true);
        } catch (Exception e){
            App.log(e);
            App.showMessage(e.getMessage());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        try {
            getMenuInflater().inflate(R.menu.menu, menu);
            MenuItem searchItem = menu.findItem(R.id.action_search);
            final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
            searchView.setSubmitButtonEnabled(false);
            searchView.setBackground(null);
            //searchView.setBackgroundResource(R.drawable.actionbar_style_searchview);
            searchView.setQueryHint(App.getString(R.string.searchViewQueryHint));
            searchView.setIconified(false); // varsayılan olarak açık gelmesi için
            searchView.setOnQueryTextListener(new SearchViewOnQueryTextListener());

            int idCloseBtn = getResources().getIdentifier("android:id/search_close_btn", null, null);
            int idEditText = searchView.getContext().getResources()
                    .getIdentifier("android:id/search_src_text", null, null);
            final EditText editText =  searchView.findViewById(idEditText);
            View closeButton = searchView.findViewById(idCloseBtn);
            closeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editText.setText("");
                }
            });

            MenuItemCompat.setOnActionExpandListener(searchItem, new MenuItemCompat.OnActionExpandListener() {
                @Override
                public boolean onMenuItemActionExpand(MenuItem item) {
                    return true;
                }

                @Override
                public boolean onMenuItemActionCollapse(MenuItem item) {
                    editText.setText("");
                    return true;
                }
            });

            searchItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    editText.requestFocus();
                    KeyboardUtils.open(editText);
                    return true;
                }
            });

        }catch (Exception e) {
            App.log(e);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        /*
        try{
            if (id == R.id.itemPreferences) {
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                intent.putExtra("refreshCoins", (Bundle)null);
                startActivity(intent);
            }else {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, AppConst.APP_URL);
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
            }
        }catch (Exception e){
            App.log(e);
            App.showMessage(e.getMessage());
        }*/
        return super.onOptionsItemSelected(item);
    }
}
