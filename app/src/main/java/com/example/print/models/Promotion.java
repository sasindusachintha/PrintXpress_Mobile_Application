package com.example.print.models;

/**
 * Model class for promotions displayed on the Home screen.
 * Uses local drawable resource IDs instead of Base64.
 */
public class Promotion {
    private String id;
    private String title;
    private int imageResId;

    public Promotion() {}

    public Promotion(String id, String title, int imageResId) {
        this.id = id;
        this.title = title;
        this.imageResId = imageResId;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public int getImageResId() { return imageResId; }
    public void setImageResId(int imageResId) { this.imageResId = imageResId; }
}
