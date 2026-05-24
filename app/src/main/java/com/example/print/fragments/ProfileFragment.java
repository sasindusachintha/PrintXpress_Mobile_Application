package com.example.print.fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.print.R;
import com.example.print.activities.AuthActivity;
import com.example.print.activities.DeliveryAddressesActivity;
import com.example.print.activities.SavedDesignsActivity;
import com.example.print.activities.SupportActivity;
import com.example.print.firebase.RealtimeDatabaseHelper;
import com.example.print.models.User;
import com.example.print.utils.Base64Utils;
import com.example.print.utils.ValidationUtils;
import com.google.android.material.textfield.TextInputEditText;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ProfileFragment extends Fragment {

    private ImageView profileImage;
    private TextInputEditText etName, etPhone;
    private TextView tvEmail;
    private Button btnUpdate, btnLogout;
    private View btnSavedDesigns, btnAddresses, btnSupport;
    private String encodedProfileImage = "";

    private final ActivityResultLauncher<Intent> imagePickerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    Uri imageUri = result.getData().getData();
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), imageUri);
                        encodedProfileImage = Base64Utils.encode(bitmap);
                        profileImage.setImageBitmap(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
    );

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        profileImage = view.findViewById(R.id.profile_image);
        etName = view.findViewById(R.id.et_profile_name);
        etPhone = view.findViewById(R.id.et_profile_phone);
        tvEmail = view.findViewById(R.id.tv_profile_email);
        btnUpdate = view.findViewById(R.id.btn_update_profile);
        btnLogout = view.findViewById(R.id.btn_logout);

        btnSavedDesigns = view.findViewById(R.id.btn_saved_designs);
        btnAddresses = view.findViewById(R.id.btn_delivery_addresses);
        btnSupport = view.findViewById(R.id.btn_support);

        loadUserData();

        profileImage.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            imagePickerLauncher.launch(intent);
        });

        btnUpdate.setOnClickListener(v -> updateProfile());

        btnLogout.setOnClickListener(v -> {
            RealtimeDatabaseHelper.getInstance().getAuth().signOut();
            startActivity(new Intent(getActivity(), AuthActivity.class));
            requireActivity().finish();
        });

        btnSavedDesigns.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), SavedDesignsActivity.class));
        });

        btnAddresses.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), DeliveryAddressesActivity.class));
        });

        btnSupport.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), SupportActivity.class));
        });

        return view;
    }

    private void loadUserData() {
        String uid = RealtimeDatabaseHelper.getInstance().getCurrentUserId();
        if (uid != null) {
            RealtimeDatabaseHelper.getInstance().getUsersRef().child(uid).get()
                    .addOnSuccessListener(snapshot -> {
                        if (isAdded() && snapshot.exists()) {
                            User user = snapshot.getValue(User.class);
                            if (user != null) {
                                etName.setText(user.getName());
                                tvEmail.setText(user.getEmail());
                                etPhone.setText(user.getPhone());
                                if (user.getProfileImageBase64() != null && !user.getProfileImageBase64().isEmpty()) {
                                    profileImage.setImageBitmap(Base64Utils.decode(user.getProfileImageBase64()));
                                    encodedProfileImage = user.getProfileImageBase64();
                                }
                            }
                        }
                    });
        }
    }

    private void updateProfile() {
        String uid = RealtimeDatabaseHelper.getInstance().getCurrentUserId();
        if (uid == null) return;

        String newName = etName.getText().toString().trim();
        String newPhone = etPhone.getText().toString().trim();

        if (ValidationUtils.isEmpty(newName) || ValidationUtils.isEmpty(newPhone)) {
            Toast.makeText(getContext(), R.string.fill_all_fields, Toast.LENGTH_SHORT).show();
            return;
        }

        if (!ValidationUtils.isValidPhone(newPhone)) {
            etPhone.setError(getString(R.string.error_invalid_phone));
            return;
        }

        Map<String, Object> updates = new HashMap<>();
        updates.put("name", newName);
        updates.put("phone", newPhone);
        if (!encodedProfileImage.isEmpty()) {
            updates.put("profileImageBase64", encodedProfileImage);
        }

        RealtimeDatabaseHelper.getInstance().getUsersRef().child(uid).updateChildren(updates)
                .addOnSuccessListener(aVoid -> Toast.makeText(getContext(), R.string.profile_updated, Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(getContext(), getString(R.string.update_failed, e.getMessage()), Toast.LENGTH_SHORT).show());
    }
}
