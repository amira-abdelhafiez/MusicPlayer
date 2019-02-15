package com.example.amira.musicplayer.fragments;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
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

import java.util.List;

/**
 * Created by Amira on 1/27/2019.
 */

public class HistoryFragment extends Fragment implements HistoryAdapter.ItemOnClickHandler{

    private static final String LOG_TAG = HistoryFragment.class.getSimpleName();
    private Context mContext;
    private List<History> mHistory;
    private HistoryViewModel mHistoryVM;

    private HistoryAdapter mAdapter;
    private RecyclerView mHistoryRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mHistoryVM = ViewModelProviders.of(this).get(HistoryViewModel.class);
        View rootView = inflater.inflate(R.layout.fragment_history , container , false);
        mHistoryRecyclerView = rootView.findViewById(R.id.rv_history);
        mAdapter = new HistoryAdapter(mContext , this);
        mLayoutManager = new LinearLayoutManager(mContext , LinearLayoutManager.VERTICAL , false);
        mHistoryRecyclerView.setAdapter(mAdapter);
        mHistoryRecyclerView.setLayoutManager(mLayoutManager);
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

//        @Override
//        protected void onPostExecute(List<History> dataCursor) {
//            super.onPostExecute(dataCursor);
//            if(dataCursor != null){
//                mHistory = dataCursor;
//                mAdapter.setmHistory(mHistory);
//            }else{
//                Log.d(LOG_TAG , "Null Cursor");
//            }
//        }
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
