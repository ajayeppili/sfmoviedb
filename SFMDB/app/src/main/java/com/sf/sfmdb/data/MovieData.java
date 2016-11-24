package com.sf.sfmdb.data;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;

/**
 * Created by aeppili on 11/23/16.
 */

public class MovieData{

    private String year;
    private String director;
    private String imageUrl;
    private String plot;
    private String title;
    private String imdbID;
    private String jsonString;
    private String rating;
    private String votes;

    public MovieData(String title, String year, String director, String plot, String imageUrl, String imdbID) {
        this.title=title;
        this.year=year;
        this.director=director;
        this.plot=plot;
        this.imageUrl=imageUrl;
        this.imdbID = imdbID;

    }

    public MovieData() {

    }
    public static String TAG = "MovieData";

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getImdbID() {
        return imdbID;
    }

    public void setImdbID(String imdbID) {
        this.imdbID = imdbID;
    }

    public String getPlot() {
        return plot;
    }

    public void setPlot(String plot) {
        this.plot = plot;
    }

    public void setJsonString(String jsonString) {
        this.jsonString = jsonString;
    }

    public String getJsonString() {
        return this.jsonString;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getVotes() {
        return votes;
    }

    public void setVotes(String votes) {
        this.votes = votes;
    }


    /**
     * {"Title":"Godfather","Year":"1991","Rated":"N/A","Released":"N/A","Runtime":"150 min","Genre":"Comedy, Drama, Romance","Director":"Lal, Siddique","Writer":"Lal (screenplay), Siddique","Actors":"N.N. Pillai, Mukesh, Kanaka, Philomina","Plot":"Two youngsters from rival clans fall in love.","Language":"Malayalam","Country":"India","Awards":"2 wins.","Poster":"http://ia.media-imdb.com/images/M/MV5BZTkyYzc5MGEtYTBiYS00ZmYyLThlZWUtOWY3ZWE4ZDhlN2MzXkEyXkFqcGdeQXVyMjM0ODk5MDU@._V1_SX300.jpg","Metascore":"N/A","imdbRating":"8.5","imdbVotes":"905","imdbID":"tt0353496","Type":"movie","Response":"True"}
     * @param jsonObject JSON form of Movie data
     * @return Object with movie details
     */
    public static MovieData parseMovieJson(JSONObject jsonObject) {
        if(jsonObject == null) {
            return null;
        }
        MovieData movieData = new MovieData();
        try {
            if (jsonObject.has("Title")) {
                movieData.title = jsonObject.getString("Title");
            }
        } catch (Exception e) {
            movieData.title = "";
        }

        try {
            if (jsonObject.has("Year")) {
                movieData.year = jsonObject.getString("Year");
            }
        } catch (Exception e) {
            movieData.year = "";
        }
        try {
            if (jsonObject.has("Director")) {
                movieData.director = jsonObject.getString("Director");
            }
        } catch (Exception e) {
            movieData.director = "";
        }
        try {
            if (jsonObject.has("Plot")) {
                movieData.plot = jsonObject.getString("Plot");
            }
        } catch (Exception e) {
            movieData.plot = "";
        }

        try {
            if (jsonObject.has("Poster")) {
                movieData.imageUrl = jsonObject.getString("Poster");
            }
        } catch (Exception e) {
            movieData.imageUrl = "";
        }

        try {
            if (jsonObject.has("imdbID")) {
                movieData.imdbID = jsonObject.getString("imdbID");
            }
        } catch (Exception e) {
            movieData.imdbID = "";
        }

        try {
            if (jsonObject.has("imdbRating")) {
                movieData.rating = jsonObject.getString("imdbRating");
            }
        } catch (Exception e) {
            movieData.rating = "";
        }

        try {
            if (jsonObject.has("imdbVotes")) {
                movieData.votes = jsonObject.getString("imdbVotes");
            }
        } catch (Exception e) {
            movieData.votes = "";
        }

        movieData.jsonString=jsonObject.toString();
        return movieData;
    }

    /**
     * {"Title":"Godfather","Year":"1991","Rated":"N/A","Released":"N/A","Runtime":"150 min","Genre":"Comedy, Drama, Romance","Director":"Lal, Siddique","Writer":"Lal (screenplay), Siddique","Actors":"N.N. Pillai, Mukesh, Kanaka, Philomina","Plot":"Two youngsters from rival clans fall in love.","Language":"Malayalam","Country":"India","Awards":"2 wins.","Poster":"http://ia.media-imdb.com/images/M/MV5BZTkyYzc5MGEtYTBiYS00ZmYyLThlZWUtOWY3ZWE4ZDhlN2MzXkEyXkFqcGdeQXVyMjM0ODk5MDU@._V1_SX300.jpg","Metascore":"N/A","imdbRating":"8.5","imdbVotes":"905","imdbID":"tt0353496","Type":"movie","Response":"True"}
     * @param mJsonData Json form of Movie / list of movies
     * @return list of movies
     */
    public static List<MovieData> parseMovieListJson(JSONObject mJsonData) {
        if(mJsonData == null) {
            return null;
        }
        List<MovieData> responseList = new ArrayList<>();
        JSONArray jsonArray = null;
        try {
            if(mJsonData.has("Title")) {
                MovieData mMovieData = parseMovieJson(mJsonData);
                responseList.add(mMovieData);

            } else if(mJsonData.has("Search")){
                jsonArray = mJsonData.getJSONArray("Search");
                for(int i=0;i<jsonArray.length();i++) {
                    JSONObject tempObj = jsonArray.getJSONObject(i);
                    MovieData movieData = parseMovieJson(tempObj);
                    responseList.add(movieData);
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "");
        }

        return responseList;
    }
}
