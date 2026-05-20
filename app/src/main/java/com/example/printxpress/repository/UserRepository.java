package com.example.print.repository;

import androidx.lifecycle.MutableLiveData;
import com.example.print.firebase.RealtimeDatabaseHelper;
import com.example.print.models.User;
import com.google.firebase.auth.FirebaseAuth;

public class UserRepository {
    private final RealtimeDatabaseHelper dbHelper;
    private final MutableLiveData<User> currentUser = new MutableLiveData<>();

    public UserRepository() {
        this.dbHelper = RealtimeDatabaseHelper.getInstance();
    }

    public void registerUser(User user, String password, OnCompleteListener listener) {
        dbHelper.getAuth().createUserWithEmailAndPassword(user.getEmail(), password)
                .addOnSuccessListener(authResult -> {
                    String uid = authResult.getUser().getUid();
                    user.setId(uid);
                    // Store user in Realtime Database as per requirements
                    dbHelper.getUsersRef().child(uid).setValue(user)
                            .addOnSuccessListener(aVoid -> listener.onSuccess())
                            .addOnFailureListener(e -> listener.onFailure(e.getMessage()));
                })
                .addOnFailureListener(e -> listener.onFailure(e.getMessage()));
    }

    public interface OnCompleteListener {
        void onSuccess();
        void onFailure(String error);
    }
}
