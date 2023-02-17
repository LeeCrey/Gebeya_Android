package com.example.online_ethio_gebeya.data.repositories.account;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.online_ethio_gebeya.data.RetrofitConnectionUtil;
import com.example.online_ethio_gebeya.data.apis.InstructionsApi;
import com.example.online_ethio_gebeya.helpers.JsonHelper;
import com.example.online_ethio_gebeya.models.Customer;
import com.example.online_ethio_gebeya.models.responses.InstructionsResponse;
import com.fasterxml.jackson.core.JsonProcessingException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InstructionRepository {
    private final MutableLiveData<InstructionsResponse> mInstructionResponse;
    private final InstructionsApi api;
    private Call<InstructionsResponse> instructionsResponseCall;
    private static final String TAG = "InstructionRepository";

    private String authorizationToken;

    public InstructionRepository(@NonNull Application application) {
        api = RetrofitConnectionUtil.getRetrofitInstance(application).create(InstructionsApi.class);
        mInstructionResponse = new MutableLiveData<>();
    }

    public MutableLiveData<InstructionsResponse> getInstructionResponse() {
        return mInstructionResponse;
    }

    // APIs
    public void sendRequest(@NonNull Customer customer, @NonNull String path) {
        cancelConnection();

        instructionsResponseCall = api.sendInstruction(path, customer);
        instructionsResponseCall.enqueue(new Callback<InstructionsResponse>() {
            @Override
            public void onResponse(@NonNull Call<InstructionsResponse> call, @NonNull Response<InstructionsResponse> response) {
                final InstructionsResponse resp = response.body();
                if (resp != null) {
                    resp.setUnlockPasswordConfirm(true);
                    mInstructionResponse.postValue(resp);
                }
            }

            @Override
            public void onFailure(@NonNull Call<InstructionsResponse> call, @NonNull Throwable t) {
                InstructionsResponse response = new InstructionsResponse();
                response.setOkay(false);
                response.setMessage(t.getMessage());
                mInstructionResponse.postValue(response);
            }
        });
    }

    // confirm
    public void finishInstructionRequest(@NonNull String unlockPath) {
        cancelConnection();

        instructionsResponseCall = api.finishInstruction(unlockPath);
        instructionsResponseCall.enqueue(new Callback<InstructionsResponse>() {
            @Override
            public void onResponse(@NonNull Call<InstructionsResponse> call, @NonNull Response<InstructionsResponse> response) {
                InstructionsResponse resp = null;
                if (response.isSuccessful()) {
                    resp = response.body();
                    if (resp != null) {
                        resp.setOkay(true);
                    }
                } else {
                    ResponseBody body = response.errorBody();
                    if (body != null) {
                        try {
                            resp = JsonHelper.parseOperationError(body.toString());
                            resp.setOkay(false);
                        } catch (JsonProcessingException e) {
                            e.printStackTrace();
                        }
                    }
                }
                mInstructionResponse.postValue(resp);
            }

            @Override
            public void onFailure(@NonNull Call<InstructionsResponse> call, @NonNull Throwable t) {
                InstructionsResponse response = new InstructionsResponse();
                response.setOkay(false);
                response.setMessage(t.getMessage());
                mInstructionResponse.postValue(response);
            }
        });
    }

    public void nullifyObserverData() {
        mInstructionResponse.setValue(null);
    }

    // odd
    // feedback
    public void sendFeedback(String header, String message) {
        cancelConnection();

        instructionsResponseCall = api.sendFeedback(header, message);
        instructionsResponseCall.enqueue(new Callback<InstructionsResponse>() {
            @Override
            public void onResponse(@NonNull Call<InstructionsResponse> call, @NonNull Response<InstructionsResponse> response) {
                if (response.isSuccessful()) {
                    final InstructionsResponse resp = response.body();
                    if (resp != null) {
                        mInstructionResponse.postValue(resp);
                    }
                } else {
                    InstructionsResponse instructionsResponse = new InstructionsResponse();
                    instructionsResponse.setOkay(false);
                    instructionsResponse.setMessage("Un-Authorized request");
                }
            }

            @Override
            public void onFailure(@NonNull Call<InstructionsResponse> call, @NonNull Throwable t) {
                InstructionsResponse response = new InstructionsResponse();
                response.setOkay(false);
                response.setMessage(t.getMessage());
                mInstructionResponse.postValue(response);
            }
        });
    }

    public void makeRateRequest(String cmt, float rating, long id) {
        cancelConnection();

        instructionsResponseCall = api.sendRatingAndComment(authorizationToken, id, cmt, rating);
        instructionsResponseCall.enqueue(new Callback<InstructionsResponse>() {
            @Override
            public void onResponse(@NonNull Call<InstructionsResponse> call, @NonNull Response<InstructionsResponse> response) {
                if (response.isSuccessful()) {
                    mInstructionResponse.postValue(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<InstructionsResponse> call, @NonNull Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    public void cancelConnection() {
        if (null != instructionsResponseCall) {
            if (!(instructionsResponseCall.isExecuted() || instructionsResponseCall.isCanceled())) {
                instructionsResponseCall.cancel();
            }
        }
    }

    public void setAuthorizationToken(String authToken) {
        authorizationToken = authToken;
    }
}
