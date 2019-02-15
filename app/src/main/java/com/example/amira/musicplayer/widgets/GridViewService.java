package com.example.amira.musicplayer.widgets;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.amira.musicplayer.R;
import com.example.amira.musicplayer.models.Favorite;
import com.example.amira.musicplayer.models.FavoriteViewModel;

import java.util.List;

/**
 * Created by Amira on 2/15/2019.
 */

public class GridViewService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new GridViewFactory(getApplicationContext() , getApplication());
    }

    class GridViewFactory implements RemoteViewsFactory{

        private final String LOG_TAG = GridViewFactory.class.getSimpleName();
        private Context mContext;
        private List<Favorite> favoriteList;
        private FavoriteViewModel mFavVM;

        public  GridViewFactory (Context context , Application application){
            this.mContext = context;
            mFavVM = new FavoriteViewModel(application);
        }

        private void setFavoriteList(List<Favorite> favList){
            this.favoriteList = favList;
        }

        @Override
        public void onCreate() {
            setFavoriteList(mFavVM.getFavoritesForWidget());
        }

        @Override
        public void onDataSetChanged() {
            setFavoriteList(mFavVM.getFavoritesForWidget());
        }

        @Override
        public void onDestroy() {

        }

        @Override
        public int getCount() {
            if(favoriteList == null) return 0;
            else return favoriteList.size();
        }

        @Override
        public RemoteViews getViewAt(int position) {

            try{
                RemoteViews rv = new RemoteViews(mContext.getPackageName() , R.layout.widget_gv_item);
                rv.setTextViewText(R.id.widget_gv_item_text , favoriteList.get(position).getName());
                rv.setImageViewResource(R.id.widget_gv_item_image , R.drawable.play_widget);
                Intent fillInIntent = new Intent();
                fillInIntent.putExtra(Intent.EXTRA_TEXT , favoriteList.get(position).getId());
                rv.setOnClickFillInIntent(R.id.widget_gv_item_image , fillInIntent);
                rv.setOnClickFillInIntent(R.id.widget_gv_item_text , fillInIntent);
                return rv;
            }catch (Exception e){
                Log.d(LOG_TAG , "Widget Error " + e.getMessage());
            }
            return null;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }
}
