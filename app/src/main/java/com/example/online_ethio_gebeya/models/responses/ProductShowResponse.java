package com.example.online_ethio_gebeya.models.responses;

import com.example.online_ethio_gebeya.models.Comment;
import com.example.online_ethio_gebeya.models.Product;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

// show page
public class ProductShowResponse {
    @JsonProperty("product")
    private Product product;

    @JsonProperty("related_products")
    private List<Product> relatedProducts;

    @JsonProperty("comments")
    private List<Comment> comments;

    public Product getProduct() {
        return product;
    }

    public List<Product> getRelatedProducts() {
        return relatedProducts;
    }

    public List<Comment> getComments() {
        return comments;
    }
}
