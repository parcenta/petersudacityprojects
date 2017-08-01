package com.peterark.popularmovies.popularmovies;

import android.content.Context;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.peterark.popularmovies.popularmovies.models.MovieItem;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by PETER on 26/7/2017.
 */

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MoviesAdapterViewHolder>{

    private Context mContext;

    private List<MovieItem> mItemList;

    public MoviesAdapter(Context context){
        this.mContext = context;
    }

    public void setItemList(List<MovieItem> itemList){
        this.mItemList = itemList;
        notifyDataSetChanged();
    }


    @Override
    public MoviesAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context         = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        int layoutId  = R.layout.list_item_movie;

        View view = inflater.inflate(layoutId, parent, false);
        return new MoviesAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MoviesAdapterViewHolder holder, int position) {
        MovieItem item = mItemList.get(position);
        Picasso.with(mContext)
                .load(item.moviePosterUrl())
                .error(R.drawable.ic_loading_error)
                .into(holder.posterImageView);
    }

    @Override
    public int getItemCount() {
        return mItemList != null ? mItemList.size() : 0;
    }

    public class MoviesAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public ImageView posterImageView;

        public MoviesAdapterViewHolder(View view){
            super(view);

            // Get Layout items reference
            posterImageView = (ImageView) view.findViewById(R.id.poster_holder);

            // TODO: Give On Click Behaviour
            view.setOnClickListener(null);
        }

        @Override
        public void onClick(View v) {

        }
    }

}
