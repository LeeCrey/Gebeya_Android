package com.example.online_ethio_gebeya.callbacks;

import androidx.annotation.NonNull;

import com.example.online_ethio_gebeya.models.Product;

public interface MainActivityCallBackInterface {
    void closeKeyBoard();

    String getAuthorizationToken();

    void checkPermission();

    void onProductClick(@NonNull Product product);

    int getFontSizeForDescription();

    void openEmailApp();

    void openLocation(float latitude, float longitude);

    String getLocale();
}
