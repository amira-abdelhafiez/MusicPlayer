package com.example.amira.musicplayer.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.amira.musicplayer.R;

/**
 * Created by Amira on 1/27/2019.
 */

public class HistoryFragment extends Fragment {
    private static final String LOG_TAG = HistoryFragment.class.getSimpleName();
    private Context mContext;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_history , container , false);
        return rootView;
    }

    public void setmContext(Context mContext) {
        this.mContext = mContext;
    }
}
