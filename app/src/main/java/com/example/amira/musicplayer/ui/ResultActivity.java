package com.example.amira.musicplayer.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.amira.musicplayer.AnalyticsApplication;
import com.example.amira.musicplayer.R;
import com.example.amira.musicplayer.adapters.RecentAdapter;
import com.example.amira.musicplayer.adapters.SearchAdapter;
import com.example.amira.musicplayer.models.Album;
import com.example.amira.musicplayer.models.Track;
import com.example.amira.musicplayer.utils.JsonUtils;
import com.example.amira.musicplayer.utils.NetworkUtils;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.StandardExceptionParser;
import com.google.android.gms.analytics.Tracker;

import java.io.IOException;
import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ResultActivity extends AppCompatActivity implements SearchAdapter.ItemOnClickHandler{

    private static final String LOG_TAG = ResultActivity.class.getSimpleName();
    private static final String RV_POSITION = "searchposition";
    private String mSearchPhrase;

    @BindView(R.id.rv_search) RecyclerView mSearchRecyclerView;
    @BindView(R.id.tv_error_message_search)
    TextView mErrorMessage;
    @BindView(R.id.pb_loading_bar_search)
    ProgressBar mLoadingBar;
    @BindView(R.id.tv_search_phrase)
    TextView mSearchPhraseTextView;
    private SearchAdapter mSearchAdapter;
    private LinearLayoutManager mLinearLayoutManager;
    private Track[] mAlbums;
    private int mCurrentScrollPosition = 0;
    private SharedPreferences prefs;

    private AnalyticsApplication mApplication;
    private Tracker mTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        Intent callingIntent = getIntent();
        if(callingIntent != null && callingIntent.hasExtra(Intent.EXTRA_TEXT)){
            mSearchPhrase = callingIntent.getStringExtra(Intent.EXTRA_TEXT);
        }
        prefs = getSharedPreferences("MusicToken", MODE_PRIVATE);
        ButterKnife.bind(this);
        mApplication = (AnalyticsApplication) getApplication();
        mTracker = mApplication.getDefaultTracker();
        mSearchPhraseTextView.append( " " + mSearchPhrase);
        hideErrorMessage();
        new SearchQuery().execute(mSearchPhrase);
        mSearchAdapter = new SearchAdapter(this , this);
        mLinearLayoutManager = new LinearLayoutManager(this , LinearLayoutManager.VERTICAL , false);
        mSearchRecyclerView.setAdapter(mSearchAdapter);
        mSearchRecyclerView.setLayoutManager(mLinearLayoutManager);
    }

    @Override
    public void onClickItem(int position) {
        Intent intent = new Intent(ResultActivity.this , PlayerActivity.class);
        intent.putExtra(Intent.EXTRA_TEXT , mAlbums[position].getId());
        startActivity(intent);
    }

    class SearchQuery extends AsyncTask<String , Void , String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showLoadingSpinner();
        }

        @Override
        protected String doInBackground(String... strings) {
            String result = null;
            String phrase = strings[0];
            URL url = NetworkUtils.buildSearchDataUrl(phrase , "30");
            Log.d("ParsedJson" , url.toString());
            String token = prefs.getString("token" , null);
            try {
                result = NetworkUtils.getData(url , token);
            } catch (IOException e) {
                Log.d("ParsedJson" , "Exception" + e.getMessage());
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(s != null && !(s.isEmpty())){
                mAlbums = JsonUtils.ParseSearchAlbums(s);
                mSearchAdapter.setmAlbums(mAlbums);
                hideErrorMessage();
                hideLoadingSpinner();
                if(prefs.contains(RV_POSITION)){
                    mCurrentScrollPosition = prefs.getInt(RV_POSITION , 0);
                }
                try {
                    mLinearLayoutManager.scrollToPosition(mCurrentScrollPosition);
                }catch (Exception e){

                }
            }else{
                hideLoadingSpinner();
                showErrorMessage();
            }
        }
    }

    private void showErrorMessage(){
        mSearchRecyclerView.setVisibility(View.INVISIBLE);
        mErrorMessage.setVisibility(View.VISIBLE);
    }
    private void hideErrorMessage(){
        mErrorMessage.setVisibility(View.INVISIBLE);
        mSearchRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showLoadingSpinner(){
        mLoadingBar.setVisibility(View.VISIBLE);
        mSearchRecyclerView.setVisibility(View.INVISIBLE);
    }

    private void hideLoadingSpinner(){
        mLoadingBar.setVisibility(View.INVISIBLE);
        mSearchRecyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Set screen name.
        mTracker.setScreenName("Result Screen");

        // Send a screen view.
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());

        GoogleAnalytics.getInstance(this).dispatchLocalHits();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        int currentVisiblePosition = 0;
        currentVisiblePosition = mLinearLayoutManager.findFirstVisibleItemPosition();
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(RV_POSITION , currentVisiblePosition);
        editor.apply();
        super.onSaveInstanceState(outState);
    }
}
