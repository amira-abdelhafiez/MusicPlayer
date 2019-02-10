package com.example.amira.musicplayer.data;

import android.provider.BaseColumns;

/**
 * Created by Amira on 2/2/2019.
 */

public class DataContract {
    public static final class FavoritesEntry implements BaseColumns {
        public static final String ID = "_id";
        public static final String NAME = "name";
        public static final String ARTIST = "artist";
        public static final String IMAGE = "image";
        public static final String URL = "url";
    }
    public static final class HistoryEntry implements BaseColumns {
        public static final String ID = "_id";
        public static final String SPOTIFY_ID = "spotifyId";
        public static final String NAME = "name";
        public static final String ARTIST = "artist";
        public static final String IMAGE = "image";
        public static final String URL = "url";
        public static final String DATE = "createdAt";
    }
}
