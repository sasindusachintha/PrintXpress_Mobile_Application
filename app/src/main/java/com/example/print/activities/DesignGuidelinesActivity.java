package com.example.print.activities;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.print.R;
import com.google.android.material.appbar.MaterialToolbar;

public class DesignGuidelinesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_design_guidelines);

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }
}
