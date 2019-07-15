package com.orientalmusic.music.entity;

/**
 * Created by kameloov on 12/10/2017.
 */

public class Order {
    private int userId;
    private int itemId;
    private int id;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
