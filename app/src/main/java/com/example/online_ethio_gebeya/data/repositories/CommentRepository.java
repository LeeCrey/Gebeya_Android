package com.example.online_ethio_gebeya.data.repositories;

import android.app.Application;

import androidx.annotation.NonNull;

import com.example.online_ethio_gebeya.data.RetrofitConnectionUtil;
import com.example.online_ethio_gebeya.data.apis.CommentApi;
import com.example.online_ethio_gebeya.models.responses.InstructionsResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentRepository {
    private final CommentApi api;
    private Call<InstructionsResponse> commentCall;
    private String authorizationToken;

    public CommentRepository(@NonNull Application application) {
        api = RetrofitConnectionUtil.getRetrofitInstance(application).create(CommentApi.class);
    }

    public void sendComment(long productId, String comment, float rateValue) {
        cancelConnection();

        commentCall = api.sendProductComment(authorizationToken, productId, comment, rateValue);
        commentCall.enqueue(new Callback<InstructionsResponse>() {
            @Override
            public void onResponse(@NonNull Call<InstructionsResponse> call, @NonNull Response<InstructionsResponse> response) {

            }

            @Override
            public void onFailure(@NonNull Call<InstructionsResponse> call, @NonNull Throwable t) {

            }
        });
    }

    public void cancelConnection() {
        if (commentCall != null) {
            commentCall.cancel();
        }
    }

    public void setAuthorizationToken(String authorizationToken) {
        this.authorizationToken = authorizationToken;
    }
}
