package com.example.online_ethio_gebeya.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.online_ethio_gebeya.data.repositories.OrderRepository;

public class FragmentOrderViewModelFactory implements ViewModelProvider.Factory {
    private final String authorization;
    private final Application application;

    public FragmentOrderViewModelFactory(@NonNull Application _application, String _authorization) {
        authorization = _authorization;
        application = _application;
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(FragmentOrdersViewModel.class)) {
            OrderRepository repository = new OrderRepository(application, authorization, true);
            return (T) new FragmentOrdersViewModel(repository);
        } else {
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
