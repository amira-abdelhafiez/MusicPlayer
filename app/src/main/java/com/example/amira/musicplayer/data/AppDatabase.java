package com.example.amira.musicplayer.data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.example.amira.musicplayer.models.Favorite;
import com.example.amira.musicplayer.models.FavoriteDao;
import com.example.amira.musicplayer.models.History;
import com.example.amira.musicplayer.models.HistoryDao;

/**
 * Created by Amira on 2/2/2019.
 */

@Database(entities = {History.class , Favorite.class} , version = 1 , exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    private static final String LOG_TAG = AppDatabase.class.getSimpleName();

    private static final String DATABASE_NAME = "playerdb";
    private static final Object LOCK = new Object();
    private static AppDatabase sInstance;

    public static AppDatabase getsInstance(Context context){
        if(sInstance == null){
            synchronized (LOCK){
                sInstance = Room.databaseBuilder(context.getApplicationContext() ,
                        AppDatabase.class , AppDatabase.DATABASE_NAME )
                        .allowMainThreadQueries()
                        .build();
            }
        }
        return sInstance;
    }

    public abstract FavoriteDao favoriteDao();
    public abstract HistoryDao historyDao();
}
