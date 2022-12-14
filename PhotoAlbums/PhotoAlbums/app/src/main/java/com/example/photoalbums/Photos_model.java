package com.example.photoalbums;

public class Photos_model {

    int id;
    int col_id;
    String img_path;
    String img_contentpath;
    String caption;
    String date;

    public String getImg_contentpath() {
        return img_contentpath;
    }

    public void setImg_contentpath(String img_contentpath) {
        this.img_contentpath = img_contentpath;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCol_id() {
        return col_id;
    }

    public void setCol_id(int col_id) {
        this.col_id = col_id;
    }

    public String getImg_path() {
        return img_path;
    }

    public void setImg_path(String img_path) {
        this.img_path = img_path;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Photos_model(int id, String img_name, String img_path, String caption, String image_loaddate, int album_id ) {
        this.id = id;
        this.img_contentpath=img_path;
        this.col_id = album_id;
        this.img_path = img_name;
        this.caption = caption;
        this.date = image_loaddate;
    }
}
