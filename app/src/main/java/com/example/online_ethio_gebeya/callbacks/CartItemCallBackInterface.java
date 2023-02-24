package com.example.online_ethio_gebeya.callbacks;

import androidx.annotation.NonNull;

import com.example.online_ethio_gebeya.models.Item;

public interface CartItemCallBackInterface {
    void onCartItemClick(@NonNull Item item, int position);
}
