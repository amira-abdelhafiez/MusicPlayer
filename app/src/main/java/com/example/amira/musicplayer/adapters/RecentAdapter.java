package com.example.amira.musicplayer.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.amira.musicplayer.R;
import com.example.amira.musicplayer.models.Album;
import com.squareup.picasso.Picasso;

/**
 * Created by Amira on 1/27/2019.
 */

public class RecentAdapter extends RecyclerView.Adapter<RecentAdapter.RecentAdapterViewHolder>{

    private static final String LOG_TAG = RecentAdapter.class.getSimpleName();
    private Context mContext;
    private Album[] mAlbums;

    public RecentAdapter(Context context){
        this.mContext = context;
    }

    public void setmAlbums(Album[] mAlbums) {
        this.mAlbums = mAlbums;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecentAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View view = layoutInflater.inflate(R.layout.rv_item_vertical , parent , false);
        RecentAdapterViewHolder vh = new RecentAdapterViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecentAdapterViewHolder holder, int position) {
        if(mAlbums == null || mAlbums[position] == null) return;
        Log.d("ParsedJson" , mAlbums[position].getName());
        holder.mAlbumName.setText(mAlbums[position].getName());
        if(mAlbums[position].getImage() != null){
            Picasso.with(mContext)
                    .load(mAlbums[position].getImage())
                    .error(R.drawable.noimage)
                    .into(holder.mAlbumImage);
        }else{
            Picasso.with(mContext)
                    .load(R.drawable.noimage)
                    .error(R.drawable.noimage)
                    .into(holder.mAlbumImage);
        }
    }

    @Override
    public int getItemCount() {
        if(mAlbums == null){
            return 0;
        }else{
            return mAlbums.length;
        }
    }

    public class RecentAdapterViewHolder extends RecyclerView.ViewHolder{
        ImageView mAlbumImage;
        TextView mAlbumName;
        public RecentAdapterViewHolder(View itemView) {
            super(itemView);
            mAlbumImage = (ImageView) itemView.findViewById(R.id.iv_item_image);
            mAlbumName = (TextView) itemView.findViewById(R.id.tv_item_name);
        }
    }
}
