package com.example.online_ethio_gebeya.viewmodels;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.online_ethio_gebeya.data.repositories.OrderRepository;
import com.example.online_ethio_gebeya.models.Order;

import java.util.List;

public class FragmentOrdersViewModel extends ViewModel {
    private LiveData<List<Order>> mOrders;
    private OrderRepository repository;

    public FragmentOrdersViewModel(@NonNull OrderRepository repository) {
        this.repository = repository;
        mOrders = this.repository.getOrderList();
    }

    public LiveData<List<Order>> getOrders() {
        return mOrders;
    }

    @Override
    protected void onCleared() {
        super.onCleared();

        repository.cancelConnection();
    }
}
