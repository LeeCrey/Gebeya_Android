package com.example.online_ethio_gebeya.models;

import com.fasterxml.jackson.annotation.JsonProperty;

//@JsonRootName("cart")
public class Cart {
    @JsonProperty("id")
    private Long id;

    @JsonProperty("name")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
