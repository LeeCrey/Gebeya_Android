package com.example.online_ethio_gebeya.models.responses;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SessionResponse {
    @JsonProperty("full_name")
    private String fullName;

    private String authToken;

    @JsonProperty("okay")
    private Boolean okay;

    @JsonProperty("message")
    private String message;

    @JsonProperty("error")
    private String error;

    public Boolean getOkay() {
        return okay;
    }

    public void setOkay(Boolean okay) {
        this.okay = okay;
    }

    public String getMessage() {
        return message;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getFullName() {
        return fullName;
    }
}
