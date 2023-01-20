package com.example.online_ethio_gebeya.models.responses;

import com.example.online_ethio_gebeya.models.Cart;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class CartResponse {
    @JsonProperty("okay")
    private Boolean okay;

    @JsonProperty("message")
    private String message;

    @JsonIgnore
    private int errorCode;

    @JsonProperty("carts")
    private List<Cart> list;

    public Boolean getOkay() {
        return okay;
    }

    public String getMessage() {
        return message;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public List<Cart> getList() {
        return list;
    }
}
