package com.peterark.popularmovies.popularmovies.detailPanel.ReviewsAdapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.peterark.popularmovies.popularmovies.R;
import com.peterark.popularmovies.popularmovies.databinding.ListItemReviewBinding;

import java.util.List;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ReviewsAdapterViewHolder>{

    private List<ReviewItem> mItemList;

    public ReviewsAdapter(){
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
        holder.mBinding.reviewUserNameBadge.setText(item.userNameFirstLetter);
    }

    @Override
    public int getItemCount() {
        return mItemList != null ? mItemList.size() : 0;
    }

    public class ReviewsAdapterViewHolder extends RecyclerView.ViewHolder{

        // ---------------------------------------------------------------------------------------------------------
        // Source: A friend who took Udacity Nanodegree before, recomend me this way to implement Databinding in the Adapter
        // He recomend me this link https://medium.com/google-developers/android-data-binding-recyclerview-db7c40d9f0e4
        // ---------------------------------------------------------------------------------------------------------
        ListItemReviewBinding mBinding;

        private ReviewsAdapterViewHolder(ListItemReviewBinding binding){
            super(binding.getRoot());
            mBinding = binding;
        }
    }
}
