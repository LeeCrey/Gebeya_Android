package com.example.online_ethio_gebeya.data.repositories.account;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.online_ethio_gebeya.data.RetrofitConnectionUtil;
import com.example.online_ethio_gebeya.data.apis.account.SessionsApi;
import com.example.online_ethio_gebeya.helpers.JsonHelper;
import com.example.online_ethio_gebeya.models.Customer;
import com.example.online_ethio_gebeya.models.responses.SessionResponse;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SessionsRepository {
    private static final String TAG = "SessionsRepository";
    private MutableLiveData<SessionResponse> mSessionResult;
    private MutableLiveData<SessionResponse> mLogoutResult;
    private Call<SessionResponse> sessionResultCall;
    private SessionsApi api;

    public SessionsRepository(@NonNull Application application) {
        if (mSessionResult != null) {
            return;
        }

        mSessionResult = new MutableLiveData<>();
        mLogoutResult = new MutableLiveData<>();
        api = RetrofitConnectionUtil.getRetrofitInstance(application).create(SessionsApi.class);
    }

    public LiveData<SessionResponse> getLogoutResult() {
        return mLogoutResult;
    }

    public LiveData<SessionResponse> getSessionResult() {
        return mSessionResult;
    }

    // BEGIN APIs implementation
    public void login(@NonNull Customer customer, String locale) {
        cancelConnection();

        sessionResultCall = api.login(customer, locale);
        sessionResultCall.enqueue(new Callback<SessionResponse>() {
            @Override
            public void onResponse(@NonNull Call<SessionResponse> call, @NonNull Response<SessionResponse> response) {
                if (response.isSuccessful()) {
                    SessionResponse sessionResponse = response.body();
                    if (sessionResponse != null) {
                        sessionResponse.setOkay(true);
                        sessionResponse.setAuthToken(response.headers().get("Authorization"));
                    }
                    mSessionResult.postValue(sessionResponse);
                } else {
                    ResponseBody body = response.errorBody();
                    if (body != null) {
                        try {
                            SessionResponse loginErrorResponse = JsonHelper.parseLoginError(body.string());
                            loginErrorResponse.setOkay(false);
                            mSessionResult.postValue(loginErrorResponse);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<SessionResponse> call, @NonNull Throwable t) {
                SessionResponse resp = null;
                if (t instanceof SocketTimeoutException) {
                    resp = getErrorMessage("Server timeout.");
                } else if (t instanceof UnknownHostException) {
                    resp = getErrorMessage("Unknown host.");
                } else if (call.isCanceled()) {
                    resp = getErrorMessage("Cancelled connection.");
                } else {
                    resp = getErrorMessage(t.getMessage());
                }
                mSessionResult.postValue(resp);
            }
        });
    }

    public void logout(String authorizationToken) {
        cancelConnection();

        sessionResultCall = api.logout(authorizationToken);
        sessionResultCall.enqueue(new Callback<SessionResponse>() {
            @Override
            public void onResponse(@NonNull Call<SessionResponse> call, @NonNull Response<SessionResponse> response) {
                if (response.isSuccessful()) {
                    mLogoutResult.postValue(response.body());
                } else {
                    mLogoutResult.postValue(getErrorMessage("Un-authorized"));
                }
            }

            @Override
            public void onFailure(@NonNull Call<SessionResponse> call, @NonNull Throwable t) {
                String a = String.valueOf(t instanceof IOException);
                mLogoutResult.postValue(getErrorMessage(a));
            }
        });
    }
    // END APIs implementation

    private SessionResponse getErrorMessage(String msg) {
        SessionResponse result = new SessionResponse();
        result.setOkay(false);
        result.setError(msg);

        return result;
    }

    public void cancelConnection() {
        if (null != sessionResultCall) {
            sessionResultCall.cancel();
        }
    }

    public void nullifyData() {
        mSessionResult.setValue(null);
        mLogoutResult.setValue(null);
    }
}
