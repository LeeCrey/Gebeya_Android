package com.example.online_ethio_gebeya.data.repositories;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.online_ethio_gebeya.data.RetrofitConnectionUtil;
import com.example.online_ethio_gebeya.data.apis.OrderApi;
import com.example.online_ethio_gebeya.models.Order;
import com.example.online_ethio_gebeya.models.responses.InstructionsResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderRepository {
    private MutableLiveData<List<Order>> mOrderList;
    private MutableLiveData<InstructionsResponse> mInstructionResponse;

    private final String authorization;
    private final OrderApi api;
    private Call<List<Order>> orderIndexCall;
    private Call<InstructionsResponse> createCall;

    public OrderRepository(@NonNull Application application, String _authorization, boolean _for_index) {
        authorization = _authorization;
        api = RetrofitConnectionUtil.getRetrofitInstance(application).create(OrderApi.class);
        if (_for_index) {
            mOrderList = new MutableLiveData<>();
        } else {
            mInstructionResponse = new MutableLiveData<>();
        }
    }

    public LiveData<List<Order>> getOrderList() {
        return mOrderList;
    }

    public LiveData<InstructionsResponse> getInstructionResponse() {
        return mInstructionResponse;
    }

    //  APIs
    // index
    public void orderIndex() {
        cancelConnection();

        orderIndexCall = api.index(authorization);
        orderIndexCall.enqueue(new Callback<List<Order>>() {
            @Override
            public void onResponse(@NonNull Call<List<Order>> call, @NonNull Response<List<Order>> response) {
                if (response.isSuccessful()) {
                    mOrderList.postValue(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Order>> call, @NonNull Throwable t) {
                // ignore
                Log.e("OrderRepository", "onFailure: " + t.getMessage());
            }
        });
    }

    //    create
    public void createOrder(String auth, long cartId) {
        cancelCreateCall();

        createCall = api.createOrder(auth, cartId);
        createCall.enqueue(new Callback<InstructionsResponse>() {
            @Override
            public void onResponse(@NonNull Call<InstructionsResponse> call, @NonNull Response<InstructionsResponse> response) {
                if (response.isSuccessful()) {
                    mInstructionResponse.postValue(response.body());
                } else {
                    if (response.code() == 404) {
                        postErrorResponse("Cart not found");
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<InstructionsResponse> call, @NonNull Throwable t) {
                postErrorResponse(t.getMessage());
            }
        });
    }

    public void cancelConnection() {
        if (orderIndexCall != null) {
            orderIndexCall.cancel();
        }
    }

    private void cancelCreateCall() {
        if (createCall != null) {
            createCall.cancel();
        }
    }

    private void postErrorResponse(String msg) {
        InstructionsResponse resp = new InstructionsResponse();
        resp.setMessage(msg);
        resp.setOkay(false);
        mInstructionResponse.postValue(resp);
    }
}
