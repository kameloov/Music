package com.orientalmusic.music.entity;

/**
 * Created by kameloov on 11/20/2017.
 */

public class SubCategory {
    private String name;
    private int id;
    private int category_id;
    private String image;
    private String details;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
