package com.example.online_ethio_gebeya.data.repositories.account;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.online_ethio_gebeya.data.RetrofitConnectionUtil;
import com.example.online_ethio_gebeya.data.apis.account.RegistrationsApi;
import com.example.online_ethio_gebeya.models.Customer;
import com.example.online_ethio_gebeya.models.responses.InstructionsResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistrationsRepository {
    private static final String TAG = "RegistrationsRepository";
    private MutableLiveData<InstructionsResponse> mRegResponse;
    private MutableLiveData<Customer> mCustomer;
    private RegistrationsApi api;
    private Call<InstructionsResponse> apiCall;
    private Call<Customer> customerCall;
    private Application application;

    public RegistrationsRepository(@NonNull Application application) {
        if (null != mRegResponse) {
            return;
        }

        mRegResponse = new MutableLiveData<>();
        api = RetrofitConnectionUtil.getRetrofitInstance(application).create(RegistrationsApi.class);
    }

    public LiveData<Customer> getCustomer() {
        return mCustomer;
    }

    public MutableLiveData<InstructionsResponse> getRegistrationResponse() {
        return mRegResponse;
    }

    public void initForAccountUpdate() {
        mCustomer = new MutableLiveData<>();
    }

    public void getCurrentCustomer(String auth) {
        if (customerCall != null) {
            customerCall.cancel();
        }

        customerCall = api.getCurrentCustomer(auth);
        customerCall.enqueue(new Callback<Customer>() {
            @Override
            public void onResponse(@NonNull Call<Customer> call, @NonNull Response<Customer> response) {
                if (response.isSuccessful()) {
                    mCustomer.postValue(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Customer> call, @NonNull Throwable t) {

            }
        });
    }

    // APIs
    public void signUp(@NonNull Customer customer) {
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
    public void updateProfile(@NonNull Customer customer, String authorizationToken) {
        cancelConnection();

        Log.d(TAG, "updateProfile: " + authorizationToken);
        return;
//        RequestBody firstName = RequestBody.create(customer.getFirstName(), MediaType.parse("text/plain"));
//        RequestBody lastName = RequestBody.create(customer.getLastName(), MediaType.parse("text/plain"));
//        RequestBody currentPassword = RequestBody.create(customer.getPassword(), MediaType.parse("text/plain"));
//        RequestBody profilePic = RequestBody.create(customer.getProfile(), MediaType.parse("image/*"));

//        apiCall = api.updateAccount(authorizationToken, firstName, lastName, currentPassword);
//        apiCall.enqueue(new Callback<InstructionsResponse>() {
//            @Override
//            public void onResponse(@NonNull Call<InstructionsResponse> call, @NonNull Response<InstructionsResponse> response) {
//                if (response.isSuccessful()) {
//                    mRegResponse.postValue(response.body());
//                } else {
////                    if (response.code() == 401) {
////                        // clear pref
////                        PreferenceHelper.clearPref(application);
////                    }
//                }
//            }
//
//            @Override
//            public void onFailure(@NonNull Call<InstructionsResponse> call, @NonNull Throwable t) {
//
//            }
//        });
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

    public Application getApplication() {
        return application;
    }
}
