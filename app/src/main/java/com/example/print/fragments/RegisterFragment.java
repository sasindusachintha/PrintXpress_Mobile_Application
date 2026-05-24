package com.example.print.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.example.print.MainActivity;
import com.example.print.R;
import com.example.print.models.User;
import com.example.print.utils.ValidationUtils;
import com.example.print.viewmodels.AuthViewModel;
import com.google.android.material.textfield.TextInputEditText;

public class RegisterFragment extends Fragment {

    private AuthViewModel viewModel;
    private TextInputEditText etName, etEmail, etPhone, etPassword;
    private ProgressBar progressBar;
    private View registrationForm;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        etName = view.findViewById(R.id.et_reg_name);
        etEmail = view.findViewById(R.id.et_reg_email);
        etPhone = view.findViewById(R.id.et_reg_phone);
        etPassword = view.findViewById(R.id.et_reg_password);
        progressBar = view.findViewById(R.id.reg_progress);
        registrationForm = view.findViewById(R.id.registration_form);
        Button btnRegister = view.findViewById(R.id.btn_reg_submit);
        TextView tvGoToLogin = view.findViewById(R.id.tv_go_to_login);

        viewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        btnRegister.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String phone = etPhone.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (ValidationUtils.isEmpty(name) || ValidationUtils.isEmpty(email) ||
                ValidationUtils.isEmpty(phone) || ValidationUtils.isEmpty(password)) {
                Toast.makeText(getContext(), R.string.fill_all_fields, Toast.LENGTH_SHORT).show();
                return;
            }

            if (!ValidationUtils.isValidEmail(email)) {
                etEmail.setError(getString(R.string.error_invalid_email));
                return;
            }

            if (!ValidationUtils.isValidPhone(phone)) {
                etPhone.setError(getString(R.string.error_invalid_phone));
                return;
            }

            if (!ValidationUtils.isValidPassword(password)) {
                etPassword.setError(getString(R.string.error_invalid_password));
                return;
            }

            setLoading(true);
            User user = new User(null, name, email, phone);
            viewModel.register(user, password);
        });

        tvGoToLogin.setOnClickListener(v -> requireActivity().getSupportFragmentManager().popBackStack());

        viewModel.getAuthSuccess().observe(getViewLifecycleOwner(), success -> {
            if (success) {
                startActivity(new Intent(getActivity(), MainActivity.class));
                requireActivity().finish();
            }
        });

        viewModel.getAuthError().observe(getViewLifecycleOwner(), error -> {
            setLoading(false);
            Toast.makeText(getContext(), error, Toast.LENGTH_LONG).show();
        });

        return view;
    }

    private void setLoading(boolean isLoading) {
        if (progressBar != null) progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        if (registrationForm != null) registrationForm.setAlpha(isLoading ? 0.5f : 1.0f);
    }
}
