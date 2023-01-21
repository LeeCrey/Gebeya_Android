package com.example.online_ethio_gebeya.data.apis;

import com.example.online_ethio_gebeya.models.responses.CartResponse;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Path;

public interface CartApi {
    @Headers({"accept: application/json"})
    @GET("carts")
    Call<CartResponse> index(@Header("Authorization") String header);

    @Headers({"accept: application/json"})
    @DELETE("carts/{id}")
    Call<CartResponse> delete(@Header("Authorization") String header, @Path("id") int cartId);

    @Headers({"accept: application/json"})
    @DELETE("carts")
    Call<CartResponse> deleteAll(@Header("Authorization") String header);
}
