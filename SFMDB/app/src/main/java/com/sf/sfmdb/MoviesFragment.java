package com.sf.sfmdb;

/**
 * Created by aeppili on 11/23/16.
 * Fragment responsible for Lists (Search results + Favorites)
 * UI - Uses RecyclerView for lists
 */

 import android.content.Intent;
 import android.content.res.Configuration;
 import android.os.Bundle;
 import android.support.v4.app.Fragment;
 import android.support.v7.widget.LinearLayoutManager;
 import android.support.v7.widget.RecyclerView;
 import android.util.Log;
 import android.view.LayoutInflater;
 import android.view.View;
 import android.view.ViewGroup;;import com.sf.sfmdb.data.MovieData;

 import org.json.JSONObject;

 import java.util.ArrayList;
 import java.util.HashSet;
 import java.util.List;
 import java.util.Set;
 import java.util.concurrent.ExecutionException;

public class MoviesFragment extends Fragment {
        static final String TAG = "MoviesFragment";
        static final String FAV_PARAM = "favoriteView";
        private List<MovieData> movieData;
        private static List<MovieData> favMovieList = new ArrayList<>(); // Hack to update Fav list, data needs to persisted + retrieved from SQLLite DB
        MovieAdapter adapter;
        boolean favoriteMoviesView = false;

        public MoviesFragment() {
            // Required empty public constructor
        }

        public void updateData(List<MovieData> _data) {
            movieData = _data;
            Log.d(TAG, "Populate lists with updated data ");
            if(adapter != null) {
                adapter.updateData(movieData);
            }
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            // retain this fragment
            setRetainInstance(true);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            // Inflate the layout for this fragment
            View rootView = inflater.inflate(R.layout.movie_list, container, false);

            RecyclerView rv = (RecyclerView) rootView.findViewById(R.id.rv_recycler_view);
            rv.setHasFixedSize(true);
            favoriteMoviesView = getArguments().getBoolean(FAV_PARAM, false);
            adapter = new MovieAdapter(null, favoriteMoviesView);
            rv.setAdapter(adapter);

            LinearLayoutManager llm = new LinearLayoutManager(getActivity());
            rv.setLayoutManager(llm);

            return rootView;
        }

    /**
     * Method to create instance with params
     * @param favoriteMoviesView flag to differential between Search Results vs Favorites
     * @return
     */
    public static MoviesFragment newInstanceWithParams(boolean favoriteMoviesView) {
            MoviesFragment mFragment = new MoviesFragment();

            Bundle args = new Bundle();
            args.putBoolean(FAV_PARAM, favoriteMoviesView);
            mFragment.setArguments(args);

            return mFragment;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        populateViewForOrientation(inflater, (ViewGroup) getView());
    }

    private void populateViewForOrientation(LayoutInflater inflater, ViewGroup viewGroup) {
        View view = onCreateView(getActivity().getLayoutInflater(), viewGroup, null); viewGroup.addView(view);
    }

    /**
     * Below methods are some Hacks to update Fav. movies, better approach will be to use Observer pattern .
     * @param movieData
     */
    static void addToFavorites(MovieData movieData) {
        favMovieList.add(movieData);
    }

    static void removeFromFavorites(MovieData movieData) {
        favMovieList.remove(movieData);
    }

    static List<MovieData> getFavMovieList() {
        return MoviesFragment.favMovieList;
    }
}
