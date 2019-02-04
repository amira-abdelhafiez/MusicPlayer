package com.example.amira.musicplayer.ui;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.example.amira.musicplayer.R;
import com.example.amira.musicplayer.adapters.RecentAdapter;
import com.example.amira.musicplayer.adapters.SearchAdapter;
import com.example.amira.musicplayer.models.Album;
import com.example.amira.musicplayer.models.Track;
import com.example.amira.musicplayer.utils.JsonUtils;
import com.example.amira.musicplayer.utils.NetworkUtils;

import java.io.IOException;
import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ResultActivity extends AppCompatActivity implements SearchAdapter.ItemOnClickHandler{

    private static final String LOG_TAG = ResultActivity.class.getSimpleName();
    private String mSearchPhrase;

    @BindView(R.id.rv_search) RecyclerView mSearchRecyclerView;
    private SearchAdapter mSearchAdapter;
    private RecyclerView.LayoutManager mLinearLayoutManager;
    private Track[] mAlbums;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        Intent callingIntent = getIntent();
        if(callingIntent != null && callingIntent.hasExtra(Intent.EXTRA_TEXT)){
            mSearchPhrase = callingIntent.getStringExtra(Intent.EXTRA_TEXT);
        }
        ButterKnife.bind(this);
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
        }

        @Override
        protected String doInBackground(String... strings) {
            String result = null;
            String phrase = strings[0];
            URL url = NetworkUtils.buildSearchDataUrl(phrase);
            Log.d("ParsedJson" , url.toString());
            try {
                result = NetworkUtils.getData(url);
            } catch (IOException e) {
                Log.d("ParsedJson" , "Exxception" + e.getMessage());
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(s != null){
                mAlbums = JsonUtils.ParseSearchAlbums(s);
                //Log.d("ParsedJson" , Integer.toString(mAlbums.length));
                mSearchAdapter.setmAlbums(mAlbums);
            }else{
                Toast.makeText(ResultActivity.this , "Sorry Something went wrong" , Toast.LENGTH_SHORT).show();
            }
        }
    }
}
