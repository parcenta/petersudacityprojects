package com.peterark.popularmovies.popularmovies.detailPanel.ReviewsAdapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.provider.ContactsContract;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.peterark.popularmovies.popularmovies.R;
import com.peterark.popularmovies.popularmovies.databinding.ListItemMovieBinding;
import com.peterark.popularmovies.popularmovies.databinding.ListItemReviewBinding;
import com.peterark.popularmovies.popularmovies.models.MovieItem;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ReviewsAdapterViewHolder>{

    private final Context mContext;
    private List<ReviewItem> mItemList;

    public ReviewsAdapter(Context context){
        this.mContext = context;
    }


    public void setItemList(List<ReviewItem> itemList){
        this.mItemList = itemList;
        notifyDataSetChanged();
    }


    @Override
    public ReviewsAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context         = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        int layoutId  = R.layout.list_item_review;

        // ---------------------------------------------------------------------------------------------------------
        // Source: A friend who took Udacity Nanodegree before, recomend me this way to implement Databinding in the Adapter
        // ---------------------------------------------------------------------------------------------------------
        ListItemReviewBinding binding = DataBindingUtil.inflate(inflater,layoutId,parent,false);

        return new ReviewsAdapterViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(final ReviewsAdapterViewHolder holder, int position) {
        ReviewItem item = mItemList.get(position);

        holder.mBinding.reviewUserName.setText(item.userName);
        holder.mBinding.reviewUserCommentary.setText(item.reviewCommentary);

    }

    @Override
    public int getItemCount() {
        return mItemList != null ? mItemList.size() : 0;
    }

    public class ReviewsAdapterViewHolder extends RecyclerView.ViewHolder{

        // ---------------------------------------------------------------------------------------------------------
        // Source: A friend who took Udacity Nanodegree before, recomend me this way to implement Databinding in the Adapter
        // ---------------------------------------------------------------------------------------------------------
        ListItemReviewBinding mBinding;

        private ReviewsAdapterViewHolder(ListItemReviewBinding binding){
            super(binding.getRoot());
            mBinding = binding;
        }
    }
}
