package com.example.amira.musicplayer.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.amira.musicplayer.R;
import com.example.amira.musicplayer.data.DataContract;
import com.example.amira.musicplayer.models.History;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Amira on 2/10/2019.
 */

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryItemViewHolder> {

    private static final String LOG_TAG = HistoryAdapter.class.getSimpleName();
    private List<History> mHistory;
    private Context mContext;
    private ItemOnClickHandler handler;
    private int ID_COL , SPOTIFYID_COL , NAME_COL , IMAGE_COL , URL_COL , ARTIST_COL , DATE_COL;

    public HistoryAdapter(Context context , ItemOnClickHandler handler){
        mContext = context;
        this.handler = handler;
    }

    public void setmHistory(List<History> mHistory) {
        this.mHistory = mHistory;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public HistoryItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View view = layoutInflater.inflate(R.layout.rv_item_horizontal , parent , false);
        HistoryAdapter.HistoryItemViewHolder vh = new HistoryAdapter.HistoryItemViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryItemViewHolder holder, int position) {

        holder.mAlbumName.setText(mHistory.get(position).getName());
        String imageUrl = mHistory.get(position).getImage();
        if(imageUrl != null){
            Picasso.with(mContext)
                    .load(imageUrl)
                    .error(R.drawable.noimage)
                    .into(holder.mAlbumImage);
        }else{
            Picasso.with(mContext)
                    .load(R.drawable.noimage)
                    .error(R.drawable.noimage)
                    .into(holder.mAlbumImage);
        }
      //  holder.mAlbumDate.setText(mHistory.get(position).getCreatedAt().toString());
    }

    @Override
    public int getItemCount() {
        if(mHistory == null) return 0;
        else return mHistory.size();
    }

    class HistoryItemViewHolder extends RecyclerView.ViewHolder{
        ImageView mAlbumImage;
        TextView mAlbumName , mAlbumDate;
        public HistoryItemViewHolder(View itemView) {
            super(itemView);
            mAlbumDate = itemView.findViewById(R.id.tv_date);
            mAlbumDate.setVisibility(View.VISIBLE);
            mAlbumName = itemView.findViewById(R.id.tv_album_name);
            mAlbumImage = itemView.findViewById(R.id.iv_album_image);
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
