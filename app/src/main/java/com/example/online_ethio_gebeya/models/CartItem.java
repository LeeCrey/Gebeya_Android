package com.example.online_ethio_gebeya.models;

import androidx.annotation.NonNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class CartItem {
    @JsonProperty("id")
    private long id;

    @JsonProperty("quantity")
    private Integer quantity;

    @JsonProperty("product")
    private Product product;

    @JsonProperty("item_image")
    private String itemImage;

    public Integer getQuantity() {
        return quantity;
    }

    public Product getProduct() {
        return product;
    }

    //    Custom method
    @JsonIgnore
    public boolean areContentsTheSame(@NonNull CartItem newItem) {
        boolean qSame = Objects.equals(quantity, newItem.getQuantity());
        boolean pSame = product.isContentTheSame(newItem.getProduct());

        return qSame && pSame;
    }

    public String getItemImage() {
        return itemImage;
    }

    public long getId() {
        return id;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public void setItemImage(String itemImage) {
        this.itemImage = itemImage;
    }
}
