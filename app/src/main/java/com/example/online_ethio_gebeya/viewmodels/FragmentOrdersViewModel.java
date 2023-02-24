package com.example.online_ethio_gebeya.viewmodels;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.online_ethio_gebeya.data.repositories.OrderRepository;
import com.example.online_ethio_gebeya.models.Order;

import java.util.List;

public class FragmentOrdersViewModel extends ViewModel {
    private final LiveData<List<Order>> mOrders;
    private final MutableLiveData<Integer> mPaidOrderPosition;
    private final OrderRepository repository;

    public FragmentOrdersViewModel(@NonNull OrderRepository repository) {
        this.repository = repository;
        mOrders = this.repository.getOrderList();
        repository.orderIndex();
        mPaidOrderPosition = new MutableLiveData<>();
    }

    public LiveData<List<Order>> getOrders() {
        return mOrders;
    }

    public LiveData<Integer> getPaidOrderPosition() {
        return mPaidOrderPosition;
    }

    public void makePaid(int _position) {
        mPaidOrderPosition.postValue(_position);
    }

    @Override
    protected void onCleared() {
        super.onCleared();

        repository.cancelConnection();
    }
}
