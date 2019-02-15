package com.example.amira.musicplayer.models;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;
import android.view.View;

import com.example.amira.musicplayer.data.AppDatabase;

import java.util.List;

/**
 * Created by Amira on 2/11/2019.
 */

public class FavoriteViewModel extends AndroidViewModel {

    private FavoriteDao FavoriteDao;
    private LiveData<List<Favorite>> favs;


    public FavoriteViewModel(@NonNull Application application) {
        super(application);
        FavoriteDao = AppDatabase.getsInstance(application).favoriteDao();
        favs = FavoriteDao.getFavorites();
    }

    public void deleteFavorite(String ID){
        FavoriteDao.deleteFavById(ID);
    }

    public long addFavorite(Favorite favorite){
        return  FavoriteDao.insertFavorite(favorite);
    }

    public LiveData<List<Favorite>> GetAllFavs(){
        return  favs;
    }

    public List<Favorite> getFavoritesForWidget(){ return FavoriteDao.getFavoritesForWidget(); }

    public  Favorite getFavById(String ID){
        return FavoriteDao.getFavById(ID);
    }

    public int getFavCount(){
        return FavoriteDao.count();
    }
}
