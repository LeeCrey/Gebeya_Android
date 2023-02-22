package com.example.online_ethio_gebeya.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.online_ethio_gebeya.data.repositories.OrderRepository;
import com.example.online_ethio_gebeya.models.responses.InstructionsResponse;

public class CheckoutFragmentViewModel extends AndroidViewModel {
    private final OrderRepository repository;
    private final LiveData<InstructionsResponse> mInstructionResponse;

    public CheckoutFragmentViewModel(@NonNull Application application) {
        super(application);

        repository = new OrderRepository(application, null, false);
        mInstructionResponse = repository.getInstructionResponse();
    }

    public LiveData<InstructionsResponse> getInstructionResponse() {
        return mInstructionResponse;
    }

    //    API
    public void createOrder(String authorization, long cartId) {
        repository.createOrder(authorization, cartId);
    }

    @Override
    protected void onCleared() {
        super.onCleared();

        repository.cancelConnection();
    }
}
