package com.example.online_ethio_gebeya.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider.Factory;

import com.example.online_ethio_gebeya.data.repositories.ProductRepository;

public class ProductDetailFragmentViewModelFactory implements Factory {
    private final String autToken;
    private final int productId;
    private final Application application;

    public ProductDetailFragmentViewModelFactory(@NonNull Application application, String autToken, int productId) {
        this.autToken = autToken;
        this.productId = productId;
        this.application = application;
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(ProductDetailFragmentViewModel.class)) {
            ProductRepository repository = new ProductRepository(application);
            repository.setAuthToken(autToken);
            return (T) new ProductDetailFragmentViewModel(repository, productId);
        } else {
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
