package com.example.online_ethio_gebeya.data.repositories.account;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.online_ethio_gebeya.data.RetrofitConnectionUtil;
import com.example.online_ethio_gebeya.data.apis.account.RegistrationsApi;
import com.example.online_ethio_gebeya.models.Customer;
import com.example.online_ethio_gebeya.models.responses.InstructionsResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistrationsRepository {
    private MutableLiveData<InstructionsResponse> mRegResponse;
    private RegistrationsApi api;
    private Call<InstructionsResponse> apiCall;

    public RegistrationsRepository(@NonNull Application application) {
        if (null != mRegResponse) {
            return;
        }

        mRegResponse = new MutableLiveData<>();
        api = RetrofitConnectionUtil.getRetrofitInstance(application).create(RegistrationsApi.class);
    }

    public MutableLiveData<InstructionsResponse> getRegistrationResponse() {
        return mRegResponse;
    }

    // APIs
    public void signUp(Customer customer) {
        cancelConnection();

        apiCall = api.signUp(customer);
        apiCall.enqueue(new Callback<InstructionsResponse>() {
            @Override
            public void onResponse(@NonNull Call<InstructionsResponse> call, @NonNull Response<InstructionsResponse> response) {
                mRegResponse.postValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<InstructionsResponse> call, @NonNull Throwable t) {
                InstructionsResponse response = new InstructionsResponse();
                response.setOkay(false);
                response.setMessage("Something went wrong.");
                mRegResponse.postValue(response);
            }
        });
    }

    // confirm account
    public void confirmAccount(@NonNull String confirmUrl) {
        cancelConnection();

        apiCall = api.confirmAccount(confirmUrl);
        apiCall.enqueue(new Callback<InstructionsResponse>() {
            @Override
            public void onResponse(@NonNull Call<InstructionsResponse> call, @NonNull Response<InstructionsResponse> response) {

            }

            @Override
            public void onFailure(@NonNull Call<InstructionsResponse> call, @NonNull Throwable t) {

            }
        });
    }

    // update profile
    public void editAccount(Customer customer, String authorizationToken) {
        cancelConnection();

        apiCall = api.updateAccount(authorizationToken, customer);
    }

    // delete account
    public void deleteAccount(String authorizationToken) {
        cancelConnection(); // cancel connection if there was request made before

        apiCall = api.destroyAccount(authorizationToken);
    }

    // cancel all requests
    public void cancelConnection() {
        if (null != apiCall) {
            apiCall.cancel();
        }
    }
}
