package com.example.online_ethio_gebeya.data.repositories.account;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.online_ethio_gebeya.data.RetrofitConnectionUtil;
import com.example.online_ethio_gebeya.data.apis.account.RegistrationsApi;
import com.example.online_ethio_gebeya.helpers.JsonHelper;
import com.example.online_ethio_gebeya.models.Customer;
import com.example.online_ethio_gebeya.models.responses.InstructionsResponse;
import com.example.online_ethio_gebeya.models.responses.RegistrationResponse;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistrationsRepository {
    private static final String TAG = "RegistrationsRepository";
    private MutableLiveData<RegistrationResponse> mRegResponse;
    private RegistrationsApi api;
    private Call<RegistrationResponse> apiCall;
    private Call<InstructionsResponse> confirmApiCall;

    public RegistrationsRepository(@NonNull Application application) {
        if (null != mRegResponse) {
            return;
        }

        mRegResponse = new MutableLiveData<>();
        api = RetrofitConnectionUtil.getRetrofitInstance(application).create(RegistrationsApi.class);
    }

    public MutableLiveData<RegistrationResponse> getRegistrationResponse() {
        return mRegResponse;
    }

    // APIs
    public void signUp(Customer customer) {
        cancelConnection();

        apiCall = api.signUp(customer);
        apiCall.enqueue(new Callback<RegistrationResponse>() {
            @Override
            public void onResponse(@NonNull Call<RegistrationResponse> call, @NonNull Response<RegistrationResponse> response) {
                RegistrationResponse lastResponse = null;
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    Log.d(TAG, "onResponse: " + response.body());
                    lastResponse = response.body();
                } else {
                    // retrofit does not deserialize string into object when response code is not successful, so
                    // we have to do it manually
                    ResponseBody errorBody = response.errorBody();
                    if (null != errorBody) {
                        try {
                            lastResponse = JsonHelper.parseSignUpError(errorBody.string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                mRegResponse.postValue(lastResponse);
            }

            @Override
            public void onFailure(@NonNull Call<RegistrationResponse> call, @NonNull Throwable t) {
                RegistrationResponse response = new RegistrationResponse();
                response.setOkay(false);
                response.setMsg("Something went wrong.");
                mRegResponse.postValue(response);
            }
        });
    }

    // confirm account
    public void confirmAccount(@NonNull String confirmUrl) {
        cancelConnection();

        confirmApiCall = api.confirmAccount(confirmUrl);
    }

    // update profile
    public void editAccount(Customer customer, String authorizationToken) {
        cancelConnection();

        apiCall = api.updateAccount(authorizationToken, customer);
        // unfinished
        apiCall.enqueue(new Callback<RegistrationResponse>() {
            @Override
            public void onResponse(@NonNull Call<RegistrationResponse> call, @NonNull Response<RegistrationResponse> response) {

            }

            @Override
            public void onFailure(@NonNull Call<RegistrationResponse> call, @NonNull Throwable t) {

            }
        });
    }

    // delete account
    public void deleteAccount(String authorizationToken) {
        cancelConnection(); // cancel connection if there was request made before

        apiCall = api.destroyAccount(authorizationToken);
        apiCall.enqueue(new Callback<RegistrationResponse>() {
            @Override
            public void onResponse(@NonNull Call<RegistrationResponse> call, @NonNull Response<RegistrationResponse> response) {

            }

            @Override
            public void onFailure(@NonNull Call<RegistrationResponse> call, @NonNull Throwable t) {
                RegistrationResponse response = new RegistrationResponse();
                response.setOkay(false);
                response.setMsg(t.getMessage());
                mRegResponse.postValue(response);
            }
        });
    }

    // cancel all requests
    public void cancelConnection() {
        if (null != apiCall) {
            apiCall.cancel();
        }

        if (null != confirmApiCall) {
            confirmApiCall.cancel();
        }
    }
}
