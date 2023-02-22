package com.example.online_ethio_gebeya.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.online_ethio_gebeya.data.repositories.EditCartItemRepository;
import com.example.online_ethio_gebeya.models.CartItem;

public class EditCartItemFragmentViewModelFactory implements ViewModelProvider.Factory {
    private final Application application;
    private final CartItem cartItem;
    private final String authorization;

    public EditCartItemFragmentViewModelFactory(@NonNull Application _application,
                                                String _authorization,
                                                @NonNull CartItem _cartItem) {
        application = _application;
        cartItem = _cartItem;
        authorization = _authorization;
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(EditCartItemFragmentViewModel.class)) {
            EditCartItemRepository repository = new EditCartItemRepository(application, authorization, cartItem.getId());
            return (T) new EditCartItemFragmentViewModel(repository, cartItem);
        } else {
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
