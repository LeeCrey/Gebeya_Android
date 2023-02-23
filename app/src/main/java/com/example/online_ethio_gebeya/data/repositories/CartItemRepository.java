package com.example.online_ethio_gebeya.data.repositories;

import android.app.Application;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.online_ethio_gebeya.data.RetrofitConnectionUtil;
import com.example.online_ethio_gebeya.data.apis.CartItemApi;
import com.example.online_ethio_gebeya.models.CartItem;
import com.example.online_ethio_gebeya.models.responses.CartItemResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartItemRepository {
    private static final String TAG = "CartItemRepository";
    private final CartItemApi api;
    private final MutableLiveData<List<CartItem>> mCartItemList;
    private Call<CartItemResponse> cartItemResponseCall;
    private Call<List<CartItem>> listCall;
    private String authorizationToken;

    public CartItemRepository(@NonNull Application application) {
        mCartItemList = new MutableLiveData<>();
        api = RetrofitConnectionUtil.getRetrofitInstance(application).create(CartItemApi.class);
    }

    public LiveData<List<CartItem>> getCartItemList() {
        return mCartItemList;
    }

    public void cancelConnection() {
        if (cartItemResponseCall != null) {
            cartItemResponseCall.cancel();
        }
    }

    //    index
    public void getCartItems(long cartId, String authToken) {
        if (listCall != null) {
            listCall.cancel();
        }

        listCall = api.getCartList(authToken, cartId);
        listCall.enqueue(new Callback<List<CartItem>>() {
            @Override
            public void onResponse(@NonNull Call<List<CartItem>> call, @NonNull Response<List<CartItem>> response) {
                if (response.isSuccessful()) {
                    mCartItemList.postValue(response.body());
                } else {
                    setEmptyToList();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<CartItem>> call, @NonNull Throwable t) {
                // ignore
                setEmptyToList();
            }
        });
    }

    private void setEmptyToList() {
        mCartItemList.postValue(new ArrayList<>());
    }

    // add item to cart
    public void addItemToCart(long productId, Button addToCart, @NonNull Integer value) {
        cancelConnection();

        addToCart.setEnabled(false);
        cartItemResponseCall = api.addItem(authorizationToken, productId, value);
        cartItemResponseCall.enqueue(new Callback<CartItemResponse>() {
            @Override
            public void onResponse(@NonNull Call<CartItemResponse> call, @NonNull Response<CartItemResponse> response) {
                if (response.isSuccessful()) {
                    addToCart.setText("Done");
                } else {
                    // if it's bad request(item may be exist in cart. No need to enable button)
                    // no need to create duplicate item in cart(we should add quantity)
                    if (response.code() != 400) {
                        // unauthorized request
                        addToCart.setEnabled(true);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<CartItemResponse> call, @NonNull Throwable t) {
                // ignore
            }
        });
    }

    // delete item from cart
    public void removeItemFromCart(String authToken, int position) {
        if (cartItemResponseCall != null) {
            cartItemResponseCall.cancel();
        }

        List<CartItem> items = new ArrayList<>(Objects.requireNonNull(mCartItemList.getValue()));

        CartItem item = items.get(position);
        cartItemResponseCall = api.deleteItem(authToken, item.getId());
        cartItemResponseCall.enqueue(new Callback<CartItemResponse>() {
            @Override
            public void onResponse(@NonNull Call<CartItemResponse> call, @NonNull Response<CartItemResponse> response) {
                if (response.isSuccessful()) {
                    items.remove(item);
                    mCartItemList.postValue(items);
                }
            }

            @Override
            public void onFailure(@NonNull Call<CartItemResponse> call, @NonNull Throwable t) {
                // ignore
            }
        });
    }

    public void setAuthorizationToken(String authorizationToken) {
        this.authorizationToken = authorizationToken;
    }
}
