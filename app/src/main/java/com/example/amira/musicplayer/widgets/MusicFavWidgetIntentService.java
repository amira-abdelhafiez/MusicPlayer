package com.example.amira.musicplayer.widgets;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.example.amira.musicplayer.R;

/**
 * Created by Amira on 2/15/2019.
 */

public class MusicFavWidgetIntentService extends IntentService {

    private static final String NAME = "MusicFavWidgetIntentService";

    public static final String ACTION_UPDATE_WIDGET = "com.example.amira.musicplayer.action.update_widget";

    public MusicFavWidgetIntentService() {
        super(NAME);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if(intent != null){
            if(intent.getAction() == ACTION_UPDATE_WIDGET){
                handleUpdateWidgetAction();
            }
        }
    }

    private void handleUpdateWidgetAction(){
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetsIds = appWidgetManager.getAppWidgetIds(new ComponentName(this , MusicFavWidget.class));
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetsIds , R.id.gv_widget);
        MusicFavWidget.updateMusicFavWidget(this , appWidgetManager  ,appWidgetsIds);
    }
}
