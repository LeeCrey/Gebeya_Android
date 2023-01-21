package com.example.online_ethio_gebeya.data.apis;

import com.example.online_ethio_gebeya.models.Customer;
import com.example.online_ethio_gebeya.models.responses.InstructionsResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
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
}
