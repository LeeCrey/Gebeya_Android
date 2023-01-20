package com.example.online_ethio_gebeya.models.responses;

import com.example.online_ethio_gebeya.models.Product;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ProductResponse {
    @JsonProperty("products")
    private List<Product> products;

    @JsonProperty("trending")
    private List<Product> trending;

    @JsonProperty("recommended")
    private List<Product> recommended;

    public List<Product> getProducts() {
        return products;
    }

    public List<Product> getTrending() {
        return trending;
    }

    public List<Product> getRecommended() {
        return recommended;
    }
}
