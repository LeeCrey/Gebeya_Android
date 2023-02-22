package com.example.online_ethio_gebeya.callbacks;


import androidx.annotation.NonNull;

import com.example.online_ethio_gebeya.models.Cart;

public interface CartCallBackInterface {
    void onItemClicked(@NonNull Cart cart);
}
