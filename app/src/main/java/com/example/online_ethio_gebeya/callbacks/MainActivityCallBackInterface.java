package com.example.online_ethio_gebeya.callbacks;

import android.location.Location;

import androidx.annotation.NonNull;

import com.example.online_ethio_gebeya.models.Product;

public interface MainActivityCallBackInterface {
    void closeKeyBoard();

    String getAuthorizationToken();

    void onProductClick(@NonNull Product product);

    int getFontSizeForDescription();

    void openEmailApp();

    void openGoogleMap(double latitude, double longitude);

    String getLocale();

    Location getLocation();
}
