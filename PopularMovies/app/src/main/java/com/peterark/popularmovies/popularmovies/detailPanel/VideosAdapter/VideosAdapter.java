package com.peterark.popularmovies.popularmovies.detailPanel.VideosAdapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.peterark.popularmovies.popularmovies.R;
import com.peterark.popularmovies.popularmovies.databinding.ListItemVideoBinding;
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
        void onVideoClick(VideoItem item);
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

        Picasso.with(mContext)
                .load(item.videoUrlThumbnailUrl)                    // Loading ImageUrl
                .placeholder(R.drawable.ic_play_arrow_white)   // PlaceHolder Image (until loading finishes)
                .error(R.drawable.ic_play_arrow_white)             // Error Image (if loading fails)
                .into(holder.mBinding.videoThumbnailImage);

    }

    @Override
    public int getItemCount() {
        return mItemList != null ? mItemList.size() : 0;
    }

    public class VideosAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        // -------------------------------------------------------------------------------------------------------------------
        // Source: A friend who took Udacity Nanodegree before, recomend me this way to implement Databinding in an Adapter
        // He recomend me this link https://medium.com/google-developers/android-data-binding-recyclerview-db7c40d9f0e4
        // -------------------------------------------------------------------------------------------------------------------
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
            mOnClickHandler.onVideoClick(item);
        }
    }

}
