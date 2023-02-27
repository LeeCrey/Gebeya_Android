package com.example.online_ethio_gebeya.data.apis;

import com.example.online_ethio_gebeya.models.Customer;
import com.example.online_ethio_gebeya.models.responses.InstructionsResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface InstructionsApi {
    /* possible routes
    //   => customers/password
    //   => customers/confirmation
    //   => customers/unlock
    */
    @Headers("accept: application/json")
    @POST
    Call<InstructionsResponse> sendInstruction(@Url String url, @Body Customer customer);

    @Headers("accept: application/json")
    @GET
    Call<InstructionsResponse> finishInstruction(@Url String url);

    /// odd here
    @Headers("accept: application/json")
    @POST("feedbacks")
    Call<InstructionsResponse> sendFeedback(@Header("Authorization") String header, @Body String feedback);

    @Headers("accept: application/json")
    @POST("products/{id}/comments")
    Call<InstructionsResponse> sendRatingAndComment(@Header("Authorization") String header, @Path("id") long id,
                                                    @Query("message") String cmd, @Query("weight") float weight);

    // delete account
    @Headers("accept: application/json")
    @DELETE("customers")
    Call<InstructionsResponse> deleteAccount(@Header("Authorization") String authorizationToken, @Query("password") String pawd);
}
