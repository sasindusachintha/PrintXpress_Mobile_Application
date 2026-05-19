package com.example.print.firebase;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RealtimeDatabaseHelper {
    private static RealtimeDatabaseHelper instance;
    private final FirebaseAuth auth;
    private final DatabaseReference dbRef;

    private RealtimeDatabaseHelper() {
        auth = FirebaseAuth.getInstance();
        dbRef = FirebaseDatabase.getInstance().getReference();
    }

    public static synchronized RealtimeDatabaseHelper getInstance() {
        if (instance == null) {
            instance = new RealtimeDatabaseHelper();
        }
        return instance;
    }

    public FirebaseAuth getAuth() {
        return auth;
    }

    public DatabaseReference getUsersRef() {
        return dbRef.child("users");
    }

    public DatabaseReference getOrdersRef() {
        return dbRef.child("orders");
    }

    public String getCurrentUserId() {
        return auth.getCurrentUser() != null ? auth.getCurrentUser().getUid() : null;
    }
}
