package com.example.online_ethio_gebeya.models.responses;

import com.example.online_ethio_gebeya.models.CartItem;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class CartItemResponse extends CartItem {
    @JsonProperty("cart_items")
    private List<CartItem> itemList;

    public List<CartItem> getItemList() {
        return itemList;
    }
}
