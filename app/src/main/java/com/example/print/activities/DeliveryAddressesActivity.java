package com.example.print.activities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.print.R;
import com.example.print.adapters.AddressAdapter;
import com.example.print.firebase.RealtimeDatabaseHelper;
import com.example.print.models.Address;
import com.example.print.utils.ValidationUtils;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class DeliveryAddressesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AddressAdapter adapter;
    private List<Address> addressList;
    private TextView tvEmpty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_addresses);

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.delivery_addresses);
        }
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        tvEmpty = findViewById(R.id.tv_empty_addresses);
        recyclerView = findViewById(R.id.rv_addresses);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        addressList = new ArrayList<>();
        adapter = new AddressAdapter();
        recyclerView.setAdapter(adapter);

        loadAddresses();

        FloatingActionButton fab = findViewById(R.id.fab_add_address);
        fab.setOnClickListener(v -> showAddAddressDialog());
    }

    private void loadAddresses() {
        String uid = RealtimeDatabaseHelper.getInstance().getCurrentUserId();
        if (uid == null) return;

        RealtimeDatabaseHelper.getInstance().getUsersRef().child(uid).child("addresses")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        addressList.clear();
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            Address addr = ds.getValue(Address.class);
                            if (addr != null) addressList.add(addr);
                        }
                        adapter.setAddresses(addressList);
                        tvEmpty.setVisibility(addressList.isEmpty() ? View.VISIBLE : View.GONE);
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        Toast.makeText(DeliveryAddressesActivity.this, "Failed to load addresses", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void showAddAddressDialog() {
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_address, null);
        EditText etLabel = dialogView.findViewById(R.id.et_address_label);
        EditText etFull = dialogView.findViewById(R.id.et_full_address);

        new AlertDialog.Builder(this)
                .setTitle(R.string.add_new_address)
                .setView(dialogView)
                .setPositiveButton(R.string.add, (dialog, which) -> {
                    String label = etLabel.getText().toString().trim();
                    String full = etFull.getText().toString().trim();

                    if (ValidationUtils.isEmpty(label)) {
                        Toast.makeText(this, "Label is required", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (ValidationUtils.isEmpty(full)) {
                        Toast.makeText(this, "Address is required", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    saveAddress(label, full);
                })
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    private void saveAddress(String label, String full) {
        String uid = RealtimeDatabaseHelper.getInstance().getCurrentUserId();
        if (uid == null) return;

        String addrId = RealtimeDatabaseHelper.getInstance().getUsersRef().child(uid).child("addresses").push().getKey();
        Address address = new Address(addrId, label, full);
        if (addrId != null) {
            RealtimeDatabaseHelper.getInstance().getUsersRef().child(uid).child("addresses").child(addrId).setValue(address)
                    .addOnSuccessListener(aVoid -> Toast.makeText(this, "Address added!", Toast.LENGTH_SHORT).show());
        }
    }
}
