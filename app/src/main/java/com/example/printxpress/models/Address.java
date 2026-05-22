package com.example.print.models;

public class Address {
    private String id;
    private String label; // Home, Office, etc.
    private String fullAddress;

    public Address() {}

    public Address(String id, String label, String fullAddress) {
        this.id = id;
        this.label = label;
        this.fullAddress = fullAddress;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getLabel() { return label; }
    public void setLabel(String label) { this.label = label; }
    public String getFullAddress() { return fullAddress; }
    public void setFullAddress(String fullAddress) { this.fullAddress = fullAddress; }
}
