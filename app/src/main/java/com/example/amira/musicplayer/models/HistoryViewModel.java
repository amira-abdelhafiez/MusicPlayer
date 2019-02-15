package com.example.amira.musicplayer.models;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.example.amira.musicplayer.data.AppDatabase;

import java.util.List;

/**
 * Created by Amira on 2/14/2019.
 */

public class HistoryViewModel extends AndroidViewModel {

    private HistoryDao historyDao;
    private LiveData<List<History>> history;

    public HistoryViewModel(@NonNull Application application) {
        super(application);
        historyDao = AppDatabase.getsInstance(application).historyDao();
        history = historyDao.getHistory();
    }

    public LiveData<List<History>> getHistory(){
        return history;
    }

    public long insertHistory(History history){
        return historyDao.insertHistory(history);
    }

    public History getHistoryById(long ID){
        return historyDao.getHistoryById(ID);
    }

    public int getHistoryCount(){
        return historyDao.count();
    }
}
