package com.sf.sfmdb;

import android.support.annotation.NonNull;

import java.net.URLEncoder;

/**
 * Created by aeppili on 11/23/16.
 */

public class RequestGenerator {
    static final String API_URL = "http://www.omdbapi.com?r=json";
    static final String SEARCH_PARAM_KEY = "s="; ///?s=Batman&page=2";
    static final String TITLE_SEARCH_PARAM_KEY="t=";
    static final String ID_SEARCH_PARAM_KEY="i=";

    public static String generateSearchQuery(@NonNull final String searchString) {
        String spaceEncodedStr = null;
        try {
            spaceEncodedStr = URLEncoder.encode(searchString, "UTF-8");
        } catch (Exception e){
            return null;
        }
        return (new StringBuilder().append(API_URL).append("&").append("plot=short").append("&")
                                    .append(SEARCH_PARAM_KEY)
                                    .append(spaceEncodedStr)).toString();
    }

    public static String generateSearchQueryByTitle(@NonNull final String searchString) {
        String spaceEncodedStr = null;
        try {
            spaceEncodedStr = URLEncoder.encode(searchString, "UTF-8");
        } catch (Exception e){
            return null;
        }
        return (new StringBuilder().append(API_URL).append("&").append("plot=short").append("&")
                                    .append(TITLE_SEARCH_PARAM_KEY)
                                    .append(spaceEncodedStr)).toString();
    }

    public static String generateSearchQueryById(@NonNull final String searchString) {
        String spaceEncodedStr = null;
        try {
            spaceEncodedStr = URLEncoder.encode(searchString, "UTF-8");
        } catch (Exception e){
            return null;
        }
        return (new StringBuilder().append(API_URL).append("&").append("plot=long").append("&")
                                    .append(ID_SEARCH_PARAM_KEY)
                                    .append(spaceEncodedStr)).toString();
    }
}


