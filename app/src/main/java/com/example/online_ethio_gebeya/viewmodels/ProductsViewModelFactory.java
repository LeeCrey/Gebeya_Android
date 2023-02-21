package com.example.online_ethio_gebeya.viewmodels;

import android.app.Application;
import android.location.Location;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.online_ethio_gebeya.data.repositories.ProductRepository;

public class ProductsViewModelFactory implements ViewModelProvider.Factory {
    private final String authToken;
    private final Application application;
    private Location location;

    public ProductsViewModelFactory(@NonNull Application application, String token, Location _location) {
        authToken = token;
        this.application = application;
        location = _location;
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(FragmentHomeViewModel.class)) {
            ProductRepository repository = new ProductRepository(application);
            repository.setAuthToken(authToken);
            repository.setLocation(location);
            return (T) new FragmentHomeViewModel(repository);
        } else {
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
