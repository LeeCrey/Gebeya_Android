package com.example.online_ethio_gebeya.data.repositories;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.online_ethio_gebeya.data.RetrofitConnectionUtil;
import com.example.online_ethio_gebeya.data.apis.PaymentApi;
import com.example.online_ethio_gebeya.helpers.JsonHelper;
import com.example.online_ethio_gebeya.models.Item;
import com.example.online_ethio_gebeya.models.responses.InstructionsResponse;

import java.io.IOException;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentRepository {
    private final PaymentApi api;
    private Call<InstructionsResponse> call;
    private Call<List<Item>> itemsIndexCall;
    private final MutableLiveData<InstructionsResponse> mResponse;
    private final MutableLiveData<List<Item>> mItems; // there is no difference b/n cart item and order item
    private String authToken;

    public PaymentRepository(Application application) {
        api = RetrofitConnectionUtil.getRetrofitInstance(application).create(PaymentApi.class);
        mResponse = new MutableLiveData<>();
        mItems = new MutableLiveData<>();
    }

    public LiveData<InstructionsResponse> getResponse() {
        return mResponse;
    }

    public LiveData<List<Item>> getItems() {
        return mItems;
    }

    //    get Order items
    public void makeGetItemList(@NonNull Long id) {
        cancelItemIndexCall();

        itemsIndexCall = api.getItemList(authToken, id);
        itemsIndexCall.enqueue(new Callback<List<Item>>() {
            @Override
            public void onResponse(@NonNull Call<List<Item>> call, @NonNull Response<List<Item>> response) {
                if (response.isSuccessful()) {
                    mItems.postValue(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Item>> call, @NonNull Throwable t) {

            }
        });
    }

    public void makePayment(long orderId) {
        cancelConnection();

        call = api.makePayment(authToken, orderId);
        call.enqueue(new Callback<InstructionsResponse>() {
            @Override
            public void onResponse(@NonNull Call<InstructionsResponse> call, @NonNull Response<InstructionsResponse> response) {
                if (response.isSuccessful()) {
                    mResponse.postValue(response.body());
                } else {
                    try {
                        ResponseBody body = response.errorBody();
                        if (body != null) {
                            InstructionsResponse rsp = JsonHelper.parseOperationError(body.string());
                            mResponse.postValue(rsp);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<InstructionsResponse> call, @NonNull Throwable t) {
                InstructionsResponse response = new InstructionsResponse();
                response.setOkay(false);
                response.setMessage(t.getMessage());
                mResponse.postValue(response);
            }
        });
    }

    public void setAuthorizationToken(String _authToken) {
        authToken = _authToken;
    }

    public void cancelConnection() {
        if (call != null) {
            call.cancel();
        }
    }

    private void cancelItemIndexCall() {
        if (itemsIndexCall != null) {
            itemsIndexCall.cancel();
        }
    }
}
