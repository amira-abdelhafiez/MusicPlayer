package com.example.amira.musicplayer.fragments;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.amira.musicplayer.R;
import com.example.amira.musicplayer.adapters.HistoryAdapter;
import com.example.amira.musicplayer.data.AppDatabase;
import com.example.amira.musicplayer.data.DataContract;
import com.example.amira.musicplayer.models.Favorite;
import com.example.amira.musicplayer.models.FavoriteViewModel;
import com.example.amira.musicplayer.models.History;
import com.example.amira.musicplayer.models.HistoryViewModel;
import com.example.amira.musicplayer.ui.PlayerActivity;
import com.example.amira.musicplayer.ui.ResultActivity;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Amira on 1/27/2019.
 */

public class HistoryFragment extends Fragment implements HistoryAdapter.ItemOnClickHandler{

    private static final String LOG_TAG = HistoryFragment.class.getSimpleName();
    private static final String RV_POSITION = "historyposition";
    private Context mContext;
    private List<History> mHistory;
    private HistoryViewModel mHistoryVM;

    private AdView mAdView;
    private ProgressBar mLoadingBar;
    private TextView mErrorMessage;
    private HistoryAdapter mAdapter;
    private RecyclerView mHistoryRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private SharedPreferences prefs;
    private int mCurrentScrollPosition = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mHistoryVM = ViewModelProviders.of(this).get(HistoryViewModel.class);
        View rootView = inflater.inflate(R.layout.fragment_history , container , false);
        mContext = getContext();
        prefs = mContext.getSharedPreferences("MusicToken", MODE_PRIVATE);
//        MobileAds.initialize(mContext,
//                "ca-app-pub-3940256099942544~3347511713");
        mLoadingBar = rootView.findViewById(R.id.pb_loading_bar_history);
        mErrorMessage = rootView.findViewById(R.id.tv_error_message_history);
        mAdView = rootView.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        mHistoryRecyclerView = rootView.findViewById(R.id.rv_history);
        mAdapter = new HistoryAdapter(mContext , this);
        mLayoutManager = new LinearLayoutManager(mContext , LinearLayoutManager.VERTICAL , false);
        mHistoryRecyclerView.setAdapter(mAdapter);
        mHistoryRecyclerView.setLayoutManager(mLayoutManager);
        hideErrorMessage();
        new GetHistoryQuery().execute();
        return rootView;
    }

    public void setmContext(Context mContext) {
        this.mContext = mContext;
    }
    private void setmHistory(List<History> favHistory){
        this.mHistory = favHistory;
    }

    @Override
    public void onClickItem(int position) {
        Intent intent = new Intent(mContext , PlayerActivity.class);
        intent.putExtra(Intent.EXTRA_TEXT , mHistory.get(position).getSpotifyId());
        startActivity(intent);
    }

    class GetHistoryQuery extends AsyncTask<Void , Void , Void>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showLoadingSpinner();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            mHistoryVM.getHistory().observe((LifecycleOwner) mContext, new Observer<List<History>>() {
                @Override
                public void onChanged(@Nullable List<History> histories) {
                    mAdapter.setmHistory(histories);
                    setmHistory(histories);
                }
            });
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            hideLoadingSpinner();
            hideErrorMessage();
            if(prefs.contains(RV_POSITION)){
                mCurrentScrollPosition = prefs.getInt(RV_POSITION , 0);
            }
            try{
                mLayoutManager.scrollToPosition(mCurrentScrollPosition);
            }catch(Exception e){

            }
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (getFragmentManager() != null) {

            getFragmentManager()
                    .beginTransaction()
                    .detach(this)
                    .attach(this)
                    .commit();
        }
    }

    private void showErrorMessage(){
        mHistoryRecyclerView.setVisibility(View.INVISIBLE);
        mErrorMessage.setVisibility(View.VISIBLE);
    }
    private void hideErrorMessage(){
        mErrorMessage.setVisibility(View.INVISIBLE);
        mHistoryRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showLoadingSpinner(){
        mLoadingBar.setVisibility(View.VISIBLE);
        mHistoryRecyclerView.setVisibility(View.INVISIBLE);
    }

    private void hideLoadingSpinner(){
        mLoadingBar.setVisibility(View.INVISIBLE);
        mHistoryRecyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        int currentVisiblePosition = 0;
        currentVisiblePosition = mLayoutManager.findFirstVisibleItemPosition();
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(RV_POSITION , currentVisiblePosition);
        editor.apply();
        super.onSaveInstanceState(outState);
    }
}
