package com.example.online_ethio_gebeya.data.repositories;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.online_ethio_gebeya.data.RetrofitConnectionUtil;
import com.example.online_ethio_gebeya.data.apis.CartItemApi;
import com.example.online_ethio_gebeya.models.responses.InstructionsResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditCartItemRepository {
    private final String authorization;
    private Call<InstructionsResponse> call;
    private final CartItemApi api;
    private static final String TAG = "EditCartItemRepository";
    private final long cart_item_id;
    private final MutableLiveData<Boolean> enableUpdateButton;

    public EditCartItemRepository(@NonNull Application application, String _authorization, long _cart_item_id) {
        authorization = _authorization;
        cart_item_id = _cart_item_id;
        api = RetrofitConnectionUtil.getRetrofitInstance(application).create(CartItemApi.class);
        enableUpdateButton = new MutableLiveData<>();
    }

    public LiveData<Boolean> getEnableUpdateButton() {
        return enableUpdateButton;
    }

    public void updateCartItem(Integer value) {
        cancelConnection();

        if (value == null || authorization == null) {
            return;
        }

        call = api.updateItem(authorization, cart_item_id, value);
        call.enqueue(new Callback<InstructionsResponse>() {
            @Override
            public void onResponse(@NonNull Call<InstructionsResponse> call, @NonNull Response<InstructionsResponse> response) {
                if (response.isSuccessful()) {
                    enableUpdateButton.postValue(false);
                }
            }

            @Override
            public void onFailure(@NonNull Call<InstructionsResponse> call, @NonNull Throwable t) {
                //
            }
        });
    }

    public void cancelConnection() {
        if (call != null) {
            call.cancel();
        }
    }
}
