package com.example.amira.musicplayer.utils;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by Amira on 1/25/2019.
 */

public class NetworkUtils {

    private static final String LOG_TAG = NetworkUtils.class.getSimpleName();

    private static final String  BASE_URL = "https://api.spotify.com/v1/";
    private static final String ACCESS_TOKEN = "Bearer BQAlQmv_IDnMl4Vnx569P3K4fI0O4UklBEJlX36n6w1xxtJe6cuOHTZKS_mgUP67pGqlaSEtsFP36byBMGbdY_QHcbBiXhClJFyqLmbfEK7uCSyfJ7vfDIcd7LWzzxkJEaYMoqA5XQl1eHVu8dQxPm7kkrlKfBirCw";
    private static final String RETURN_URL = "http://musicplayer.com/callback";
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String SEARCH_PATH =  "search";
    private static final String BROWSE = "browse";
    private static final String MOST_RECENT = "new-releases";
    private static final String TRACK_PATH = "tracks";

    private static final String TYPE_PARAM = "type";
    private static final String TYPE_PARAM_VAL = "track";
    private static final String QUERY_PARAM = "q";
    private static final String TRACK_ID_PARAM = "id";

    public static URL buildRecentDataUrl(){
        Uri recentDataUri = Uri.parse(BASE_URL).buildUpon()
                .appendPath(BROWSE)
                .appendPath(MOST_RECENT)
                .build();

        URL url = null;
        try{
            url = new URL(recentDataUri.toString());
        }catch (MalformedURLException e){
            Log.d(LOG_TAG , e.getStackTrace().toString());
        }

        return url;
    }
    public static URL buildSearchDataUrl(String queryPhrase){
        Uri searchDataUri = Uri.parse(BASE_URL).buildUpon()
                .appendPath(SEARCH_PATH)
                .appendQueryParameter(QUERY_PARAM , queryPhrase)
                .appendQueryParameter(TYPE_PARAM , TYPE_PARAM_VAL)
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

    public static String getData(URL dataUrl) throws IOException {
        HttpURLConnection con = (HttpURLConnection) dataUrl.openConnection();
        con.addRequestProperty(AUTHORIZATION_HEADER , ACCESS_TOKEN);
        con.addRequestProperty("Accept" , "application/json");
        con.addRequestProperty("Content-Type" , "application/json");

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
}
