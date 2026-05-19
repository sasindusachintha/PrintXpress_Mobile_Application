package com.example.print.constants;

public class AppConstants {
    // Realtime Database Nodes (Active)
    public static final String NODE_USERS = "users";
    public static final String NODE_ORDERS = "orders";

    // Order Statuses
    public static final String STATUS_PENDING = "Pending";
    public static final String STATUS_PROCESSING = "Processing";
    public static final String STATUS_COMPLETED = "Completed";

    // Delivery Options
    public static final String OPTION_PICKUP = "Pickup";
    public static final String OPTION_DELIVERY = "Home Delivery";

    // Categories
    public static final String CAT_BUSINESS_CARDS = "Business Cards";
    public static final String CAT_POSTERS = "Posters";
    public static final String CAT_BANNERS = "Banners";
    public static final String CAT_FLYERS = "Flyers";
    public static final String CAT_STICKERS = "Stickers";
    public static final String CAT_MERCHANDISE = "Customized Merchandise";

    // Legacy Firestore Constants (Temporary to prevent build errors in trash files)
    public static final String COLLECTION_USERS = "users";
    public static final String COLLECTION_PRODUCTS = "products";
    public static final String COLLECTION_ORDERS = "orders";
    public static final String COLLECTION_PROMOTIONS = "promotions";
    public static final String COLLECTION_RESOURCES = "resources";
    public static final String STATUS_PRINTING = "Printing";
    public static final String STATUS_READY = "Ready for Pickup";
    public static final String STATUS_CANCELLED = "Cancelled";
}
