package com.example.print.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.print.models.User;
import com.example.print.repository.UserRepository;
import com.google.firebase.auth.FirebaseAuth;

public class AuthViewModel extends ViewModel {
    private final UserRepository repository;
    private final MutableLiveData<String> authError = new MutableLiveData<>();
    private final MutableLiveData<Boolean> authSuccess = new MutableLiveData<>();

    public AuthViewModel() {
        repository = new UserRepository();
    }

    public void login(String email, String password) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> authSuccess.setValue(true))
                .addOnFailureListener(e -> authError.setValue(e.getMessage()));
    }

    public void register(User user, String password) {
        repository.registerUser(user, password, new UserRepository.OnCompleteListener() {
            @Override
            public void onSuccess() {
                authSuccess.setValue(true);
            }

            @Override
            public void onFailure(String error) {
                authError.setValue(error);
            }
        });
    }

    public LiveData<Boolean> getAuthSuccess() { return authSuccess; }
    public LiveData<String> getAuthError() { return authError; }
}
