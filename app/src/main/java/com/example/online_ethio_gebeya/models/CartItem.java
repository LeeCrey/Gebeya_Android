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

    @JsonIgnore
    private boolean isChecked;

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

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public long getId() {
        return id;
    }
}
