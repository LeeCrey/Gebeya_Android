package com.example.online_ethio_gebeya.models.responses;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class InstructionsResponse {
    @JsonIgnore
    private boolean unlockPasswordConfirm;

    @JsonProperty("okay")
    private Boolean okay;

    @JsonProperty("message")
    private String message;

    public Boolean getOkay() {
        return okay;
    }

    public void setOkay(Boolean okay) {
        this.okay = okay;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isUnlockPasswordConfirm() {
        return unlockPasswordConfirm;
    }

    public void setUnlockPasswordConfirm(boolean unlockPasswordConfirm) {
        this.unlockPasswordConfirm = unlockPasswordConfirm;
    }
}
