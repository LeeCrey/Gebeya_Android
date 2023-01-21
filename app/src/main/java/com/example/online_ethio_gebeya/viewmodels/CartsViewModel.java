package com.example.online_ethio_gebeya.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.online_ethio_gebeya.data.repositories.CartRepository;
import com.example.online_ethio_gebeya.models.Cart;

import java.util.List;

public class CartsViewModel extends AndroidViewModel {
    private final CartRepository repository;
    private final LiveData<List<Cart>> cartResponse;

    public CartsViewModel(@NonNull Application application) {
        super(application);

        repository = new CartRepository(application);
        cartResponse = repository.getCartResponse();
    }

    public LiveData<List<Cart>> getCartResponse() {
        return cartResponse;
    }

    // apis
    public void getCarts() {
        repository.getCartList();
    }

    public void deleteCart(@NonNull Cart cart) {
        repository.deleteCart(cart);
    }

    public void deleteAllCarts() {
        repository.deleteAllCarts();
    }

    @Override
    protected void onCleared() {
        super.onCleared();

        repository.cancelConnection();
    }
}
