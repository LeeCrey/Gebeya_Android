package com.example.online_ethio_gebeya.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider.Factory;

import com.example.online_ethio_gebeya.data.repositories.ProductRepository;

public class ProductDetailFragmentViewModelFactory implements Factory {
    private final String autToken;
    private final long productId;
    private final Application application;

    public ProductDetailFragmentViewModelFactory(@NonNull Application application, String autToken, long productId) {
        this.autToken = autToken;
        this.productId = productId;
        this.application = application;
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(FragmentProductDetailViewModel.class)) {
            ProductRepository repository = new ProductRepository(application);
            repository.setAuthToken(autToken);
            return (T) new FragmentProductDetailViewModel(repository, productId);
        } else {
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
