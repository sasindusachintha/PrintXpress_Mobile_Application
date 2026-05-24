package com.example.print.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import com.example.print.models.PrintOrder;
import com.example.print.repository.OrderRepository;
import java.util.List;

public class OrderViewModel extends ViewModel {
    private final OrderRepository repository;
    private final LiveData<List<PrintOrder>> orders;

    public OrderViewModel() {
        repository = new OrderRepository();
        orders = repository.getOrders();
    }

    public void startListening(String userId) {
        repository.startListeningForOrders(userId);
    }

    public LiveData<List<PrintOrder>> getOrders() {
        return orders;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        repository.stopListening();
    }
}
