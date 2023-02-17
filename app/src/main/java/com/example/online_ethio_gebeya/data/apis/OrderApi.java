package com.example.online_ethio_gebeya.data.apis;

import com.example.online_ethio_gebeya.models.Order;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;

public interface OrderApi {
    @Headers({"accept: application/json"})
    @GET("orders")
    Call<List<Order>> index(@Header("Authorization") String token);

    @Headers({"accept: application/json"})
    @DELETE("orders/{id}")
    Call<List<Order>> destroy(@Header("Authorization") String token);
}
