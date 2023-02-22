package com.example.online_ethio_gebeya.data.apis;

import com.example.online_ethio_gebeya.models.Order;
import com.example.online_ethio_gebeya.models.responses.InstructionsResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface OrderApi {
    //    index
    @Headers({"accept: application/json"})
    @GET("orders")
    Call<List<Order>> index(@Header("Authorization") String token);

    //    create
    @Headers({"accept: application/json"})
    @POST("/carts/{id}/orders")
    Call<InstructionsResponse> createOrder(@Header("Authorization") String auth, @Path("id") long cartId);

    //    destroy
    @Headers({"accept: application/json"})
    @DELETE("orders/{id}")
    Call<List<Order>> destroy(@Header("Authorization") String token);
}
