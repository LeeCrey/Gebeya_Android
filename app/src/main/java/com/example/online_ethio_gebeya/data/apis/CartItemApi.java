package com.example.online_ethio_gebeya.data.apis;

import com.example.online_ethio_gebeya.models.Item;
import com.example.online_ethio_gebeya.models.responses.InstructionsResponse;
import com.example.online_ethio_gebeya.models.responses.ItemResponse;

import java.util.List;

import retrofit2.Call;
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
    Call<List<Item>> getCartList(@Header("Authorization") String authorization, @Path("cart_id") long cartId);

    // create
    @Headers("accept: application/json")
    @POST("cart_items")
    Call<ItemResponse> addItem(@Header("Authorization") String authorization, @Query("product_id") long id, @Query("quantity") int gt);

    // delete
    @Headers("accept: application/json")
    @DELETE("cart_items/{id}")
    Call<ItemResponse> deleteItem(@Header("Authorization") String authorization, @Path("id") long cartItemId);

    // update
    @Headers("accept: application/json")
    @PATCH("cart_items/{id}")
    Call<InstructionsResponse> updateItem(@Header("Authorization") String authorization, @Path("id") long carItemId, @Query("quantity") int quantity);
}
