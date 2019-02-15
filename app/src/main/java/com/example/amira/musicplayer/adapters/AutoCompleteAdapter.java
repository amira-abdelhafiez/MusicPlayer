package com.example.amira.musicplayer.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Filterable;
import android.widget.TextView;

import com.example.amira.musicplayer.R;

import java.util.List;

/**
 * Created by Amira on 2/2/2019.
 */

public class AutoCompleteAdapter extends BaseAdapter {
    private List<String> mSearchSuggestions;
    private Context mContext;

    public AutoCompleteAdapter(Context context){
        mContext = context;
    }

    public void setmSearchSuggestions(List<String> mSearchSuggestions) {
        this.mSearchSuggestions = mSearchSuggestions;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if(mSearchSuggestions == null) return 0;
        else return mSearchSuggestions.size();
    }

    @Override
    public Object getItem(int position) {
        return mSearchSuggestions.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View ListItem = convertView;
        if(ListItem == null){
            ListItem = LayoutInflater.from(mContext).inflate(R.layout.search_item , parent , false);

        }
        TextView searchTextView = ListItem.findViewById(R.id.tv_search_suggest);
        searchTextView.setText(mSearchSuggestions.get(position));
        return ListItem;
    }
}
