package com.example.online_ethio_gebeya.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.online_ethio_gebeya.data.repositories.ProductRepository;
import com.example.online_ethio_gebeya.models.responses.ProductResponse;

public class FragmentSearchViewModel extends AndroidViewModel {
    private final ProductRepository repository;
    private final LiveData<ProductResponse> mProductResponse;

    public FragmentSearchViewModel(@NonNull Application application) {
        super(application);

        repository = new ProductRepository(application);
        mProductResponse = repository.getProductIndex();
    }

    public LiveData<ProductResponse> getProductResponse() {
        return mProductResponse;
    }

    public void searchProduct(String query) {
        repository.searchProduct(query);
    }

    public void setAuthToken(String authToken) {
        repository.setAuthToken(authToken);
    }
}
