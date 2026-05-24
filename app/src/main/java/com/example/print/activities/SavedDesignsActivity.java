package com.example.print.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.print.R;
import com.example.print.adapters.SavedDesignAdapter;
import com.example.print.firebase.RealtimeDatabaseHelper;
import com.example.print.models.SavedDesign;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class SavedDesignsActivity extends AppCompatActivity {

    private SavedDesignAdapter adapter;
    private List<SavedDesign> designList;
    private TextView tvEmpty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_designs);

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.saved_designs);
        }
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        tvEmpty = findViewById(R.id.tv_empty_designs);
        RecyclerView recyclerView = findViewById(R.id.rv_saved_designs);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        designList = new ArrayList<>();
        adapter = new SavedDesignAdapter(design -> {
            // Handle clicking a saved design (e.g., start a new order with it)
            Toast.makeText(this, "Selected: " + design.getName(), Toast.LENGTH_SHORT).show();
        });
        recyclerView.setAdapter(adapter);

        loadSavedDesigns();
    }

    private void loadSavedDesigns() {
        String uid = RealtimeDatabaseHelper.getInstance().getCurrentUserId();
        if (uid == null) return;

        RealtimeDatabaseHelper.getInstance().getUsersRef().child(uid).child("saved_designs")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        designList.clear();
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            SavedDesign design = ds.getValue(SavedDesign.class);
                            if (design != null) {
                                design.setId(ds.getKey());
                                designList.add(design);
                            }
                        }
                        adapter.setDesigns(designList);
                        tvEmpty.setVisibility(designList.isEmpty() ? View.VISIBLE : View.GONE);
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        Toast.makeText(SavedDesignsActivity.this, "Failed to load designs", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
