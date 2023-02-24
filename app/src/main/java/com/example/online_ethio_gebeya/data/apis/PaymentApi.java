package com.example.online_ethio_gebeya.data.apis;

import com.example.online_ethio_gebeya.models.Item;
import com.example.online_ethio_gebeya.models.responses.InstructionsResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface PaymentApi {
    @Headers("accept: application/json")
    @POST("/orders/{order_id}/payments")
    Call<InstructionsResponse> makePayment(@Header("Authorization") String authToken, @Path("order_id") long orderId);

    @Headers({"accept: application/json"})
    @GET("orders/{order_id}/items")
    Call<List<Item>> getItemList(@Header("Authorization") String token, @Path("order_id") long orderId);
}
