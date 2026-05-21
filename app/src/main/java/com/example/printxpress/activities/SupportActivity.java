package com.example.print.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.print.R;

public class SupportActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Help & Support");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        findViewById(R.id.btn_email_support).setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:support@printapp.com"));
            intent.putExtra(Intent.EXTRA_SUBJECT, "Design Related Query");
            startActivity(Intent.createChooser(intent, "Send Email"));
        });

        findViewById(R.id.btn_call_support).setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:+123456789"));
            startActivity(intent);
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
