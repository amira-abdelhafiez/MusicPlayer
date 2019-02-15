package com.example.amira.musicplayer.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
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

import com.example.amira.musicplayer.R;
import com.example.amira.musicplayer.fragments.FavoritesFragment;
import com.example.amira.musicplayer.fragments.HistoryFragment;
import com.example.amira.musicplayer.fragments.HomeFragment;
import com.example.amira.musicplayer.utils.JsonUtils;
import com.example.amira.musicplayer.utils.NetworkUtils;

import java.io.IOException;
import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String LOG_TAG = HomeActivity.class.getSimpleName();

    @BindView(R.id.drawer_layout) DrawerLayout mDrawer;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.nav_view) NavigationView mNavigationView;

    private HomeFragment mHomeFragment;
    private HistoryFragment mHistoryFragment;
    private FavoritesFragment mFavoritesFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        new newTokenQuery().execute();

        ButterKnife.bind(this);
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
        fragmentManager.beginTransaction().replace(R.id.flContent, mHomeFragment).commit();
        setChecked(0);
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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

}
