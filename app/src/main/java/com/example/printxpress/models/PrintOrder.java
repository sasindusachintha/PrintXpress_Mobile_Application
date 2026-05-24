package com.example.print.models;

public class PrintOrder {
    private String orderId;
    private String userId;
    private String category;
    private String designName;
    private String description;
    private String quantity;
    private String notes;
    private String imageBase64;
    private String status;
    private long timestamp;
    private String rescheduleDate; // New field for rescheduling

    public PrintOrder() {
        // Default constructor required for calls to DataSnapshot.getValue(PrintOrder.class)
    }

    public PrintOrder(String orderId, String userId, String category, String designName,
                      String description, String quantity, String notes, String imageBase64,
                      String status, long timestamp) {
        this.orderId = orderId;
        this.userId = userId;
        this.category = category;
        this.designName = designName;
        this.description = description;
        this.quantity = quantity;
        this.notes = notes;
        this.imageBase64 = imageBase64;
        this.status = status;
        this.timestamp = timestamp;
    }

    // Getters and Setters
    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getDesignName() { return designName; }
    public void setDesignName(String designName) { this.designName = designName; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getQuantity() { return quantity; }
    public void setQuantity(String quantity) { this.quantity = quantity; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    public String getImageBase64() { return imageBase64; }
    public void setImageBase64(String imageBase64) { this.imageBase64 = imageBase64; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
    public String getRescheduleDate() { return rescheduleDate; }
    public void setRescheduleDate(String rescheduleDate) { this.rescheduleDate = rescheduleDate; }
}
