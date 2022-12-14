package com.example.photoalbums;

public class Album {
    int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Album(int id, String name) {
        this.id = id;
        this.name = name;
    }

    String name;
}
