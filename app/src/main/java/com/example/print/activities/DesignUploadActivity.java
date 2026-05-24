package com.example.print.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.example.print.R;
import com.example.print.firebase.RealtimeDatabaseHelper;
import com.example.print.models.PrintOrder;
import com.example.print.utils.Base64Utils;
import com.example.print.utils.NotificationHelper;
import com.example.print.utils.ValidationUtils;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class DesignUploadActivity extends AppCompatActivity {

    private String categoryName;
    private String encodedImage = "";
    private ImageView ivDesignPreview;
    private TextInputEditText etDesignName, etDescription, etQuantity, etNotes;
    private CheckBox cbSaveDesign;
    private ProgressBar progressBar;
    private MaterialButton btnPlaceOrder;

    private final ActivityResultLauncher<Intent> galleryLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    Uri imageUri = result.getData().getData();
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                        ivDesignPreview.setImageBitmap(bitmap);
                        encodedImage = Base64Utils.encode(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(this, R.string.failed_load_image, Toast.LENGTH_SHORT).show();
                    }
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_design_upload);

        categoryName = getIntent().getStringExtra("CATEGORY_NAME");

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(getString(R.string.upload_title_format, categoryName));
        }
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        ivDesignPreview = findViewById(R.id.iv_design_preview);
        etDesignName = findViewById(R.id.et_design_name);
        etDescription = findViewById(R.id.et_description);
        etQuantity = findViewById(R.id.et_quantity);
        etNotes = findViewById(R.id.et_notes);
        cbSaveDesign = findViewById(R.id.cb_save_design);
        progressBar = findViewById(R.id.progress_bar);
        btnPlaceOrder = findViewById(R.id.btn_place_order);

        findViewById(R.id.btn_select_image).setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            galleryLauncher.launch(intent);
        });

        btnPlaceOrder.setOnClickListener(v -> placeOrder());
    }

    private void placeOrder() {
        String name = etDesignName.getText().toString().trim();
        String desc = etDescription.getText().toString().trim();
        String qty = etQuantity.getText().toString().trim();
        String notes = etNotes.getText().toString().trim();

        if (ValidationUtils.isEmpty(encodedImage)) {
            Toast.makeText(this, R.string.error_select_design, Toast.LENGTH_SHORT).show();
            return;
        }
        if (ValidationUtils.isEmpty(name)) {
            etDesignName.setError(getString(R.string.error_name_required));
            return;
        }
        if (!ValidationUtils.isValidQuantity(qty)) {
            etQuantity.setError(getString(R.string.error_invalid_quantity));
            return;
        }

        String userId = RealtimeDatabaseHelper.getInstance().getCurrentUserId();
        if (userId == null) {
            Toast.makeText(this, R.string.user_not_authenticated, Toast.LENGTH_SHORT).show();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        btnPlaceOrder.setEnabled(false);

        String orderId = RealtimeDatabaseHelper.getInstance().getOrdersRef().push().getKey();
        PrintOrder order = new PrintOrder(
                orderId,
                userId,
                categoryName,
                name,
                desc,
                qty,
                notes,
                encodedImage,
                "Pending",
                System.currentTimeMillis()
        );

        if (orderId != null) {
            RealtimeDatabaseHelper.getInstance().getOrdersRef().child(orderId).setValue(order)
                    .addOnSuccessListener(aVoid -> {
                        if (cbSaveDesign != null && cbSaveDesign.isChecked()) {
                            saveToLibrary(userId, name, encodedImage);
                        }
                        progressBar.setVisibility(View.GONE);
                        NotificationHelper.showNotification(this, getString(R.string.order_confirmed), getString(R.string.order_success_msg_format, categoryName));
                        showSuccessDialog();
                    })
                    .addOnFailureListener(e -> {
                        progressBar.setVisibility(View.GONE);
                        btnPlaceOrder.setEnabled(true);
                        Toast.makeText(this, getString(R.string.failed_place_order_format, e.getMessage()), Toast.LENGTH_SHORT).show();
                    });
        }
    }

    private void saveToLibrary(String userId, String name, String image) {
        Map<String, Object> design = new HashMap<>();
        design.put("name", name);
        design.put("imageBase64", image);
        design.put("timestamp", System.currentTimeMillis());

        RealtimeDatabaseHelper.getInstance().getUsersRef().child(userId).child("saved_designs").push().setValue(design);
    }

    private void showSuccessDialog() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.success)
                .setMessage(R.string.order_placed_success)
                .setPositiveButton(R.string.ok, (dialog, which) -> finish())
                .setCancelable(false)
                .show();
    }
}
