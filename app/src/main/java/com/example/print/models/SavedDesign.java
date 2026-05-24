package com.example.print.models;

public class SavedDesign {
    private String id;
    private String name;
    private String imageBase64;
    private long timestamp;

    public SavedDesign() {}

    public SavedDesign(String id, String name, String imageBase64, long timestamp) {
        this.id = id;
        this.name = name;
        this.imageBase64 = imageBase64;
        this.timestamp = timestamp;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getImageBase64() { return imageBase64; }
    public void setImageBase64(String imageBase64) { this.imageBase64 = imageBase64; }
    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
}
