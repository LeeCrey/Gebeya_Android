package com.example.online_ethio_gebeya.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.online_ethio_gebeya.data.repositories.InstructionRepository;
import com.example.online_ethio_gebeya.models.Customer;
import com.example.online_ethio_gebeya.models.responses.InstructionsResponse;

public class InstructionsViewModel extends AndroidViewModel {
    private final LiveData<InstructionsResponse> oInstructionResponse;
    private final InstructionRepository repository;

    public InstructionsViewModel(@NonNull Application application) {
        super(application);

        repository = new InstructionRepository(application);
        oInstructionResponse = repository.getInstructionResponse();
    }

    public LiveData<InstructionsResponse> getInstructionResponse() {
        return oInstructionResponse;
    }

    public void sendFeedback(String header, String msg) {
        repository.sendFeedback(header, msg);
    }

    public void sendInstruction(String email, @NonNull String path) {
        Customer customer = new Customer();
        customer.setEmail(email);
        repository.sendRequest(customer, path);
    }

    public void sendFinishRequest(@NonNull String url) {
        repository.finishInstructionRequest(url);
    }

    public void nullifyLiveData() {
        repository.nullifyObserverData();
    }
}
