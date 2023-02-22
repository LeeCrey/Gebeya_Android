package com.example.online_ethio_gebeya.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.online_ethio_gebeya.data.repositories.CartItemRepository;
import com.example.online_ethio_gebeya.models.CartItem;

import java.util.List;

public class FragmentCartItemViewModel extends AndroidViewModel {
    private final CartItemRepository repository;
    private final LiveData<List<CartItem>> oCartItemList;
    private final MutableLiveData<Boolean> mOrderCreated;
    private final MutableLiveData<CartItem> mUpdatedCartItem;
    private String authToken = null;

    public FragmentCartItemViewModel(@NonNull Application application) {
        super(application);

        repository = new CartItemRepository(application);
        oCartItemList = repository.getCartItemList();
        mOrderCreated = new MutableLiveData<>();
        mUpdatedCartItem = new MutableLiveData<>();
    }

    public void init(String authorizationToken) {
        authToken = authorizationToken;
    }

    public LiveData<Boolean> getOrderCreated() {
        return mOrderCreated;
    }

    public LiveData<CartItem> getUpdatedCartItem() {
        return mUpdatedCartItem;
    }

    public LiveData<List<CartItem>> getCartItemResponse() {
        return oCartItemList;
    }

    // get list
    public void getCartItems(long cartId) {
        repository.getCartItems(cartId, authToken);
    }

    public void removeItemFromCart(int position) {
        repository.removeItemFromCart(authToken, position);
    }

    public void setOrderCreated() {
        mOrderCreated.postValue(true);
    }

    public void setUpdateCartItem(@NonNull CartItem cartItem) {
        mUpdatedCartItem.postValue(cartItem);
    }
}
