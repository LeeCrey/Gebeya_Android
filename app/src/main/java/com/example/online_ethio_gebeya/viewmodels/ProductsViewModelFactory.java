package com.example.online_ethio_gebeya.viewmodels;

import android.app.Application;

import androidx.lifecycle.ViewModelProvider;

public class ProductsViewModelFactory implements ViewModelProvider.Factory {
    private final String authToken;
    private Application application;

    public ProductsViewModelFactory(String token) {
        authToken = token;
    }

//    @NonNull
//    @Override
//    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
//        if (modelClass.isAssignableFrom(ProductsViewModel.class)) {
//            ProductRepository repository = new ProductRepository(ap)
//            RateRepository repository = new RateRepository(application, productId, token);
//            return (T) new RateFragmentViewModel(repository);
//        } else {
//            throw new IllegalArgumentException("Unknown ViewModel class");
//        }
//        return ViewModelProvider.Factory.super.create(modelClass);
//    }
}
