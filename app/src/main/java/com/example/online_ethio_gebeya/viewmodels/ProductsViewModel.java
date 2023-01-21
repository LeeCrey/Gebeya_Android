package com.example.online_ethio_gebeya.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.online_ethio_gebeya.data.repositories.ProductRepository;
import com.example.online_ethio_gebeya.models.responses.ProductResponse;

public class ProductsViewModel extends AndroidViewModel {
    private final LiveData<ProductResponse> oProductIndex;
    protected ProductRepository repository;
    protected String authToken = null;

    public ProductsViewModel(@NonNull Application application) {
        super(application);

        repository = new ProductRepository(application);
        oProductIndex = repository.getProductIndex();
        repository.getListFromApi("all");
    }

    public LiveData<ProductResponse> getProductIndex() {
        return oProductIndex;
    }

    @Override
    protected void onCleared() {
        super.onCleared();

        repository.cancelConnection();
    }
}
