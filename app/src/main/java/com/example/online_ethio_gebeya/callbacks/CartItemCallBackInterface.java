package com.example.online_ethio_gebeya.callbacks;

import androidx.annotation.NonNull;

import com.example.online_ethio_gebeya.models.CartItem;

public interface CartItemCallBackInterface {
    void onCartItemClick(@NonNull CartItem cartItem);
}
