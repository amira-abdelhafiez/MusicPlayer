package com.example.amira.musicplayer.fragments;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.amira.musicplayer.R;
import com.example.amira.musicplayer.adapters.RecentAdapter;
import com.example.amira.musicplayer.models.Album;
import com.example.amira.musicplayer.ui.ResultActivity;
import com.example.amira.musicplayer.utils.JsonUtils;
import com.example.amira.musicplayer.utils.LayoutUtils;
import com.example.amira.musicplayer.utils.NetworkUtils;

import java.io.IOException;
import java.net.URL;

/**
 * Created by Amira on 1/27/2019.
 */

public class HomeFragment extends Fragment implements RecentAdapter.ItemOnClickHandler {

    private static final String LOG_TAG = HomeFragment.class.getSimpleName();
    private Context mContext;
    private Album[] mNewReleases;
    private RecyclerView mRecentRecyclerView;
    private RecentAdapter mRecentAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ImageView mSearch;
    private AutoCompleteTextView mSearchText;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        View rootView = inflater.inflate(R.layout.fragment_home , container , false);
        mRecentRecyclerView = rootView.findViewById(R.id.rv_new_releases);
        mSearchText = (AutoCompleteTextView) rootView.findViewById(R.id.et_searchtext);
        mSearchText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                Log.d("EditClick" , "Clicked Here ");
                if(event.getAction() == KeyEvent.ACTION_DOWN
                        || event.getAction() == KeyEvent.KEYCODE_ENTER){
                    Log.d("EditClick" , "Clicked Here ");
                    openSearchResult();
                    return true;
                }
                return false;
            }
        });
//        mSearchText.setOnEditorActionListener(new EditText.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent keyEvent) {
//                Log.d("EditClick" , "Clicked Here ");
//                if(actionId == EditorInfo.IME_ACTION_SEARCH
//                        || actionId == EditorInfo.IME_ACTION_DONE
//                        || keyEvent.getAction() == KeyEvent.ACTION_DOWN
//                        || keyEvent.getAction() == KeyEvent.KEYCODE_ENTER){
//                    Log.d("EditClick" , "Clicked Here ");
//                    openSearchResult();
//                    return true;
//                }
//                return false;
//            }
//        });
//        mSearchText.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });
        mSearch = (ImageView) rootView.findViewById(R.id.search_ic);
        mSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSearchResult();
            }
        });
        mRecentAdapter = new RecentAdapter(mContext, this);
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
        mRecentRecyclerView.setAdapter(mRecentAdapter);
        mRecentRecyclerView.setLayoutManager(mLayoutManager);
        new RecentQuery().execute();
        populateData();
        return rootView;
    }

    private void populateData() {
        if(mNewReleases != null){
            mRecentAdapter.setmAlbums(mNewReleases);
            setUserVisibleHint(true);
        }
    }

    public void setmContext(Context mContext) {
        this.mContext = mContext;
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

    @Override
    public void onClickItem(int position) {
        if(mNewReleases == null) return;
        String url = NetworkUtils.WEBSITE_URL + mNewReleases[position].getID();
        if(url != null) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(browserIntent);
        }
    }

    class RecentQuery extends AsyncTask<Void , Void , String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... voids) {
            String result = null;
            URL url = NetworkUtils.buildRecentDataUrl();
            Log.d("ParsedJson" , url.toString());
            try {
                result = NetworkUtils.getData(url);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(s != null) {
                Log.d("ParsedJson" , s);
                mNewReleases = JsonUtils.ParseNewReleasesAlbums(s);
                mRecentAdapter.setmAlbums(mNewReleases);

                Log.d("ParsedJson" , "Here" + mNewReleases[0].getName());
                Log.d("ParsedJson" , "Here" + mNewReleases[0].getImage());
            }else{
                Toast.makeText(mContext , "Null Data" , Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void openSearchResult(){
        String searchPharse = mSearchText.getText().toString();
        Intent intent = new Intent(mContext , ResultActivity.class);
        intent.putExtra(Intent.EXTRA_TEXT , searchPharse);
        startActivity(intent);
    }

}
