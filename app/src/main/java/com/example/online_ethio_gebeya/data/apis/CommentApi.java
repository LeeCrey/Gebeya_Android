package com.example.online_ethio_gebeya.data.apis;

import com.example.online_ethio_gebeya.models.responses.InstructionsResponse;

import retrofit2.Call;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface CommentApi {
    @Headers("accept: application/json")
    @POST("products/{product_id}/comments")
    Call<InstructionsResponse> sendProductComment(@Header("Authorization") String authToken,
                                                  @Path("product_id") long productId,
                                                  @Query("message") String message,
                                                  @Query("weight") float rate);
}
