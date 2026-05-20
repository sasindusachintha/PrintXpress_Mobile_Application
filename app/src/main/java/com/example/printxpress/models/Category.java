package com.example.print.models;

import java.util.List;

public class Category {
    private String name;
    private int imageResId;
    private String pricing;
    private String specs;
    private List<Integer> sampleImageResIds;

    public Category(String name, int imageResId) {
        this.name = name;
        this.imageResId = imageResId;
    }

    public Category(String name, int imageResId, String pricing, String specs, List<Integer> sampleImageResIds) {
        this.name = name;
        this.imageResId = imageResId;
        this.pricing = pricing;
        this.specs = specs;
        this.sampleImageResIds = sampleImageResIds;
    }

    public String getName() { return name; }
    public int getImageResId() { return imageResId; }
    public String getPricing() { return pricing; }
    public String getSpecs() { return specs; }
    public List<Integer> getSampleImageResIds() { return sampleImageResIds; }
}
