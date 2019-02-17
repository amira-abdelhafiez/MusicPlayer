package com.example.amira.musicplayer.ui;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.amira.musicplayer.AnalyticsApplication;
import com.example.amira.musicplayer.R;
import com.example.amira.musicplayer.fragments.FavoritesFragment;
import com.example.amira.musicplayer.fragments.HistoryFragment;
import com.example.amira.musicplayer.fragments.HomeFragment;
import com.example.amira.musicplayer.utils.JsonUtils;
import com.example.amira.musicplayer.utils.NetworkUtils;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.io.IOException;
import java.net.URL;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String LOG_TAG = HomeActivity.class.getSimpleName();
    private static final String CURRENT_FRAGMENT_ID = "fragmentId";
    @BindView(R.id.drawer_layout) DrawerLayout mDrawer;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.nav_view) NavigationView mNavigationView;

    private AnalyticsApplication mApplication;
    private Tracker mTracker;

    private HomeFragment mHomeFragment;
    private HistoryFragment mHistoryFragment;
    private FavoritesFragment mFavoritesFragment;
    private SharedPreferences prefs;
    private int mCurrentFragmentID = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs = getSharedPreferences("MusicToken", MODE_PRIVATE);
        setUpLanguage();
        setContentView(R.layout.activity_home);
        mApplication = (AnalyticsApplication) getApplication();
        mTracker = mApplication.getDefaultTracker();
        new newTokenQuery().execute();

        ButterKnife.bind(this);

        if(savedInstanceState != null && savedInstanceState.containsKey(CURRENT_FRAGMENT_ID)){
            mCurrentFragmentID = savedInstanceState.getInt(CURRENT_FRAGMENT_ID , 0);
            Log.d("Amira" , "get Saved " + mCurrentFragmentID);
        }
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawer.addDrawerListener(toggle);
        toggle.syncState();
        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        mNavigationView.setNavigationItemSelectedListener(this);
        // Insert the fragment by replacing any existing fragment
        mHomeFragment = new HomeFragment();
        mHomeFragment.setmContext(this);
        mFavoritesFragment = new FavoritesFragment();
        mFavoritesFragment.setmContext(this);
        mHistoryFragment = new HistoryFragment();
        mHistoryFragment.setmContext(this);

        FragmentManager fragmentManager = getSupportFragmentManager();
        Log.d("Amira" , "Current is " + mCurrentFragmentID);
        if(mCurrentFragmentID == 0){
            fragmentManager.beginTransaction().replace(R.id.flContent, mHomeFragment).commit();
            setChecked(0);
        }else if(mCurrentFragmentID == 2){
            fragmentManager.beginTransaction().replace(R.id.flContent, mHistoryFragment).commit();
            setChecked(2);
        }else{
            fragmentManager.beginTransaction().replace(R.id.flContent, mFavoritesFragment).commit();
            setChecked(1);
        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_change_language) {
            openDialog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void openDialog(){
        // Initialize Dialog
        final Dialog dialog = new Dialog(HomeActivity.this);
        String Dialogtitle = getResources().getString(R.string.choose_langage_dialog_title);
        dialog.setTitle(Dialogtitle);
        dialog.setContentView(R.layout.custom_dialog);

        // Get views
        RadioGroup mSortRadioGroup = (RadioGroup) dialog.findViewById(R.id.rg_language);
        final RadioButton mArabicRadio = dialog.findViewById(R.id.rd_arabic);
        final RadioButton mEnglishRadio = dialog.findViewById(R.id.rd_english);

        // The radio group doesn't automatically so i have to do this (check and uncheck)
        int languageId = 1;
        if(prefs.contains("language")){
            languageId = prefs.getInt("language" , 1);
        }
        if(languageId == 1){
            mEnglishRadio.setChecked(true);
            mArabicRadio.setChecked(false);
        }else if(languageId ==2){
            mEnglishRadio.setChecked(false);
            mArabicRadio.setChecked(true);
        }
        mSortRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int id = 1;
                switch (checkedId){
                    case R.id.rd_arabic:
                        mArabicRadio.setChecked(true);
                        mEnglishRadio.setChecked(false);
                        id = 2;
                        setLanguage("ar" , id);
                        break;
                    case R.id.rd_english:
                        id = 1;
                        mArabicRadio.setChecked(false);
                        mEnglishRadio.setChecked(true);
                        setLanguage("en" , id);
                        break;
                }

                dialog.dismiss();
                finish();
                startActivity(getIntent());
            }
        });
        dialog.show();
    }
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        int index = 0;
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (id == R.id.nav_favs) {
            // Handle the camera action
            mFavoritesFragment = new FavoritesFragment();
            mFavoritesFragment.setmContext(HomeActivity.this);
            fragmentManager.beginTransaction().replace(R.id.flContent, mFavoritesFragment).commit();
            index = 1;
        } else if (id == R.id.nav_history) {
            mHistoryFragment = new HistoryFragment();
            mHistoryFragment.setmContext(HomeActivity.this);
            fragmentManager.beginTransaction().replace(R.id.flContent, mHistoryFragment).commit();
            index = 2;
        }else if(id == R.id.nav_home){
            mHomeFragment = new HomeFragment();
            mHomeFragment.setmContext(HomeActivity.this);
            fragmentManager.beginTransaction().replace(R.id.flContent, mHomeFragment).commit();
        }
        mCurrentFragmentID = index;
        // Set action bar title
        setTitle(item.getTitle().toString());
        setChecked(index);
        // Close the navigation drawer
        mDrawer.closeDrawers();

        return true;
    }

    public void setChecked(int index){
        mNavigationView.getMenu().getItem(0).getSubMenu().getItem(index).setChecked(true);
        for(int i = 0 ; i < 3 ; i++){
            if(i != index){
                mNavigationView.getMenu().getItem(0).getSubMenu().getItem(i).setChecked(false);
            }
        }
    }


    class newTokenQuery extends AsyncTask<Void , Void , String>{
        @Override
        protected String doInBackground(Void... voids) {
            String result = null;
            try{
                result = NetworkUtils.getNewToken();
                //Log.d("Amira" , result);
            }catch(IOException e){
                Log.d("Amira" , "null result");
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(s != null){
                String token = JsonUtils.getParsedToken(s);
                SharedPreferences.Editor editor = getSharedPreferences("MusicToken", MODE_PRIVATE).edit();
                editor.putString("token", token);
                editor.apply();
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(CURRENT_FRAGMENT_ID , mCurrentFragmentID);
        Log.d("Amira" , "Saving " + mCurrentFragmentID);
        super.onSaveInstanceState(outState);
    }

    private void setUpLanguage(){
        int languageId = 1;
        if(prefs.contains("language")){
            languageId = prefs.getInt("language" , 1);
        }
        if(languageId == 1){
            setLanguage("en" , 1);
        }else{
            setLanguage("ar" , 2);
        }
    }

    private void setLanguage(String lang , int id){
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.setLocale(locale);
        getBaseContext().getResources().updateConfiguration(configuration , getBaseContext().getResources().getDisplayMetrics());
        SharedPreferences.Editor editor = getSharedPreferences("MusicToken", MODE_PRIVATE).edit();
        editor.putInt("language", id);
        editor.apply();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Set screen name.
        mTracker.setScreenName("Home Screen");

        // Send a screen view.
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());

        GoogleAnalytics.getInstance(this).dispatchLocalHits();
    }
}
