package com.sf.sfmdb.data;

import android.support.annotation.NonNull;

import com.sf.sfmdb.RequestGenerator;
import com.sf.sfmdb.network.NetworkManager;
import com.sf.sfmdb.network.NetworkResponse;
import com.sf.sfmdb.network.NetworkResponseHandler;
import org.json.JSONObject;

/**
 * Class to provide APIs to query OMDB
 */

public class MovieDataManager {
    private static int rIndex = 0;
    public interface MovieSearchListener {
        void onResponseReceived(JSONObject movieDataList);
    }

    public static void getMovieByTitle(String searchString, @NonNull final MovieSearchListener searchListener) {
        if(searchString == null) {
            searchListener.onResponseReceived(null);

            return;
        }

        NetworkManager.volleyRequest(Integer.toString(rIndex++), RequestGenerator.generateSearchQueryByTitle(searchString), new NetworkResponseHandler() {
            @Override
            public void onResponseReceived(String taskId, NetworkResponse response) {
                if(response != null) {
                    JSONObject jsonObj = response.getResponseAsJSON();
                    searchListener.onResponseReceived(jsonObj);
                } else {
                    searchListener.onResponseReceived(null);
                }

            }
        });
    }

    public static void searchMovies(String searchString, @NonNull final MovieSearchListener searchListener) {
        if(searchString == null) {
            searchListener.onResponseReceived(null);
            return;
        }

        NetworkManager.volleyRequest(Integer.toString(rIndex++), RequestGenerator.generateSearchQuery(searchString), new NetworkResponseHandler() {
            @Override
            public void onResponseReceived(String taskId, NetworkResponse response) {
                if(response != null) {
                    JSONObject jsonObj = response.getResponseAsJSON();
                    searchListener.onResponseReceived(jsonObj);
                }

            }
        });
    }

    public static void searchMovieById(String imdbId, @NonNull final MovieSearchListener searchListener) {
        if(imdbId == null) {
            searchListener.onResponseReceived(null);

            return;
        }

        NetworkManager.volleyRequest(Integer.toString(rIndex++), RequestGenerator.generateSearchQueryById(imdbId), new NetworkResponseHandler() {
            @Override
            public void onResponseReceived(String taskId, NetworkResponse response) {
                if(response != null) {
                    JSONObject jsonObj = response.getResponseAsJSON();
                    searchListener.onResponseReceived(jsonObj);
                }

            }
        });
    }
}
