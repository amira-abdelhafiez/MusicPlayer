package com.example.amira.musicplayer.models;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.database.Cursor;

import java.util.List;

/**
 * Created by Amira on 2/2/2019.
 */
@Dao
public interface HistoryDao {
    @Insert
    long insertHistory(History insertedHistory);

    @Query("SELECT * FROM history ORDER By createdAt DESC")
    LiveData<List<History>> getHistory();

    @Query("SELECT * FROM history WHERE _id = :Id")
    History getHistoryById(long Id);

    @Query("SELECT COUNT(*) FROM history")
    int count();
}
