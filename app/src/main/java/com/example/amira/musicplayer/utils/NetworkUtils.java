package com.example.amira.musicplayer.utils;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Amira on 1/25/2019.
 */

public class NetworkUtils {

    private static final String LOG_TAG = NetworkUtils.class.getSimpleName();

    private static final String  BASE_URL = "https://api.spotify.com/v1/";
    public static final String WEBSITE_URL = "https://open.spotify.com/album/";

    private static final String TOKEN_END_POINT = "https://accounts.spotify.com/api/token";

    private static final String TOKEN_AUTh = "NzczN2I0ZmViOGE5NGE2MjllYWU0NDUzYjExOGE4OGU6ZjQwZDY5ZDRiZTFmNDFlNWE2ZDFlNzAxYmVkYTI3OTQ=";

    private static final String TOKEN_HEADER = "Basic " + TOKEN_AUTh;

    private static final String GRANT_TYPE_PARAM = "grant_type";
    private static final String GRANT_TYPE_VALUE = "client_credentials";


    private static String sToken  = "";

    private static String ACCESS_TOKEN = "Bearer " + sToken;


    private static final String RETURN_URL = "http://musicplayer.com/callback";
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String SEARCH_PATH =  "search";
    private static final String BROWSE = "browse";
    private static final String MOST_RECENT = "new-releases";
    private static final String TRACK_PATH = "tracks";

    private static final String TYPE_PARAM = "type";
    private static final String LIMIT_PARAM = "limit";
    private static final String TYPE_PARAM_VAL = "track";
    private static final String QUERY_PARAM = "q";
    private static final String TRACK_ID_PARAM = "id";

    public static URL buildRecentDataUrl(){
        Uri recentDataUri = Uri.parse(BASE_URL).buildUpon()
                .appendPath(BROWSE)
                .appendPath(MOST_RECENT)
                .appendQueryParameter(LIMIT_PARAM , "40")
                .build();

        URL url = null;
        try{
            url = new URL(recentDataUri.toString());
        }catch (MalformedURLException e){
            Log.d(LOG_TAG , e.getStackTrace().toString());
        }

        return url;
    }
    public static URL buildSearchDataUrl(String queryPhrase , String limit){
        Uri searchDataUri = Uri.parse(BASE_URL).buildUpon()
                .appendPath(SEARCH_PATH)
                .appendQueryParameter(QUERY_PARAM , queryPhrase)
                .appendQueryParameter(TYPE_PARAM , TYPE_PARAM_VAL)
                .appendQueryParameter(LIMIT_PARAM , limit)
                .build();

        URL url = null;
        try{
            url = new URL(searchDataUri.toString());
        }catch (MalformedURLException e){
            Log.d(LOG_TAG , e.getStackTrace().toString());
        }

        return url;
    }
    public static URL buildGetTrackURL(String trackId){
        Uri trackDataUri = Uri.parse(BASE_URL).buildUpon()
                .appendPath(TRACK_PATH)
                .appendPath(trackId)
                .build();

        URL url = null;
        try{
            url = new URL(trackDataUri.toString());
        }catch (MalformedURLException e){
            Log.d(LOG_TAG , e.getStackTrace().toString());
        }

        return url;
    }

    public static URL buildNewTokenURL(){
        Uri trackDataUri = Uri.parse(TOKEN_END_POINT).buildUpon()
                .appendQueryParameter(GRANT_TYPE_PARAM , GRANT_TYPE_VALUE)
                .build();

        URL url = null;
        try{
            url = new URL(trackDataUri.toString());
        }catch (MalformedURLException e){
            Log.d(LOG_TAG , e.getStackTrace().toString());
        }

        return url;
    }

    public static String getData(URL dataUrl , String token) throws IOException {
        String token_header = "Bearer " + token;
        HttpURLConnection con = (HttpURLConnection) dataUrl.openConnection();
        con.addRequestProperty(AUTHORIZATION_HEADER , token_header);
        con.addRequestProperty("Accept" , "application/json");
        con.addRequestProperty("Content-Type" , "application/json");

        Log.d("Status Code" , "The status is " + con.getResponseCode());

        try{
            InputStream in = con.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasNext = scanner.hasNext();
            if(hasNext){
                return scanner.next();
            }else{
                return null;
            }

        }catch(Exception e)
        {
            Log.d("ParsedJson" , e.getMessage());
            return null;
        }
        finally {
            con.disconnect();
        }
    }

    public static String getNewToken() throws IOException {
        URL dataUrl = buildNewTokenURL();
        Log.d("Amira" , dataUrl.toString());
        HttpURLConnection con = (HttpURLConnection) dataUrl.openConnection();
        con.addRequestProperty(AUTHORIZATION_HEADER , TOKEN_HEADER);
        con.addRequestProperty("Content-Type" , "application/x-www-form-urlencoded");
        con.setRequestMethod("POST");
        Log.d("Amira" , con.getResponseMessage());
        try{
            InputStream in = con.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasNext = scanner.hasNext();
            if(hasNext){
                return scanner.next();
            }else{
                return null;
            }

        }catch(Exception e)
        {
            Log.d("ParsedJson" , e.getMessage());
            Log.d("Amira" , e.getMessage());
            return null;
        }
        finally {
            con.disconnect();
        }
    }

    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            // Log exception
            return null;
        }
    }
}


