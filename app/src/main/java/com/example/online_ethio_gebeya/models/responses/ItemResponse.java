package com.example.online_ethio_gebeya.models.responses;

import com.example.online_ethio_gebeya.models.Item;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ItemResponse extends Item {
    @JsonProperty("cart_items")
    private List<Item> itemList;

    public List<Item> getItemList() {
        return itemList;
    }
}
