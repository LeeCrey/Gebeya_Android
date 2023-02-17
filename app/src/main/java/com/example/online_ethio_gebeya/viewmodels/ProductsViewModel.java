package com.example.online_ethio_gebeya.viewmodels;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.online_ethio_gebeya.data.repositories.ProductRepository;
import com.example.online_ethio_gebeya.models.responses.ProductResponse;

public class ProductsViewModel extends AndroidViewModel {
    private final LiveData<ProductResponse> oProductIndex;
    protected ProductRepository repository;

    public ProductsViewModel(@NonNull ProductRepository _repository) {
        super(_repository.getApplication());

        repository = _repository;
        oProductIndex = repository.getProductIndex();
        repository.getListFromApi("all");
    }

    public LiveData<ProductResponse> getProductIndex() {
        return oProductIndex;
    }

    public void searchProduct(String query) {
        repository.searchProduct(query);
    }

    @Override
    protected void onCleared() {
        super.onCleared();

        repository.cancelConnection();
    }
}
