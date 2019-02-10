package com.example.amira.musicplayer.models;

import android.arch.persistence.room.TypeConverter;

import java.util.Date;

/**
 * Created by Amira on 2/9/2019.
 */

public class DateConvertor {
    @TypeConverter
    public static Date toDate(Long timestamp){
        return timestamp == null ? null : new Date(timestamp);
    }

    @TypeConverter
    public static Long toTimestamp(Date date){
        return date == null ? null : date.getTime();
    }
}
