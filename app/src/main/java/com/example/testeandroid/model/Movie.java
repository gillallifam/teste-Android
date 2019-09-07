package com.example.testeandroid.model;

import java.io.Serializable;

public class Movie implements Serializable {

    String title, id;

    public Movie(String id, String title) {
        this.title = title;
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public String getId() {
        return id;
    }
}

