package com.example.online_ethio_gebeya.viewmodels;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.online_ethio_gebeya.data.repositories.ProductRepository;
import com.example.online_ethio_gebeya.models.responses.ProductShowResponse;

public class FragmentProductDetailViewModel extends AndroidViewModel {
    private final LiveData<ProductShowResponse> showResponse;
    private final long productId;
    private ProductRepository repository;

    public FragmentProductDetailViewModel(@NonNull ProductRepository _repository, long productId) {
        super(_repository.getApplication());
        repository = _repository;
        showResponse = repository.getShowResponse();
        this.productId = productId;
    }

    public LiveData<ProductShowResponse> getShowResponse() {
        return showResponse;
    }

    public void getProductDetail() {
        repository.getProductDetail(productId);
    }

    @Override
    protected void onCleared() {
        super.onCleared();

        repository.cancelConnection();
    }
}
