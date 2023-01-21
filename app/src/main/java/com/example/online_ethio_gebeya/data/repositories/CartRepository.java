package com.example.online_ethio_gebeya.data.repositories;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.online_ethio_gebeya.data.RetrofitConnectionUtil;
import com.example.online_ethio_gebeya.data.apis.CartApi;
import com.example.online_ethio_gebeya.helpers.PreferenceHelper;
import com.example.online_ethio_gebeya.models.Cart;
import com.example.online_ethio_gebeya.models.responses.CartResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartRepository {
    private static final String TAG = "CartRepository";
    private final MutableLiveData<List<Cart>> mCartList;
    private final CartApi api;
    private final String token;
    private Call<CartResponse> cartResponseCall;

    public CartRepository(@NonNull Application application) {
        api = RetrofitConnectionUtil.getRetrofitInstance(application).create(CartApi.class);
        mCartList = new MutableLiveData<>();

        token = PreferenceHelper.getAuthToken(application.getBaseContext());
        getCartList();
    }

    public LiveData<List<Cart>> getCartResponse() {
        return mCartList;
    }

    // crud
    public void getCartList() {
        cancelConnection();

        cartResponseCall = api.index(token);
        cartResponseCall.enqueue(new Callback<CartResponse>() {
            @Override
            public void onResponse(@NonNull Call<CartResponse> call, @NonNull Response<CartResponse> response) {
                CartResponse cartResponse = response.body();
                if (cartResponse != null) {
                    mCartList.postValue(cartResponse.getList());
                }
            }

            @Override
            public void onFailure(@NonNull Call<CartResponse> call, @NonNull Throwable t) {

            }
        });
    }

    // DELETE CART(SINGLE)
    public void deleteCart(@NonNull Cart cart) {
        cancelConnection();

        cartResponseCall = api.delete(token, cart.getId());
        cartResponseCall.enqueue(new Callback<CartResponse>() {
            @Override
            public void onResponse(@NonNull Call<CartResponse> call, @NonNull Response<CartResponse> response) {
                if (response.isSuccessful()) {
                    List<Cart> currentList = mCartList.getValue();
                    if (currentList != null) {
                        currentList.remove(cart); // remove
                        mCartList.postValue(currentList);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<CartResponse> call, @NonNull Throwable t) {
                // ignore
            }
        });
    }

    // DELETE ALL CARTS
    public void deleteAllCarts() {
        cancelConnection();

        cartResponseCall = api.deleteAll(token);
        cartResponseCall.enqueue(new Callback<CartResponse>() {
            @Override
            public void onResponse(@NonNull Call<CartResponse> call, @NonNull Response<CartResponse> response) {
                if (response.isSuccessful()) {
                    List<Cart> currentList = mCartList.getValue();
                    if (currentList != null) {
                        currentList.clear();
                        mCartList.postValue(currentList);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<CartResponse> call, @NonNull Throwable t) {
                // ignore
            }
        });
    }

    //
    public void cancelConnection() {
        if (cartResponseCall != null) {
            cartResponseCall.cancel();
        }
    }
}