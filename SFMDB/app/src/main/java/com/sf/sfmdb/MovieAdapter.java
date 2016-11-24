package com.sf.sfmdb;

/**
 * Created by aeppili on 11/23/16.
 */

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sf.sfmdb.data.MovieData;
import com.squareup.picasso.Picasso;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {
    static final String TAG = "MovieAdater";
    private List<MovieData> movieList;
    boolean favListView = false;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public CardView mCardView;
        public TextView mTitleView;
        public TextView mDirector;
        public TextView mDescription;
        public ImageView mPostView;
        public ImageView mFavImage;
        boolean mFavListView; // boolean to add / delete to favorite list
        boolean mFavMovie; // will be used in Search Results list to toggle FAV / UNFAV

        final ViewHolderClickListener mListener;

        public ViewHolder(View v, boolean _favList, @NonNull final ViewHolderClickListener listener) {
            super(v);
            this.mFavListView = _favList;
            this.mFavMovie = _favList;
            mListener = listener;
            mCardView = (CardView) v.findViewById(R.id.card_view);
            mTitleView = (TextView) v.findViewById(R.id.mtitle);
            mDirector = (TextView) v.findViewById(R.id.mdirector);
            mDescription = (TextView) v.findViewById(R.id.mdescription);
            mPostView = (ImageView) v.findViewById(R.id.mimage);
            mFavImage = (ImageView) v.findViewById(R.id.toggleFavImg);
             addFavoriteIconListener();
            itemView.setOnClickListener(this);
        }

        void addFavoriteIconListener() {
            mFavImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(mFavMovie) {
                        //  remove from DB
                        // In fav list, Update UI - remove list item
                        mListener.onFavoriteIcon(view, getAdapterPosition(), false);
                        // In search List , toggle icon
                        mFavImage.setImageResource(R.drawable.favoriteadd);

                        mFavMovie = false;
                    } else {
                        // In search list, add to DB
                        mListener.onFavoriteIcon(view, getAdapterPosition(), true);
                        // Toggle Icon
                        mFavImage.setImageResource(R.drawable.favoriteblack);
                        mFavMovie = true;
                    }
                }
            });
        }

        @Override
        public void onClick(View view) {
            mListener.onViewClicked(view, getAdapterPosition());
        }

        public static interface ViewHolderClickListener {
            void onFavoriteIcon(View favIcon, int position, boolean addToFavorites);
            void onViewClicked(View view, int position);
        }

    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MovieAdapter(List<MovieData> dataSet, boolean favList) {
        movieList = dataSet;
        this.favListView = favList;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MovieAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_item_view, parent, false);

        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v, favListView, new ViewHolder.ViewHolderClickListener() {
            @Override
            public void onFavoriteIcon(View favIcon, int position, boolean addToFavList) {
                if(addToFavList) {
                    // In Search Results view
                    // Add To DB
                    addToFavorites(position);
                } else {
                    // In fav list, Update UI - remove list item
                    removeFromFavorites(position);
                }

            }

            @Override
            public void onViewClicked(View view, int position) {
                openDetailView(view.getContext(), position);
            }
        });
        return vh;
    }

    public void removeFromFavorites(int moviePosition) {
        if(moviePosition < movieList.size()) {
            MoviesFragment.removeFromFavorites(movieList.get(moviePosition));
            //movieList.remove(moviePosition);
            notifyDataSetChanged();
        }
    }

    public void addToFavorites(int moviePosition) {
        if(moviePosition < movieList.size()) {
            MoviesFragment.addToFavorites(movieList.get(moviePosition));
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        MovieData data = movieList.get(position);
        if(data.getYear()!= null) {
            holder.mTitleView.setText(data.getTitle() + "(" +data.getYear()+")");
        }
        holder.mDirector.setText(data.getDirector());
        holder.mDescription.setText(data.getPlot());
        try {
            String imageURL = data.getImageUrl();
            String cleanImageURL = Utils.decode(imageURL);
            Picasso.with(holder.mPostView.getContext()).load(cleanImageURL).into(holder.mPostView);
        } catch (Exception e ){
            holder.mPostView.setImageResource(R.drawable.defaultmovie);
            e.printStackTrace();
        }
        if(favListView) {
            holder.mFavImage.setImageResource(R.drawable.favoriteblack);
        } else {
            holder.mFavImage.setImageResource(R.drawable.favoriteadd);
        }


    }

    @Override
    public int getItemCount() {
        if(movieList == null) {
            return 0;
        }

        return movieList.size();
    }

    void updateData(List<MovieData> _datalist) {
        movieList = _datalist;
        notifyDataSetChanged();
    }

    void openDetailView(Context context, int position) {
        if(position<movieList.size()) {
            MovieData data = movieList.get(position);
            try {
                Intent detailIntent = new Intent(context, MovieDetailView.class);
                detailIntent.putExtra("imdbId", data.getImdbID());
                context.startActivity(detailIntent);
            } catch (Exception e){
                e.printStackTrace();
                Log.e(TAG, "Failed to open detail view ");
            }
        }
    }
}