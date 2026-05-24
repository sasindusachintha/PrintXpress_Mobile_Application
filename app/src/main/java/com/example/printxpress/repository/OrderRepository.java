package com.example.print.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import com.example.print.firebase.RealtimeDatabaseHelper;
import com.example.print.models.PrintOrder;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class OrderRepository {
    private final RealtimeDatabaseHelper dbHelper;
    private final MutableLiveData<List<PrintOrder>> orders = new MutableLiveData<>();
    private ValueEventListener orderListener;

    public OrderRepository() {
        this.dbHelper = RealtimeDatabaseHelper.getInstance();
    }

    /**
     * Places an order using the PrintOrder model.
     */
    public void placeOrder(PrintOrder order, OnOrderCompleteListener listener) {
        String id = dbHelper.getOrdersRef().push().getKey();
        if (id == null) {
            listener.onFailure("Could not generate order ID");
            return;
        }
        order.setOrderId(id);
        dbHelper.getOrdersRef().child(id).setValue(order)
                .addOnSuccessListener(aVoid -> listener.onSuccess())
                .addOnFailureListener(e -> listener.onFailure(e.getMessage()));
    }

    public void cancelOrder(String orderId, OnOrderCompleteListener listener) {
        dbHelper.getOrdersRef().child(orderId).child("status").setValue("Cancelled")
                .addOnSuccessListener(aVoid -> listener.onSuccess())
                .addOnFailureListener(e -> listener.onFailure(e.getMessage()));
    }

    public void startListeningForOrders(String userId) {
        if (orderListener != null) {
            dbHelper.getOrdersRef().removeEventListener(orderListener);
        }

        orderListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<PrintOrder> orderList = new ArrayList<>();
                for (DataSnapshot doc : snapshot.getChildren()) {
                    PrintOrder order = doc.getValue(PrintOrder.class);
                    if (order != null && userId.equals(order.getUserId())) {
                        orderList.add(order);
                    }
                }
                // Sort by timestamp descending (using long compare for compatibility)
                orderList.sort((o1, o2) -> Long.compare(o2.getTimestamp(), o1.getTimestamp()));
                orders.setValue(orderList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        };

        dbHelper.getOrdersRef().addValueEventListener(orderListener);
    }

    public void stopListening() {
        if (orderListener != null) {
            dbHelper.getOrdersRef().removeEventListener(orderListener);
        }
    }

    public MutableLiveData<List<PrintOrder>> getOrders() {
        return orders;
    }

    public interface OnOrderCompleteListener {
        void onSuccess();
        void onFailure(String error);
    }
}
