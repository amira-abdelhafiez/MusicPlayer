package com.example.amira.musicplayer.models;

import java.util.List;

/**
 * Created by Amira on 1/25/2019.
 */

public class Track {
    private String Id;
    private String Name;
    private List<String> Artists;
    private int Popularity;
    private String Image;
    private String AlbumName;
    private String PreviewUrl;

    public void setId(String id) {
        Id = id;
    }

    public String getId() {
        return Id;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getName() {
        return Name;
    }

    public void setPopularity(int popularity) {
        Popularity = popularity;
    }

    public int getPopularity() {
        return Popularity;
    }

    public void setArtists(List<String> artists) {
        Artists = artists;
    }

    public List<String> getArtists() {
        return Artists;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getImage() {
        return Image;
    }

    public void setAlbumName(String albumName) {
        AlbumName = albumName;
    }

    public String getAlbumName() {
        return AlbumName;
    }

    public void setPreviewUrl(String previewUrl) {
        PreviewUrl = previewUrl;
    }

    public String getPreviewUrl() {
        return PreviewUrl;
    }
}
