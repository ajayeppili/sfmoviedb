package com.sf.sfmdb.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by aeppili on 11/23/16.
 */

public class SQLHelper extends SQLiteOpenHelper {

        static final String FAV_MOVIES_TABLE = "favmovies";
        static final String COLUMN_ID = "_id";
        static final String COLUMN_MOVIE_TITLE = "title";
        static final String COLUMN_MOVIE_DIRECTOR = "director";
        static final String COLUMN_MOVIE_PLOT = "plot";
        static final String COLUMN_MOVIE_YEAR = "year";
        static final String COLUMN_MOVIE_POSTER = "poster";

        private static final String DATABASE_NAME = "moviesfmdb.db";
        private static final int DATABASE_VERSION = 1;

        private static final String DATABASE_CREATE = "create table " + FAV_MOVIES_TABLE
                + " ("

                + COLUMN_ID + " integer primary key autoincrement, "
                + COLUMN_MOVIE_TITLE + " text not null, "
                + COLUMN_MOVIE_DIRECTOR + " text not null, "
                + COLUMN_MOVIE_PLOT + " text not null, "
                + COLUMN_MOVIE_YEAR + " integer not null, "
                + COLUMN_MOVIE_POSTER + " integer not null, "
                + ");";

         SQLHelper(final Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            final List<MovieData> movieList = new ArrayList<>();

            sqLiteDatabase.execSQL(DATABASE_CREATE);
            sqLiteDatabase.beginTransaction();
            sqLiteDatabase.setTransactionSuccessful();
            sqLiteDatabase.endTransaction();
        }

        @Override
        public void onDowngrade(SQLiteDatabase database, int oldVersion, int newVersion) {
            Log.w(SQLHelper.class.getName(),
                    "Downgrading database from version " + oldVersion + " to "
                            + newVersion + ", which will destroy all old data"
            );
            recreateDb(database);
        }

        @Override
        public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
            Log.w(SQLHelper.class.getName(),
                    "Upgrading database from version " + oldVersion + " to "
                            + newVersion + ", which will destroy all old data"
            );
            recreateDb(database);
        }

        private void recreateDb(SQLiteDatabase database) {
            database.execSQL("DROP TABLE IF EXISTS " + FAV_MOVIES_TABLE);
            onCreate(database);
        }
    }

