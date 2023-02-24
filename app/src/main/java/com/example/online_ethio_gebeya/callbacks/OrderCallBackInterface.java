package com.example.online_ethio_gebeya.callbacks;

import androidx.annotation.NonNull;

import com.example.online_ethio_gebeya.models.Order;

public interface OrderCallBackInterface {
    void onOrderClick(@NonNull Order order, int pos);
}
