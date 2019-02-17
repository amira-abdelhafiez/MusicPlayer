package com.example.amira.musicplayer.fragments;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.amira.musicplayer.R;
import com.example.amira.musicplayer.adapters.FavoritesAdapter;
import com.example.amira.musicplayer.data.AppDatabase;
import com.example.amira.musicplayer.models.Favorite;
import com.example.amira.musicplayer.models.FavoriteViewModel;
import com.example.amira.musicplayer.ui.PlayerActivity;
import com.example.amira.musicplayer.utils.LayoutUtils;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Amira on 1/27/2019.
 */

public class FavoritesFragment extends Fragment implements FavoritesAdapter.ItemOnClickHandler{
    private static final String LOG_TAG = FavoritesFragment.class.getSimpleName();
    private Context mContext;
    private List<Favorite> mFavList;
    private FavoriteViewModel mFavVM;

    private FavoritesAdapter mAdapter;
    private RecyclerView mFavRecyclerView;
    private GridLayoutManager mLayoutManager;
    private TextView mErrorMessage;
    private ProgressBar mLoadingBar;
    private static final String RV_POSITION = "favposition";
    private int mCurrentScrollPosition = 0;
    private SharedPreferences prefs;
    private AdView mAdView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_favorites , container , false);
        mContext = getContext();
        prefs = mContext.getSharedPreferences("MusicToken", MODE_PRIVATE);

        mAdView = rootView.findViewById(R.id.adViewFav);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        mLoadingBar = rootView.findViewById(R.id.pb_loading_bar_fav);
        mErrorMessage = rootView.findViewById(R.id.tv_error_message_fav);
        mFavVM = ViewModelProviders.of(this).get(FavoriteViewModel.class);
        mFavRecyclerView = rootView.findViewById(R.id.rv_favs);
        mAdapter = new FavoritesAdapter(mContext , this);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int value = displayMetrics.widthPixels;
        int valueDp = (int) LayoutUtils.convertPxToDp(mContext, (float)value);

        boolean IsLargeScreen = (valueDp > 600);
        int span , scalingFactor;
        if(IsLargeScreen){
            scalingFactor = 200;
        }else {
            scalingFactor = 150;
        }
        span = LayoutUtils.calculateNoOfColumns(mContext , scalingFactor);

        mLayoutManager = new GridLayoutManager(mContext , span);
        mFavRecyclerView.setAdapter(mAdapter);
        mFavRecyclerView.setLayoutManager(mLayoutManager);
        hideErrorMessage();
        new GetFavQuery().execute();
        return rootView;
    }

    public void setmContext(Context mContext) {
        this.mContext = mContext;
    }
    private void setmFavList(List<Favorite> favList){
        this.mFavList = favList;
    }
    @Override
    public void onClickItem(int position) {
        Intent intent = new Intent(mContext , PlayerActivity.class);
        intent.putExtra(Intent.EXTRA_TEXT , mFavList.get(position).getId());
        startActivity(intent);
    }

    class GetFavQuery extends AsyncTask<Void , Void , Void>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showLoadingSpinner();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            mFavVM.GetAllFavs().observe((LifecycleOwner) mContext, new Observer<List<Favorite>>() {
                @Override
                public void onChanged(@Nullable List<Favorite> favorites) {
                    mAdapter.setmFavorites(favorites);
                    setmFavList(favorites);
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
        mFavRecyclerView.setVisibility(View.INVISIBLE);
        mErrorMessage.setVisibility(View.VISIBLE);
    }
    private void hideErrorMessage(){
        mErrorMessage.setVisibility(View.INVISIBLE);
        mFavRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showLoadingSpinner(){
        mLoadingBar.setVisibility(View.VISIBLE);
        mFavRecyclerView.setVisibility(View.INVISIBLE);
    }

    private void hideLoadingSpinner(){
        mLoadingBar.setVisibility(View.INVISIBLE);
        mFavRecyclerView.setVisibility(View.VISIBLE);
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
