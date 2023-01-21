package com.example.online_ethio_gebeya.data.apis.account;

import com.example.online_ethio_gebeya.models.Customer;
import com.example.online_ethio_gebeya.models.responses.SessionResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface SessionsApi {
    @Headers("accept: application/json")
    @POST("customers/sign_in")
    Call<SessionResponse> login(@Body Customer customer, @Query("locale") String userLocale);

    @Headers("accept: application/json")
    @DELETE("customers/sign_out")
    Call<SessionResponse> logout(@Header("Authorization") String token);
}
