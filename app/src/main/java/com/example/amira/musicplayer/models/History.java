package com.example.amira.musicplayer.models;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.Date;

/**
 * Created by Amira on 2/2/2019.
 */
@Entity(tableName = "history")
public class History {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "_id")
    private long Id;
    @ColumnInfo(name = "spotifyId")
    private String spotifyId;
    @ColumnInfo(name = "name")
    private String Name;
    @ColumnInfo(name = "artist")
    private String ArtistName;
    @ColumnInfo(name = "image")
    private String Image;
    @ColumnInfo(name = "url")
    private String Url;
    @ColumnInfo(name = "createdAt")
    private Date CreatedAt;

    @Ignore
    public History(){

    }

    public History(long Id , String spotifyId , String Name , String ArtistName , String Image , String Url , Date CreatedAt){
        this.Id = Id;
        this.spotifyId = spotifyId;
        this.Name = Name;
        this.ArtistName = ArtistName;
        this.Image = Image;
        this.Url = Url;
        this.CreatedAt = CreatedAt;
    }

    public void setId(long id) {
        Id = id;
    }

    public long getId() {
        return Id;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getName() {
        return Name;
    }

    public void setSpotifyId(String spotifyId) {
        this.spotifyId = spotifyId;
    }

    public String getSpotifyId() {
        return spotifyId;
    }

    public void setArtistName(String artistName) {
        ArtistName = artistName;
    }

    public String getArtistName() {
        return ArtistName;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getImage() {
        return Image;
    }

    public void setUrl(String url) {
        Url = url;
    }

    public String getUrl() {
        return Url;
    }

    public void setCreatedAt(Date CreatedAt){ this.CreatedAt = CreatedAt;}

    public Date getCreatedAt(){
        return CreatedAt;
    }
}
