package com.example.online_ethio_gebeya.viewmodels;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.online_ethio_gebeya.data.repositories.EditCartItemRepository;
import com.example.online_ethio_gebeya.models.CartItem;

public class EditCartItemFragmentViewModel extends ViewModel {
    private final int productQuantity;
    private final MutableLiveData<Integer> currentQuantity;
    private final EditCartItemRepository repository;
    private boolean increment, decrement;
    private final LiveData<Boolean> enableUpdateButton;

    public EditCartItemFragmentViewModel(@NonNull EditCartItemRepository _repository, @NonNull CartItem _cartItem) {
        repository = _repository;
        currentQuantity = new MutableLiveData<>(_cartItem.getQuantity());
        enableUpdateButton = repository.getEnableUpdateButton();
        productQuantity = _cartItem.getProduct().getQuantity();
        decrement = _cartItem.getQuantity() > 1;
        increment = _cartItem.getQuantity() < _cartItem.getProduct().getQuantity();
    }

    public LiveData<Integer> getCurrentQuantity() {
        return currentQuantity;
    }

    public LiveData<Boolean> getEnableUpdateButton() {
        return enableUpdateButton;
    }

    //    APi
    public void updateCartItem() {
        repository.updateCartItem(currentQuantity.getValue());
    }

    public void increment() {
        Integer value = currentQuantity.getValue();
        if (value != null) {
            decrement = value > 0;
            if (value < productQuantity) {
                value += 1;
                increment = value < productQuantity;
                currentQuantity.postValue(value);
            }
        }
    }

    public void decrement() {
        Integer value = currentQuantity.getValue();
        if (value != null) {
            increment = true;
            if (value > 1) {
                value -= 1;
                decrement = value > 1;
                currentQuantity.postValue(value);
            }
        }
    }

    public boolean isIncrement() {
        return increment;
    }

    public boolean isDecrement() {
        return decrement;
    }

    @Override
    protected void onCleared() {
        super.onCleared();

        repository.cancelConnection();
    }
}
