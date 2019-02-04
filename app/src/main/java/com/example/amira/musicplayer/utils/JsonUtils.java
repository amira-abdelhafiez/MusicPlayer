package com.example.amira.musicplayer.utils;

import android.util.Log;

import com.example.amira.musicplayer.models.Album;
import com.example.amira.musicplayer.models.Track;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Amira on 1/26/2019.
 */

public class JsonUtils {
    private static final String LOG_TAG = JsonUtils.class.getSimpleName();
    // New Releases Parsing
    private static final String ALBUMS = "albums";
    private static final String ITEMS = "items";
    private static final String ARTISTS = "artists";
    private static final String IMAGES = "images";
    private static final String NAME = "name";
    private static final String ALBUM_TYPE = "album_type";
    private static final String ID = "id";
    private static final String TYPE = "type";
    private static final String RELEASE_DATE = "release_date";
    private static final String IMAGE_URL = "url";

    // Search

    private static final String TRACKS = "tracks";
    private static final String TRACK_ID = "id";
    private static final String TRACK_NAME="name";
    private static final String POPULARITY = "popularity";
    private static final String ALBUM = "album";


    // Track

    private static final String PREVIEW_URL = "preview_url";

    public static Track[] ParseSearchAlbums(String jsonStr){
       Track[] parsedTrackList = null;

       if(jsonStr != null){
           try{
               JSONObject obj = new JSONObject(jsonStr);
               JSONObject tracks = obj.getJSONObject(TRACKS);
               JSONArray items = tracks.getJSONArray(ITEMS);

               int length = items.length();
               parsedTrackList = new Track[length];
               Track track;
               JSONObject itemJson , imageJson , artistJson , albumJson;
               JSONArray artistArr , imagesArr;
               List<String> artistsList;
               for(int i = 0 ; i < length ; i++){
                   track = new Track();
                   itemJson = new JSONObject(items.optString(i));
                   track.setId(itemJson.optString(TRACK_ID));
                   track.setName(itemJson.optString(TRACK_NAME));
                   track.setPopularity(itemJson.optInt(POPULARITY));
                   albumJson = new JSONObject(itemJson.optString(ALBUM));
                   track.setAlbumName(albumJson.optString(NAME));
                   imagesArr = new JSONArray(albumJson.optString(IMAGES));
                   imageJson = new JSONObject(imagesArr.optString(0));
                   track.setImage(imageJson.optString(IMAGE_URL));
                   artistArr = new JSONArray(itemJson.optString(ARTISTS));
                   artistsList = new ArrayList<>();
                   for(int j = 0 ; j < artistArr.length() ; j++){
                       artistJson = new JSONObject(artistArr.optString(j));
                       artistsList.add(artistJson.optString(NAME));
                   }
                   track.setArtists(artistsList);
                   parsedTrackList[i] = track;
               }

           }catch(JSONException ex){
               Log.d(LOG_TAG , "Exception " + ex.getMessage());
           }
       }else{
           Log.d(LOG_TAG , "Null Data");
       }
       return  parsedTrackList;
    }


    public static Album[] ParseNewReleasesAlbums(String jsonStr){
        Album[] parsedAlbumList = null;

        if(jsonStr != null){
            try{
                JSONObject obj = new JSONObject(jsonStr);
                JSONObject albums = obj.getJSONObject(ALBUMS);
                JSONArray items = albums.getJSONArray(ITEMS);
                int length = items.length();
                if(length > 0){
                    parsedAlbumList = new Album[length];
                    Album item;
                    JSONObject itemJson , imageJson , artistJson;
                    JSONArray imagesArr , artistArr;
                    Date date = null;
                    for(int i =0 ; i < length ; i++){
                        item = new Album();
                        itemJson = new JSONObject(items.optString(i));
                        item.setID(itemJson.optString(ID));
                        item.setAlbumType(itemJson.optString(ALBUM_TYPE));
                        item.setName(itemJson.optString(NAME));
                        Log.d("ParsedJson" , Integer.toString(i) + itemJson.optString(NAME));
                        try {
                            date = new SimpleDateFormat("yyyy-MM-dd").parse(itemJson.optString(RELEASE_DATE));
                        }catch (ParseException ex){
                            Log.d(LOG_TAG , "Date Parsing Exception");
                        }
                        item.setReleaseDate(date);
                        imagesArr = itemJson.getJSONArray(IMAGES);
                        artistArr = itemJson.getJSONArray(ARTISTS);
                        imageJson = new JSONObject(imagesArr.optString(0));
                        artistJson = new JSONObject(artistArr.optString(0));
                        item.setArtist(artistJson.optString(NAME));
                        item.setImage(imageJson.optString(IMAGE_URL));
                        parsedAlbumList[i] = item;
                    }
                }
            }catch (JSONException e){
                Log.d(LOG_TAG , e.getStackTrace().toString());
            }
        }else{
            Log.d(LOG_TAG ,"Null Json Data: New Releases");
        }

        return parsedAlbumList;
    }

    public static Track parseTrackObj(String jsonStr){
        Track parsedJsonObj = new Track();
         if(jsonStr != null){
             try{
                 JSONObject dataObj = new JSONObject(jsonStr);
                 parsedJsonObj.setId(dataObj.optString(TRACK_ID));
                 parsedJsonObj.setName(dataObj.optString(TRACK_NAME));
                 parsedJsonObj.setPopularity(dataObj.optInt(POPULARITY));
                 parsedJsonObj.setPreviewUrl(dataObj.optString(PREVIEW_URL));
                 JSONObject albumObj = new JSONObject(dataObj.optString(ALBUM));
                 parsedJsonObj.setAlbumName(albumObj.optString(NAME));
                 JSONArray artists = dataObj.getJSONArray(ARTISTS);
                 List<String> artList = new ArrayList<>();
                 JSONObject artist , image;
                 if(artists.length() > 0){
                     for(int i = 0 ; i < artists.length() ; i++){
                         artist = new JSONObject(artists.optString(i));
                         artList.add(artist.optString(NAME));
                     }
                 }
                 parsedJsonObj.setArtists(artList);
                 JSONArray images = albumObj.getJSONArray(IMAGES);
                 if(images.length() > 0) {
                     image = new JSONObject(images.optString(0));
                     parsedJsonObj.setImage(image.optString(IMAGE_URL));
                 }

             }catch (JSONException e){
                 Log.d(LOG_TAG , e.getMessage().toString());
             }
         }else{
             Log.d(LOG_TAG , "NULL DATA : GET TRACK ");
         }

         return  parsedJsonObj;
    }
}
