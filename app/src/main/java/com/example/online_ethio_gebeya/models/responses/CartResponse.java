package com.example.online_ethio_gebeya.models.responses;

import com.example.online_ethio_gebeya.models.Cart;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class CartResponse {
    @JsonProperty("okay")
    private Boolean okay;

    @JsonProperty("message")
    private String message;

    @JsonProperty("carts")
    private List<Cart> list;

    public Boolean getOkay() {
        return okay;
    }

    public String getMessage() {
        return message;
    }

    public List<Cart> getList() {
        return list;
    }
}
