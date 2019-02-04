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
import android.widget.Filterable;

import java.util.List;

/**
 * Created by Amira on 2/2/2019.
 */

public class AutoCompleteAdapter extends ArrayAdapter<String>  {

    private AppCompatActivity activity;
    private List<String> names;
    private static LayoutInflater inflater = null;

    public AutoCompleteAdapter(AppCompatActivity a, int textViewResourceId, List<String> lst) {
        super(a, textViewResourceId, lst);
        activity = a;
        names = lst;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setNames(List<String> names) {
        this.names = names;
        notifyDataSetChanged();
    }

    public int getCount() {
        return names.size();
    }

    public long getItemId(int position) {
        return position;
    }

    public String getCurrentString(int position) {
        return names.get(position);
    }

    public void removeItem(int position) {
        names.remove(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View vi = convertView;
        return super.getView(position, convertView, parent);
    }
}
