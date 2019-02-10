package com.example.amira.musicplayer.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.amira.musicplayer.R;
import com.example.amira.musicplayer.models.Favorite;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Amira on 2/10/2019.
 */

public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.FavoriteItemViewHolder> {
    private static final String LOG_TAG = FavoritesAdapter.class.getSimpleName();
    private Context mContext;
    private ItemOnClickHandler handler;
    private List<Favorite> mFavorites;

    public  FavoritesAdapter (Context context , ItemOnClickHandler handler){
        this.mContext = context;
        this.handler = handler;
    }

    public void setmFavorites(List<Favorite> mFavorites) {
        this.mFavorites = mFavorites;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FavoriteItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View view = layoutInflater.inflate(R.layout.rv_item_horizontal , parent , false);
        FavoritesAdapter.FavoriteItemViewHolder vh = new FavoritesAdapter.FavoriteItemViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteItemViewHolder holder, int position) {
        holder.mAlbumName.setText(mFavorites.get(position).getName());
        String imageUrl = mFavorites.get(position).getImage();
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
    }

    @Override
    public int getItemCount() {
        if(mFavorites != null){
            return mFavorites.size();
        }else return 0;
    }

    class FavoriteItemViewHolder extends RecyclerView.ViewHolder{
        ImageView mAlbumImage;
        TextView mAlbumName ;

        public FavoriteItemViewHolder(View itemView) {
            super(itemView);
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
