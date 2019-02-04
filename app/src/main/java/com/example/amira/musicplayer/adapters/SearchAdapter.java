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
import com.example.amira.musicplayer.models.Track;
import com.squareup.picasso.Picasso;

/**
 * Created by Amira on 1/28/2019.
 */

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchAdapterViewHolder> {

    private static final String LOG_TAG = SearchAdapter.class.getSimpleName();
    private Context mContext;
    private Track[] mAlbums;
    private ItemOnClickHandler handler;

    public SearchAdapter(Context context , ItemOnClickHandler handler){
        mContext = context;
        this.handler = handler;
    }

    public void setmAlbums(Track[] mAlbums) {
        this.mAlbums = mAlbums;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SearchAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View view = layoutInflater.inflate(R.layout.rv_item_horizontal , parent , false);
        SearchAdapter.SearchAdapterViewHolder vh = new SearchAdapter.SearchAdapterViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull SearchAdapterViewHolder holder, int position) {
        if(mAlbums == null || mAlbums[position] == null) return;
        Log.d("ParsedJson" , mAlbums[position].getName());
        holder.mAlbumText.setText(mAlbums[position].getName());
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

    public class SearchAdapterViewHolder extends RecyclerView.ViewHolder{
        ImageView mAlbumImage;
        TextView mAlbumText;
        public SearchAdapterViewHolder(View itemView) {
            super(itemView);
            mAlbumImage = itemView.findViewById(R.id.iv_album_image);
            mAlbumText = itemView.findViewById(R.id.tv_album_name);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    handler.onClickItem(position);
                }
            });
        }
    }
    public interface ItemOnClickHandler {
        void onClickItem(int position);
    }
}
