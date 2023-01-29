package com.example.online_ethio_gebeya.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.online_ethio_gebeya.data.repositories.ProductRepository;

public class ProductsViewModelFactory implements ViewModelProvider.Factory {
    private final String authToken;
    private final Application application;

    public ProductsViewModelFactory(@NonNull Application application, String token) {
        authToken = token;
        this.application = application;
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(FragmentHomeViewModel.class)) {
            ProductRepository repository = new ProductRepository(application);
            repository.setAuthToken(authToken);
            return (T) new FragmentHomeViewModel(repository);
        } else {
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
