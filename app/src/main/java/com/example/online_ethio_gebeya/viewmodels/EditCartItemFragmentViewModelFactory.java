package com.example.online_ethio_gebeya.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.online_ethio_gebeya.data.repositories.EditCartItemRepository;
import com.example.online_ethio_gebeya.models.Item;

public class EditCartItemFragmentViewModelFactory implements ViewModelProvider.Factory {
    private final Application application;
    private final Item item;
    private final String authorization;

    public EditCartItemFragmentViewModelFactory(@NonNull Application _application,
                                                String _authorization,
                                                @NonNull Item _Item) {
        application = _application;
        item = _Item;
        authorization = _authorization;
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(EditCartItemFragmentViewModel.class)) {
            EditCartItemRepository repository = new EditCartItemRepository(application, authorization, item.getId());
            return (T) new EditCartItemFragmentViewModel(repository, item);
        } else {
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
