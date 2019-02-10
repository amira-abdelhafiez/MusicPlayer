package com.example.amira.musicplayer.models;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.database.Cursor;

import java.util.List;

/**
 * Created by Amira on 2/2/2019.
 */

@Dao
public interface FavoriteDao {

    @Insert
    long insertFavorite(Favorite insertedFav);

    @Query("SELECT * FROM favorite")
    LiveData<List<Favorite>> getFavorites();

    @Query("SELECT * FROM favorite WHERE _id = :Id")
    LiveData<Favorite> getFavById(String Id);

    @Query("SELECT COUNT(*) FROM favorite")
    int count();

    @Query("DELETE FROM favorite WHERE _id = :id")
    void deleteFavById(String id);

}
