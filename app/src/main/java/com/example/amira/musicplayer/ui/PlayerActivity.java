package com.example.amira.musicplayer.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.amira.musicplayer.R;
import com.example.amira.musicplayer.data.AppDatabase;
import com.example.amira.musicplayer.models.Favorite;
import com.example.amira.musicplayer.models.History;
import com.example.amira.musicplayer.models.Track;
import com.example.amira.musicplayer.utils.JsonUtils;
import com.example.amira.musicplayer.utils.NetworkUtils;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PlayerActivity extends AppCompatActivity {

    private static final String LOG_TAG = PlayerActivity.class.getSimpleName();
    private static final String CURRENT_PLAYER_POSITION = "current_player_position";
    private static final String PLAY_IF_READY = "playwhenReady";
    private static final String CURRENT_TRACK_ID = "currentTrack";
    private long mCurrentPlayerPosition = 0;
    private boolean mPlayWhenReady = true;

    private Boolean IsFavorite = false;
    // Bundle
    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String IMAGE  = "image";
    private static final String ARTIST = "artist";
    private static final String URL = "url";

    private AppDatabase mDb;

    private SimpleExoPlayer mExoPlayer;

    @BindView(R.id.exo_player_view)
    SimpleExoPlayerView mExoPlayerView;
    @BindView(R.id.fab_add_to_fav)
    FloatingActionButton mFavFab;
    private String mTrackId;
    private Track mCurrentTrack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        setTitle("Playing ...");

        mDb = AppDatabase.getsInstance(this);

        Intent callingIntent = getIntent();

        if(callingIntent != null && callingIntent.hasExtra(Intent.EXTRA_TEXT)){
            mTrackId = callingIntent.getStringExtra(Intent.EXTRA_TEXT);
        }

        if(savedInstanceState != null){

            if(savedInstanceState.containsKey(CURRENT_PLAYER_POSITION)){
                mCurrentPlayerPosition = savedInstanceState.getLong(CURRENT_PLAYER_POSITION, 0);
            }
            if(savedInstanceState.containsKey(CURRENT_TRACK_ID)){
                mTrackId = savedInstanceState.getString(CURRENT_TRACK_ID);
            }
            if(savedInstanceState.containsKey(PLAY_IF_READY)){
                mPlayWhenReady = savedInstanceState.getBoolean(PLAY_IF_READY);
            }
        }
        ButterKnife.bind(this);
        mFavFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean flag = Boolean.valueOf(!IsFavorite);
                new AddRemoveFavQuery().execute(flag);
            }
        });

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
                populateData();
            }else{
                Toast.makeText(PlayerActivity.this , "Sorry Something went wrong" , Toast.LENGTH_SHORT).show();
            }
        }
    }

    class AddRemoveFavQuery extends AsyncTask<Boolean , Void , Boolean>{
        @Override
        protected Boolean doInBackground(Boolean... booleans) {
            Boolean operation = booleans[0];
            if(mCurrentTrack == null) return false;
            if(operation == true){
                // add

                StringBuilder artistName = new StringBuilder();
                for(int i = 0 ; i < mCurrentTrack.getArtists().size() ; i++){
                    artistName.append(mCurrentTrack.getArtists().get(i) + " ");
                    if(i != (mCurrentTrack.getArtists().size()-1)){
                        artistName.append(" , ");
                    }
                }
                Favorite favorite = new Favorite();
                favorite.setId(mCurrentTrack.getId());
                favorite.setName(mCurrentTrack.getName());
                favorite.setArtistName(artistName.toString());
                favorite.setImage(mCurrentTrack.getImage());
                favorite.setUrl(mCurrentTrack.getPreviewUrl());
                long addedId = mDb.favoriteDao().insertFavorite(favorite);
                Log.d("FavoriteQery" , "Added " + addedId);
               // mFavFab.setBackgroundDrawable(ContextCompat.getDrawable(PlayerActivity.this , R.drawable.heart_green));
                IsFavorite = true;
            }else{
                // Remove
                mDb.favoriteDao().deleteFavById(mCurrentTrack.getId());
                Log.d("FavoriteQery" , "Remove");
                //mFavFab.setBackgroundDrawable(ContextCompat.getDrawable(PlayerActivity.this , R.drawable.heart_white));
                IsFavorite = false;
            }
            return  true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            setFavImage(IsFavorite);
            Log.d("FavoriteQery" ,"Favorite " + IsFavorite.toString());
        }
    }

    private void setFavImage(boolean flag){
        if(flag){
            mFavFab.setImageDrawable(VectorDrawableCompat.create(getResources(), R.drawable.heart_green , null));
        }else{
            mFavFab.setImageDrawable(VectorDrawableCompat.create(getResources(), R.drawable.heart_white , null));
        }
    }

    class AddToHistoryQuery extends AsyncTask<Bundle , Void , Boolean>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Bundle... bundles) {
            Bundle bundle = bundles[0];
            History history = new History();
            history.setSpotifyId(bundle.getString(ID));
            history.setArtistName(bundle.getString(ARTIST));
            history.setName(bundle.getString(NAME));
            history.setImage(bundle.getString(IMAGE));
            history.setUrl(bundle.getString(URL));
            history.setCreatedAt(new Date());

            long insertedId = mDb.historyDao().insertHistory(history);

            if(insertedId > 0){
                Log.d(LOG_TAG , "inserted");
                return true;
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
        }
    }

    class CheckIsFavQuery extends AsyncTask<String , Void , Boolean>{

        @Override
        protected Boolean doInBackground(String... strings) {
            String id = strings[0];
            if(id == null) return false;
            Favorite favorite = mDb.favoriteDao().getFavById(id);
            if(favorite == null){
                return false;
            }else{
                return true;
            }
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if(aBoolean == false){
                Log.d(LOG_TAG , "Not Fav");

                mFavFab.setImageDrawable(VectorDrawableCompat.create(getResources(), R.drawable.heart_white , null));
                IsFavorite = false;
            }else{
                Log.d(LOG_TAG , "Is Fav");
                mFavFab.setImageDrawable(VectorDrawableCompat.create(getResources(), R.drawable.heart_green , null));
                //mFavFab.setBackgroundDrawable(ContextCompat.getDrawable(PlayerActivity.this , R.drawable.heart_green));
                IsFavorite = true;
            }
        }
    }

    class GetBitMapQuery extends AsyncTask<String , Void , Bitmap>{

        @Override
        protected Bitmap doInBackground(String... strings) {
            String imageUrl = strings[0];
            if(imageUrl != null){
                return NetworkUtils.getBitmapFromURL(imageUrl);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            if(bitmap != null){
                Log.d(LOG_TAG , "Bitmap Worked");
                mExoPlayerView.setDefaultArtwork(bitmap);
            }else{
                Log.d(LOG_TAG , "Bitmap no");
                mExoPlayerView.setDefaultArtwork(BitmapFactory.decodeResource(getResources() , R.drawable.noimage));
            }
        }
    }

    private void populateData(){
        setTitle(mCurrentTrack.getName());
        initializeExoPlayer();

        new GetBitMapQuery().execute(mCurrentTrack.getImage());

        new CheckIsFavQuery().execute(mCurrentTrack.getId());

        Bundle bundle = new Bundle();
        StringBuilder artistName = new StringBuilder();
        for(int i = 0 ; i < mCurrentTrack.getArtists().size() ; i++){
            artistName.append(mCurrentTrack.getArtists().get(i) + " ");
            if(i != (mCurrentTrack.getArtists().size()-1)){
                artistName.append(" , ");
            }
        }
        bundle.putString(ID , mCurrentTrack.getId());
        bundle.putString(ARTIST , artistName.toString());
        bundle.putString(IMAGE , mCurrentTrack.getImage());
        bundle.putString(NAME , mCurrentTrack.getName());
        bundle.putString(URL , mCurrentTrack.getPreviewUrl());

        new AddToHistoryQuery().execute(bundle);
    }
    private void initializeExoPlayer(){
        if(mExoPlayer == null) {
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(this,
                    new DefaultTrackSelector(), new DefaultLoadControl());
            preparePlayerMediaSource();
            mExoPlayer.setPlayWhenReady(mPlayWhenReady);
            //mExoPlayerView.setDefaultArtwork();
            mExoPlayerView.setPlayer(mExoPlayer);
        }else{
            preparePlayerMediaSource();
            mExoPlayer.setPlayWhenReady(mPlayWhenReady);
        }
    }

    private void preparePlayerMediaSource(){
        mExoPlayer.stop();

        Uri videoUri = Uri.parse(mCurrentTrack.getPreviewUrl());
        MediaSource mediaSource = buildMediaSource(videoUri);
        mExoPlayer.prepare(mediaSource);
        mExoPlayer.seekTo(mCurrentPlayerPosition);
    }

    private MediaSource buildMediaSource(Uri uri){
        String userAgent = Util.getUserAgent(this , "Music_Player");
        return new ExtractorMediaSource(uri , new DefaultHttpDataSourceFactory(userAgent)
                ,new DefaultExtractorsFactory() , null , null);
    }

    private void releasePlayer(){
        if(mExoPlayer == null) return;
        mExoPlayer.stop();
        mExoPlayer.release();
        mExoPlayer = null;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putLong(CURRENT_PLAYER_POSITION, mCurrentPlayerPosition);
        outState.putBoolean(PLAY_IF_READY , mPlayWhenReady);
        outState.putString(CURRENT_TRACK_ID , mTrackId);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onPause() {
        mCurrentPlayerPosition = mExoPlayer.getCurrentPosition();
        mPlayWhenReady = mExoPlayer.getPlayWhenReady();
        super.onPause();
        if (Util.SDK_INT <= 23) {
            // release player
            releasePlayer();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            // release player
            releasePlayer();
        }
    }

}
