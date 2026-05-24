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
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        etEmail = view.findViewById(R.id.et_login_email);
        etPassword = view.findViewById(R.id.et_login_password);
        Button btnLogin = view.findViewById(R.id.btn_login_submit);
        TextView tvGoToRegister = view.findViewById(R.id.tv_go_to_register);

        viewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        btnLogin.setOnClickListener(v -> {
            if (etEmail.getText() == null || etPassword.getText() == null) return;
            
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (ValidationUtils.isEmpty(email) || ValidationUtils.isEmpty(password)) {
                Toast.makeText(requireContext(), R.string.fill_all_fields, Toast.LENGTH_SHORT).show();
                return;
            }

            if (!ValidationUtils.isValidEmail(email)) {
                etEmail.setError(getString(R.string.error_invalid_email));
                return;
            }

            viewModel.login(email, password);
        });

        tvGoToRegister.setOnClickListener(v -> {
            if (getActivity() instanceof AuthActivity) {
                ((AuthActivity) getActivity()).switchFragment(new RegisterFragment());
            }
        });

        viewModel.getAuthSuccess().observe(getViewLifecycleOwner(), (Boolean success) -> {
            if (Boolean.TRUE.equals(success)) {
                startActivity(new Intent(requireActivity(), MainActivity.class));
                requireActivity().finish();
            }
        });

        viewModel.getAuthError().observe(getViewLifecycleOwner(), (String error) -> {
            if (error != null) {
                Toast.makeText(requireContext(), error, Toast.LENGTH_LONG).show();
            }
        });
    }
}
