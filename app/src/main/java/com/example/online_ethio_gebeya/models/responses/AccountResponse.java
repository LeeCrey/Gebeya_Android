package com.example.online_ethio_gebeya.models.responses;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AccountResponse {
    @JsonProperty("okay")
    private Boolean okay;
    @JsonProperty("message")
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getOkay() {
        return okay;
    }

    public void setOkay(Boolean okay) {
        this.okay = okay;
    }
}
