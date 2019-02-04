package com.example.amira.musicplayer.ui;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.amira.musicplayer.R;
import com.example.amira.musicplayer.models.Track;
import com.example.amira.musicplayer.utils.JsonUtils;
import com.example.amira.musicplayer.utils.NetworkUtils;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;

import java.io.IOException;
import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PlayerActivity extends AppCompatActivity {

    private static final String LOG_TAG = PlayerActivity.class.getSimpleName();

    private SimpleExoPlayer mExoPlayer;

    @BindView(R.id.exo_player_view)
    SimpleExoPlayerView mExoPlayerView;
    private String mTrackId;
    private Track mCurrentTrack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        Intent callingIntent = getIntent();

        if(callingIntent != null && callingIntent.hasExtra(Intent.EXTRA_TEXT)){
            mTrackId = callingIntent.getStringExtra(Intent.EXTRA_TEXT);
        }

        ButterKnife.bind(this);
        new TrackQuery().execute(mTrackId);
    }

    class TrackQuery extends AsyncTask<String , Void , String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            String id = strings[0];
            String result = null;
            if(id != null && !id.isEmpty()){
                URL url = NetworkUtils.buildGetTrackURL(id);
                try {
                    result = NetworkUtils.getData(url);
                }catch(IOException ex){
                    Log.d("ParsedJson" , ex.getMessage());
                }
            }else{
                Log.d("ParsedJson" , "Json Error");
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(s != null && !s.isEmpty()){
                mCurrentTrack = JsonUtils.parseTrackObj(s);
            }else{
                Toast.makeText(PlayerActivity.this , "Sorry Something went wrong" , Toast.LENGTH_SHORT).show();
            }
        }
    }
}
