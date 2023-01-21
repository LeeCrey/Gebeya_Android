package com.example.online_ethio_gebeya.viewmodels;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.online_ethio_gebeya.data.repositories.ProductRepository;
import com.example.online_ethio_gebeya.models.responses.ProductShowResponse;

public class ProductDetailFragmentViewModel extends ViewModel {
    private final LiveData<ProductShowResponse> showResponse;
    private final ProductRepository productRepository;
    private final int productId;

    public ProductDetailFragmentViewModel(@NonNull ProductRepository repository, int productId) {
        productRepository = repository;
        showResponse = productRepository.getShowResponse();
        this.productId = productId;
    }

    public LiveData<ProductShowResponse> getShowResponse() {
        return showResponse;
    }

    public void getProductDetail() {
        productRepository.getProductDetail(productId);
    }
}
