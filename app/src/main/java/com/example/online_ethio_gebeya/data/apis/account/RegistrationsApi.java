package com.example.online_ethio_gebeya.data.apis.account;

import com.example.online_ethio_gebeya.models.Customer;
import com.example.online_ethio_gebeya.models.responses.InstructionsResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface RegistrationsApi {
    // create account
    @Headers("accept: application/json")
    @POST("customers")
    Call<InstructionsResponse> signUp(@Body Customer customer);

    // edit account
    @Headers("accept: application/json")
    @PATCH("customers")
    Call<InstructionsResponse> updateAccount(@Header("Authorization") String authorizationToken, @Body Customer customer);

    @Headers("accept: application/json")
    @GET("/customer")
    Call<Customer> getCurrentCustomer(@Header("Authorization") String authorizationToken);

    // edit account
    @Headers("accept: application/json")
    @GET
    Call<InstructionsResponse> confirmAccount(@Url String url);

    @Headers("accept: application/json")
    @POST("customers/password")
    Call<InstructionsResponse> changePassword(@Body Customer customer);
}
