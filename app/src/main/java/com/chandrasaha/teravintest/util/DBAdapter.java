package com.chandrasaha.teravintest.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.SQLException;
import android.util.Log;

import com.chandrasaha.teravintest.model.Movie;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Chandra Saha on 8/29/2017.
 */

public class DBAdapter {
    private static final String	DB_NAME		= "movie_db";
    private static final int	DB_VER		= 1;

    public static final String	TABLE_NAME	= "movie";
    public static final String	COL_ID		= "_id";
    public static final String	COL_NAME	= "name";
    public static final String	COL_DATE	= "date";

    private static final String	TAG			= "MovieDBAdapter";
    private DatabaseHelper		dbHelper;
    private SQLiteDatabase		db;

    private static final String	DB_CREATE	= "create table movie (_id integer primary key, name text not null, date text not null);";

    private final Context		context;

    private static class DatabaseHelper extends SQLiteOpenHelper
    {
        public DatabaseHelper(Context context)
        {
            // TODO Auto-generated constructor stub
            super(context, DB_NAME, null, DB_VER);
        }

        @Override
        public void onCreate(SQLiteDatabase db)
        {
            // TODO Auto-generated method stub
            db.execSQL(DB_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
        {
            // TODO Auto-generated method stub
            Log.d(TAG, "upgrade DB");
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);

        }

    }

    public DBAdapter(Context context)
    {
        this.context = context;
        // TODO Auto-generated constructor stub
    }

    public DBAdapter open() throws SQLException
    {
        dbHelper = new DatabaseHelper(context);
        db = dbHelper.getWritableDatabase();
        return this;
    }

    public void close()
    {
        dbHelper.close();
    }

    public void createMovie(Movie movie)
    {
        ContentValues val = new ContentValues();
        val.put(COL_NAME, movie.getTitle());
        val.put(COL_DATE, movie.getDate());
        db.insert(TABLE_NAME, null, val);
    }

    public boolean deleteMovie(int id)
    {
        return db.delete(TABLE_NAME, COL_ID + "=" + id, null) > 0;
    }

    public void deleteAllMovie(){
        db.execSQL("delete from "+ TABLE_NAME);
    }

    public List<Movie> getAllMovie()
    {
        List<Movie> movieList = new ArrayList<Movie>();

        Cursor cursor = db.query(TABLE_NAME, new String[]
                {COL_ID, COL_NAME, COL_DATE
                }, null, null, null, null, null);
        while (cursor.moveToNext()) {

            int index = cursor.getColumnIndex(COL_ID);
            int index2 = cursor.getColumnIndex(COL_NAME);
            int index3 = cursor.getColumnIndex(COL_DATE);

            Movie movie = new Movie();
            movie.setTitle(cursor.getString(index2));
            movie.setDate(cursor.getString(index3));

            movieList.add(movie);

        }
        cursor.close();
        return movieList;
    }


}
