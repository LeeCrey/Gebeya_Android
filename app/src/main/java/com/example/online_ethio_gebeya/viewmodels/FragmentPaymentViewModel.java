package com.example.online_ethio_gebeya.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.online_ethio_gebeya.data.repositories.PaymentRepository;
import com.example.online_ethio_gebeya.models.Item;
import com.example.online_ethio_gebeya.models.Order;
import com.example.online_ethio_gebeya.models.responses.InstructionsResponse;

import java.util.List;

public class FragmentPaymentViewModel extends AndroidViewModel {
    private final LiveData<List<Item>> mItems; // there is no difference b/n cart item and order item
    private final PaymentRepository repository;
    private final LiveData<InstructionsResponse> oResponse;

    public FragmentPaymentViewModel(@NonNull Application application) {
        super(application);

        repository = new PaymentRepository(application);
        mItems = repository.getItems();
        oResponse = repository.getResponse();
    }

    public LiveData<InstructionsResponse> getResponse() {
        return oResponse;
    }

    public void makePayment(@NonNull Order order) {
        repository.makePayment(order);
    }

    public LiveData<List<Item>> getItems() {
        return mItems;
    }

    public void setAuthorizationToken(String authToken) {
        repository.setAuthorizationToken(authToken);
    }

    @Override
    protected void onCleared() {
        super.onCleared();

        repository.cancelConnection();
    }

    public void makeGetItemList(@NonNull Long id) {
        repository.makeGetItemList(id);
    }
}
