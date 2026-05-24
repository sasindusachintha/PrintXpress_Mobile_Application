package com.example.print.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.print.R;
import java.util.ArrayList;
import java.util.List;

public class CategoryDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_detail);

        String name = getIntent().getStringExtra("CATEGORY_NAME");
        String pricing = getIntent().getStringExtra("CATEGORY_PRICING");
        String specs = getIntent().getStringExtra("CATEGORY_SPECS");
        int mainImage = getIntent().getIntExtra("CATEGORY_IMAGE", R.drawable.ic_placeholder_image);
        int[] sampleImages = getIntent().getIntArrayExtra("CATEGORY_SAMPLES");

        TextView tvName = findViewById(R.id.tv_category_name);
        TextView tvPricing = findViewById(R.id.tv_pricing);
        TextView tvSpecs = findViewById(R.id.tv_specs);
        ImageView ivMain = findViewById(R.id.iv_category_main);
        LinearLayout samplesContainer = findViewById(R.id.ll_samples_container);

        tvName.setText(name);
        tvPricing.setText(pricing);
        tvSpecs.setText(specs);
        ivMain.setImageResource(mainImage);

        if (sampleImages != null) {
            for (int resId : sampleImages) {
                ImageView iv = new ImageView(this);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(400, 400);
                params.setMargins(0, 0, 16, 0);
                iv.setLayoutParams(params);
                iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
                iv.setImageResource(resId);
                samplesContainer.addView(iv);
            }
        }

        findViewById(R.id.btn_upload_my_design).setOnClickListener(v -> {
            Intent intent = new Intent(this, DesignUploadActivity.class);
            intent.putExtra("CATEGORY_NAME", name);
            startActivity(intent);
        });
    }
}
