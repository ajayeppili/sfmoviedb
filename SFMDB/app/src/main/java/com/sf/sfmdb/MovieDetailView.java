package com.sf.sfmdb;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.sf.sfmdb.data.MovieData;
import com.sf.sfmdb.data.MovieDataManager;
import com.sf.sfmdb.network.NetworkManager;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

/**
 * Created by aeppili on 11/23/16.
 */

public class MovieDetailView extends AppCompatActivity {
    final Activity activity = this;
    TextView mTitleView;
    ImageView mPosterView;
    TextView mDetailView;
    TextView mVotes;
    TextView mRating;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_detail_view);
        mTitleView = (TextView) findViewById(R.id.title);
        mPosterView = (ImageView) findViewById(R.id.poster);
        mDetailView = (TextView) findViewById(R.id.description);
        String imdbId = getIntent().getStringExtra("imdbId");
        MovieDataManager.searchMovieById(imdbId, new MovieDataManager.MovieSearchListener() {
            @Override
            public void onResponseReceived(JSONObject movieJSONData) {
                handleUIFromData(movieJSONData);
            }
        });


    }

    private void handleUIFromData(JSONObject movieJSONData) {
        MovieData data = MovieData.parseMovieJson(movieJSONData);
        if(data == null) {
            Snackbar.make(findViewById(android.R.id.content), "Unable to display movie information", Snackbar.LENGTH_LONG).show();
            finish();
        }
        if(data.getYear() != null) {
            mTitleView.setText(data.getTitle()+ " ("+data.getYear()+")");
        }

        try {
            String imageURL = data.getImageUrl();
            String cleanImageURL = Utils.decode(imageURL);
            Picasso.with(this).load(cleanImageURL).into(mPosterView);
        } catch (Exception e ){
            e.printStackTrace();
        }

        if(data.getPlot() != null) {
            mDetailView.setText(data.getPlot());
        }

        if(data.getRating() != null) {
            mRating.setText(data.getRating());
        }

        if(data.getVotes() != null) {
            mVotes.setText(data.getVotes());
        }
    }
}
