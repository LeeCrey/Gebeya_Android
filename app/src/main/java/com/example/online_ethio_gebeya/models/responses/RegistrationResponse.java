package com.example.online_ethio_gebeya.models.responses;

import com.example.online_ethio_gebeya.models.FormErrors;
import com.fasterxml.jackson.annotation.JsonProperty;

public class RegistrationResponse {
    @JsonProperty("okay")
    private Boolean okay;

    @JsonProperty("message")
    private String msg;

    @JsonProperty("errors")
    private FormErrors errors;

    public Boolean getOkay() {
        return okay;
    }

    public void setOkay(Boolean okay) {
        this.okay = okay;
    }

    public FormErrors getErrors() {
        return errors;
    }

    public void setErrors(FormErrors errors) {
        this.errors = errors;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
