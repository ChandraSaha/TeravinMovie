package com.chandrasaha.teravintest.model;

/**
 * Created by Chandra Saha on 8/29/2017.
 */

public class Movie {
    String title;
    String date;
    int id;

    public Movie() {
    }

    public Movie(String title, String date) {
        super();
        this.title = title;
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
