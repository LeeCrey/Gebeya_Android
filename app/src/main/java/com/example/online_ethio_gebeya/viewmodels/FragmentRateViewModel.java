package com.example.online_ethio_gebeya.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;

public class FragmentRateViewModel extends InstructionsViewModel {
    private long productId;

    public FragmentRateViewModel(@NonNull Application application) {
        super(application);
    }

    public void submitRatingWithComment(String cmt, float rating) {
        repository.makeRateRequest(cmt, rating, productId);
    }

    public void setAuthorization(String authToken) {
        repository.setAuthorizationToken(authToken);
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }
}
