package com.example.online_ethio_gebeya.data.repositories;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.online_ethio_gebeya.data.RetrofitConnectionUtil;
import com.example.online_ethio_gebeya.data.apis.OrderApi;
import com.example.online_ethio_gebeya.models.Order;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderRepository {
    private MutableLiveData<List<Order>> mOrderList;
    private String authorization;
    private OrderApi api;
    private Call<List<Order>> orderIndexCall;

    public OrderRepository(@NonNull Application application, String _authorization) {
        authorization = _authorization;
        api = RetrofitConnectionUtil.getRetrofitInstance(application).create(OrderApi.class);
        mOrderList = new MutableLiveData<>();
        orderIndex(); // #OrderController#index
    }

    public LiveData<List<Order>> getOrderList() {
        return mOrderList;
    }

    //    APIs
    private void orderIndex() {
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

    public void cancelConnection() {
        if (orderIndexCall != null) {
            orderIndexCall.cancel();
        }
    }
}
