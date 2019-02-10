package com.example.amira.musicplayer.models;

import android.support.annotation.Nullable;

import java.util.Date;

/**
 * Created by Amira on 1/25/2019.
 */

public class Album {
    private String ID;
    private String Name;
    private String AlbumType;
    private String Artist;
    private String Image;
    private Date ReleaseDate;
    private int Popularity;
    private String ExternalUrl;

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getID() {
        return ID;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getName() {
        return Name;
    }

    public void setAlbumType(String albumType) {
        AlbumType = albumType;
    }

    public String getAlbumType() {
        return AlbumType;
    }

    public void setArtist(String artist) {
        Artist = artist;
    }

    public String getArtist() {
        return Artist;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getImage() {
        return Image;
    }

    public void setReleaseDate(Date releaseDate) {
        ReleaseDate = releaseDate;
    }

    public Date getReleaseDate() {
        return ReleaseDate;
    }

    public void setPopularity(int popularity) {
        Popularity = popularity;
    }

    public int getPopularity() {
        return Popularity;
    }

    public void setExternalUrl(String externalUrl) {
        ExternalUrl = externalUrl;
    }

    public String getExternalUrl() {
        return ExternalUrl;
    }
}
