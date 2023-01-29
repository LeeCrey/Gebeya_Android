package com.example.online_ethio_gebeya.models;

import androidx.annotation.NonNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Comment {
    @JsonProperty("id")
    private long id;

    @JsonProperty("content")
    private String content;

    public String getContent() {
        return content;
    }

    // custom
    @JsonIgnore
    public boolean hasTheSameContent(@NonNull Comment newItem) {
        return content.equals(newItem.getContent());
    }

    public long getId() {
        return id;
    }
}
