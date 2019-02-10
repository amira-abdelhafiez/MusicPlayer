package com.example.amira.musicplayer.models;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.view.View;

import java.util.List;

/**
 * Created by Amira on 2/11/2019.
 */

public class FavoriteViewModel extends ViewModel {

    private LiveData<List<History>> HistoryList;

    public FavoriteViewModel(){

    }

}
