package com.example.print.models;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String id;
    private String name;
    private String email;
    private String phone;
    private String profileImageBase64;
    private List<String> savedDesignsBase64 = new ArrayList<>();
    private List<String> deliveryAddresses = new ArrayList<>();

    public User() {}

    public User(String id, String name, String email, String phone) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getProfileImageBase64() { return profileImageBase64; }
    public void setProfileImageBase64(String profileImageBase64) { this.profileImageBase64 = profileImageBase64; }
    public List<String> getSavedDesignsBase64() { return savedDesignsBase64; }
    public void setSavedDesignsBase64(List<String> savedDesignsBase64) { this.savedDesignsBase64 = savedDesignsBase64; }
    public List<String> getDeliveryAddresses() { return deliveryAddresses; }
    public void setDeliveryAddresses(List<String> deliveryAddresses) { this.deliveryAddresses = deliveryAddresses; }
}
