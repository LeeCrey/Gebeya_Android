package com.example.online_ethio_gebeya.models;

import androidx.annotation.NonNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Comment {
    @JsonProperty("id")
    private long id;

    @JsonProperty("body")
    private String content;

    @JsonProperty("customer")
    private Customer customer;

    @JsonProperty("created_date")
    private String createdDate;

    public String getContent() {
        return content;
    }

    public long getId() {
        return id;
    }

    public boolean areSimilar(Comment newP) {
        boolean idSame = id == newP.getId();
        boolean contentSame = content.equals(newP.content);

        return idSame && contentSame;
    }

    public Customer getCustomer() {
        return customer;
    }

    public String getCreatedDate() {
        return createdDate;
    }
}
