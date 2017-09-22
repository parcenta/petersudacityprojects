package com.peterark.popularmovies.popularmovies.detailPanel.VideosAdapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.peterark.popularmovies.popularmovies.R;
import com.peterark.popularmovies.popularmovies.databinding.ListItemMovieBinding;
import com.peterark.popularmovies.popularmovies.databinding.ListItemVideoBinding;
import com.peterark.popularmovies.popularmovies.models.MovieItem;
import com.squareup.picasso.Picasso;

import java.util.List;

public class VideosAdapter extends RecyclerView.Adapter<VideosAdapter.VideosAdapterViewHolder>{

    private final Context mContext;
    private final OnVideoClickHandler mOnClickHandler;
    private List<VideoItem> mItemList;

    public VideosAdapter(Context context, OnVideoClickHandler handler ){
        this.mContext = context;
        this.mOnClickHandler = handler;
    }

    public interface OnVideoClickHandler{
        void onMovieClick(VideoItem item);
    }


    public void setItemList(List<VideoItem> itemList){
        this.mItemList = itemList;
        notifyDataSetChanged();
    }


    @Override
    public VideosAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context         = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        int layoutId  = R.layout.list_item_video;

        // ---------------------------------------------------------------------------------------------------------
        // Source: A friend who took Udacity Nanodegree before, recomend me this way to implement Databinding in the Adapter
        // ---------------------------------------------------------------------------------------------------------
        ListItemVideoBinding binding = DataBindingUtil.inflate(inflater,layoutId,parent,false);

        return new VideosAdapterViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(final VideosAdapterViewHolder holder, int position) {
        VideoItem item = mItemList.get(position);

        holder.mBinding.videoThumbnailTitle.setText(item.videoTitle);

        /*Picasso.with(mContext)
                .load(item.videoUrl)                    // Loading ImageUrl
                .placeholder(R.drawable.ic_image_placeholder)   // PlaceHolder Image (until loading finishes)
                .error(R.drawable.ic_loading_error)             // Error Image (if loading fails)
                .into(holder.mBinding.video);*/

    }

    @Override
    public int getItemCount() {
        return mItemList != null ? mItemList.size() : 0;
    }

    public class VideosAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        // ---------------------------------------------------------------------------------------------------------
        // Source: A friend who took Udacity Nanodegree before, recomend me this way to implement Databinding in the Adapter
        // ---------------------------------------------------------------------------------------------------------
        ListItemVideoBinding mBinding;

        private VideosAdapterViewHolder(ListItemVideoBinding binding){
            super(binding.getRoot());
            mBinding = binding;
            mBinding.getRoot().setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            VideoItem item = mItemList.get(adapterPosition);
            mOnClickHandler.onMovieClick(item);
        }
    }

}