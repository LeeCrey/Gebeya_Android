package com.example.online_ethio_gebeya.models;

import androidx.annotation.NonNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import java.util.List;
import java.util.Objects;

@JsonRootName("product")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Product {
    @JsonProperty("id")
    private Integer id;

    @JsonProperty("quantity")
    private Integer quantity;

    @JsonProperty("name")
    private String name;

    @JsonProperty("rates")
    private Float rate;

    @JsonProperty("price")
    private Double price;

    @JsonProperty("discount")
    private Double discount;

    @JsonProperty("photos")
    private List<String> images;

    @JsonProperty("origin")
    private String origin;

    @JsonProperty("description")
    private String description;

    @JsonProperty("shop")
    private Shop shop;

    @JsonProperty("numberOfRates")
    private Integer numberOfRates;

    public Product() {
    }

    // id
    public Integer getId() {
        return id;
    }

    // name
    public String getName() {
        return name;
    }

    // shop
    public Shop getShop() {
        return shop;
    }

    // rate
    public Float getRate() {
        return rate;
    }

    public String getOrigin() {
        return origin;
    }

    public String getDescription() {
        return description;
    }

    //
    public Integer getNumberOfRates() {
        return numberOfRates;
    }

    @JsonIgnore
    public List<String> getImages() {
        return images;
    }

    public Integer getQuantity() {
        return quantity;
    }

    @NonNull
    @Override
    public String toString() {
        return name;
    }

    public boolean isContentTheSame(Product newProduct) {
        boolean nameTheSame = Objects.equals(getName(), newProduct.getName());
        boolean priceSame = Objects.equals(getPrice(), newProduct.getPrice());
        boolean idSame = Objects.equals(getId(), newProduct.getId());

        return nameTheSame && priceSame && idSame;
    }

    public boolean isSimilar(Product newProduct) {
        return Objects.equals(getId(), newProduct.getId());
    }

    public Double getPrice() {
        return price;
    }

    public Double getDiscount() {
        return discount;
    }

    public String getThumbnail() {
        return images.get(0);
    }
}
