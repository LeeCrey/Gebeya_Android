package com.example.online_ethio_gebeya.data.apis.account;

import com.example.online_ethio_gebeya.models.Customer;
import com.example.online_ethio_gebeya.models.responses.InstructionsResponse;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Url;

public interface RegistrationsApi {
    // create account
    @Headers("accept: application/json")
    @POST("customers")
    Call<InstructionsResponse> signUp(@Body Customer customer);

    // edit account
    @Multipart
    @PATCH("customers")
    Call<InstructionsResponse> updateAccount(@Header("Authorization") String authorizationToken,
                                             @Part("first_name") RequestBody firstName,
                                             @Part("last_name") RequestBody lastName,
                                             @Part("current_password") RequestBody currentPassword,
                                             @Part("profile") RequestBody profile);

    @Headers("accept: application/json")
    @GET("/customer")
    Call<Customer> getCurrentCustomer(@Header("Authorization") String authorizationToken);

    // edit account
    @Headers("accept: application/json")
    @GET
    Call<InstructionsResponse> confirmAccount(@Url String url);

    // delete account
    @Headers("accept: application/json")
    @DELETE("customers")
    Call<InstructionsResponse> destroyAccount(@Header("Authorization") String authorizationToken);
}
