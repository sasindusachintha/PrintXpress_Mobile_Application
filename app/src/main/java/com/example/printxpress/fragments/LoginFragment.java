package com.example.print.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.example.print.MainActivity;
import com.example.print.R;
import com.example.print.activities.AuthActivity;
import com.example.print.utils.ValidationUtils;
import com.example.print.viewmodels.AuthViewModel;
import com.google.android.material.textfield.TextInputEditText;

public class LoginFragment extends Fragment {

    private AuthViewModel viewModel;
    private TextInputEditText etEmail, etPassword;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        etEmail = view.findViewById(R.id.et_login_email);
        etPassword = view.findViewById(R.id.et_login_password);
        Button btnLogin = view.findViewById(R.id.btn_login_submit);
        TextView tvGoToRegister = view.findViewById(R.id.tv_go_to_register);

        viewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        btnLogin.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (ValidationUtils.isEmpty(email) || ValidationUtils.isEmpty(password)) {
                Toast.makeText(getContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!ValidationUtils.isValidEmail(email)) {
                etEmail.setError("Invalid email address");
                return;
            }

            viewModel.login(email, password);
        });

        tvGoToRegister.setOnClickListener(v -> {
            ((AuthActivity) requireActivity()).switchFragment(new RegisterFragment());
        });

        viewModel.getAuthSuccess().observe(getViewLifecycleOwner(), success -> {
            if (success) {
                startActivity(new Intent(getActivity(), MainActivity.class));
                requireActivity().finish();
            }
        });

        viewModel.getAuthError().observe(getViewLifecycleOwner(), error -> {
            Toast.makeText(getContext(), error, Toast.LENGTH_LONG).show();
        });

        return view;
    }
}
