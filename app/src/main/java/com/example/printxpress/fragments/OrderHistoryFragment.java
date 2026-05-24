package com.example.print.fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.print.R;
import com.example.print.adapters.OrderAdapter;
import com.example.print.firebase.RealtimeDatabaseHelper;
import com.example.print.models.PrintOrder;
import com.example.print.utils.NotificationHelper;
import com.example.print.viewmodels.OrderViewModel;
import java.util.Calendar;
import java.util.Locale;

public class OrderHistoryFragment extends Fragment {

    private OrderAdapter adapter;
    private OrderViewModel viewModel;
    private TextView tvNoOrders;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_history, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.order_recycler);
        tvNoOrders = view.findViewById(R.id.tv_no_orders);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new OrderAdapter();
        recyclerView.setAdapter(adapter);

        setupAdapterListeners();

        viewModel = new ViewModelProvider(this).get(OrderViewModel.class);

        String userId = RealtimeDatabaseHelper.getInstance().getCurrentUserId();
        if (userId != null) {
            viewModel.startListening(userId);
        }

        viewModel.getOrders().observe(getViewLifecycleOwner(), orders -> {
            if (orders == null || orders.isEmpty()) {
                tvNoOrders.setVisibility(View.VISIBLE);
                adapter.setOrders(null);
            } else {
                tvNoOrders.setVisibility(View.GONE);
                adapter.setOrders(orders);
            }
        });

        return view;
    }

    private void setupAdapterListeners() {
        adapter.setOnOrderActionListener(new OrderAdapter.OnOrderActionListener() {
            @Override
            public void onCancelOrder(PrintOrder order) {
                new AlertDialog.Builder(requireContext())
                        .setTitle("Cancel Order")
                        .setMessage("Are you sure you want to cancel this order?")
                        .setPositiveButton("Yes", (dialog, which) -> {
                            RealtimeDatabaseHelper.getInstance().getOrdersRef()
                                    .child(order.getOrderId()).child("status").setValue("Cancelled")
                                    .addOnSuccessListener(aVoid -> {
                                        Toast.makeText(getContext(), "Order cancelled", Toast.LENGTH_SHORT).show();
                                        NotificationHelper.showNotification(requireContext(), "Order Cancelled", "Your order #" + order.getOrderId().substring(0, 5) + " has been cancelled.");
                                    });
                        })
                        .setNegativeButton("No", null)
                        .show();
            }

            @Override
            public void onRescheduleOrder(PrintOrder order) {
                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(),
                        (view, year1, monthOfYear, dayOfMonth) -> {
                            String selectedDate = String.format(Locale.getDefault(), "%02d/%02d/%d", dayOfMonth, monthOfYear + 1, year1);

                            // Update notes or a specific reschedule field in Firebase
                            RealtimeDatabaseHelper.getInstance().getOrdersRef()
                                    .child(order.getOrderId()).child("notes")
                                    .setValue(order.getNotes() + "\n[Rescheduled to: " + selectedDate + "]")
                                    .addOnSuccessListener(aVoid -> {
                                        Toast.makeText(getContext(), "Reschedule requested for " + selectedDate, Toast.LENGTH_SHORT).show();
                                        NotificationHelper.showNotification(requireContext(), "Reschedule Requested",
                                                "Requested delivery date for order #" + order.getOrderId().substring(0, 5) + " moved to " + selectedDate);
                                    });
                        }, year, month, day);
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
                datePickerDialog.show();
            }
        });
    }
}
