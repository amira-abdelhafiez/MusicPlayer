package com.example.amira.musicplayer.models;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

/**
 * Created by Amira on 2/2/2019.
 */

@Entity(tableName = "favorite")
public class Favorite {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "_id")
    private String Id;
    @ColumnInfo(name = "name")
    private String Name;
    @ColumnInfo(name = "artist")
    private String ArtistName;
    @ColumnInfo(name = "image")
    private String Image;
    @ColumnInfo(name = "url")
    private String Url;

    @Ignore
    private Favorite(){

    }

    public Favorite(String Id , String Name , String ArtistName , String Image , String Url){
        this.Id = Id;
        this.Name = Name;
        this.ArtistName = ArtistName;
        this.Image = Image;
        this.Url = Url;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getId() {
        return Id;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getImage() {
        return Image;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getName() {
        return Name;
    }

    public void setArtistName(String artistName) {
        ArtistName = artistName;
    }

    public String getArtistName() {
        return ArtistName;
    }

    public void setUrl(String url) {
        Url = url;
    }

    public String getUrl() {
        return Url;
    }
}
