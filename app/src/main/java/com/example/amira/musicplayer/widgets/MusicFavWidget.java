package com.example.amira.musicplayer.widgets;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

import com.example.amira.musicplayer.R;
import com.example.amira.musicplayer.models.Favorite;
import com.example.amira.musicplayer.ui.PlayerActivity;

import java.util.List;

/**
 * Implementation of App Widget functionality.
 */
public class MusicFavWidget extends AppWidgetProvider {

    private static List<Favorite> favoriteList;


    public static void setfavoriteList(List<Favorite> list){
        favoriteList = list;
    }

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.music_fav_widget);
        Intent intent = new Intent(context , PlayerActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context , 0 , intent , PendingIntent.FLAG_UPDATE_CURRENT );

        views.setPendingIntentTemplate(R.id.gv_widget , pendingIntent);

        Intent serviceIntent = new Intent(context , GridViewService.class);
        serviceIntent.setData(Uri.parse(serviceIntent.toUri(Intent.URI_INTENT_SCHEME)));
        views.setRemoteAdapter(R.id.gv_widget , serviceIntent);
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    public static void updateMusicFavWidget(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds){
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

