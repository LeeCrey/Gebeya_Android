package com.example.online_ethio_gebeya.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Order {
    @JsonProperty("id")
    private Long id;

    @JsonProperty("status")
    private String status;

    @JsonProperty("shop_name")
    private String shopName;

    public Long getId() {
        return id;
    }

    public String getStatus() {
        return status;
    }

    public String getShopName() {
        return shopName;
    }
}
