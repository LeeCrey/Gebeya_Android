package com.example.online_ethio_gebeya.data.apis;

import com.example.online_ethio_gebeya.models.Customer;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;

public interface WalletApi {
    @Headers({"accept: application/json"})
    @GET("balance")
    Call<Customer> balance(@Header("Authorization") String auth);
}
