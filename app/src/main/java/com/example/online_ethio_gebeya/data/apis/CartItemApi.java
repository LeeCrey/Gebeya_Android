package com.example.online_ethio_gebeya.data.apis;

import com.example.online_ethio_gebeya.models.Cart;
import com.example.online_ethio_gebeya.models.CartItem;
import com.example.online_ethio_gebeya.models.responses.CartItemResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface CartItemApi {
    // index
    @Headers("accept: application/json")
    @GET("carts/{cart_id}/cart_items")
    Call<List<CartItem>> getCartList(@Header("Authorization") String authorization, @Path("cart_id") long cartId);

    // create
    @Headers("accept: application/json")
    @POST("cart_items")
    Call<CartItemResponse> addItem(@Header("Authorization") String authorization, @Query("product_id") long id);

    // delete
    @Headers("accept: application/json")
    @DELETE("cart_items/{id}")
    Call<CartItemResponse> deleteItem(@Header("Authorization") String authorization, @Path("id") long cartItemId);

    // update
    @Headers("accept: application/json")
    @PATCH("cart_items/{id}")
    Call<CartItemResponse> updateItem(@Header("Authorization") String authorization, @Body Cart cart);
}
