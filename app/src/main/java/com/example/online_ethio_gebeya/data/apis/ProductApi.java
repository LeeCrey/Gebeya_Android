package com.example.online_ethio_gebeya.data.apis;

import com.example.online_ethio_gebeya.models.Category;
import com.example.online_ethio_gebeya.models.responses.ProductResponse;
import com.example.online_ethio_gebeya.models.responses.ProductShowResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ProductApi {
    // controllers/products_controllers.rb#method => index
    @Headers({"accept: application/json"})
    @GET("products")
    Call<ProductResponse> index(@Header("Authorization") String token,
                                @Query("category") String category,
                                @Query("latitude") double latitude,
                                @Query("longitude") double longitude);

    // controllers/products_controllers.rb#method => show
    @Headers({"accept: application/json"})
    @GET("products/{id}")
    Call<ProductShowResponse> show(@Header("Authorization") String token, @Path("id") long productId);

    // controllers/products_controller.rb#method => categories
    @Headers({"accept: application/json"})
    @GET("categories")
    Call<List<Category>> categories();

    @Headers({"accept: application/json"})
    @GET("search")
    Call<ProductResponse> searchProduct(@Header("Authorization") String token, @Query("q") String query, @Query("offset") int offSet);
}
