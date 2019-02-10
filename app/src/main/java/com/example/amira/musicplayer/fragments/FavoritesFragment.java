package com.example.amira.musicplayer.fragments;

import android.content.Context;
import android.content.Intent;
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

import com.example.amira.musicplayer.R;
import com.example.amira.musicplayer.adapters.FavoritesAdapter;
import com.example.amira.musicplayer.data.AppDatabase;
import com.example.amira.musicplayer.models.Favorite;
import com.example.amira.musicplayer.ui.PlayerActivity;

import java.util.List;

/**
 * Created by Amira on 1/27/2019.
 */

public class FavoritesFragment extends Fragment implements FavoritesAdapter.ItemOnClickHandler{
    private static final String LOG_TAG = FavoritesFragment.class.getSimpleName();
    private Context mContext;
    private List<Favorite> mFavList;
    private AppDatabase mDb;

    private FavoritesAdapter mAdapter;
    private RecyclerView mFavRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_favorites , container , false);
        mDb = AppDatabase.getsInstance(mContext);
        mFavRecyclerView = rootView.findViewById(R.id.rv_favs);
        mAdapter = new FavoritesAdapter(mContext , this);
        mLayoutManager = new LinearLayoutManager(mContext , LinearLayoutManager.VERTICAL , false);
        mFavRecyclerView.setAdapter(mAdapter);
        mFavRecyclerView.setLayoutManager(mLayoutManager);
        new GetFavQuery().execute();
        return rootView;
    }

    public void setmContext(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public void onClickItem(int position) {
        Intent intent = new Intent(mContext , PlayerActivity.class);
        intent.putExtra(Intent.EXTRA_TEXT , mFavList.get(position).getId());
        startActivity(intent);
    }

    class GetFavQuery extends AsyncTask<Void , Void , List<Favorite>>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<Favorite> doInBackground(Void... voids) {
            List<Favorite> Favs = mDb.favoriteDao().getFavorites();
            return Favs;
        }

        @Override
        protected void onPostExecute(List<Favorite> favorites) {
            super.onPostExecute(favorites);
            if(favorites != null){
                Log.d("FavoriteQery" , "the sixe is " + favorites.size());
                mFavList = favorites;
                mAdapter.setmFavorites(mFavList);
            }else{
                Log.d("FavoriteQery" , "Favorite null");
                Log.d(LOG_TAG , "Null Cursor");
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
}
