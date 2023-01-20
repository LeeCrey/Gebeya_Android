package com.example.online_ethio_gebeya.models.responses;

import com.example.online_ethio_gebeya.models.Product;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

// show page
public class ProductShowResponse {
    @JsonProperty("product")
    private Product product;

    @JsonProperty("related_products")
    private List<Product> relatedProducts;

    public Product getProduct() {
        return product;
    }

    public List<Product> getRelatedProducts() {
        return relatedProducts;
    }
}
