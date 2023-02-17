package com.example.online_ethio_gebeya.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Shop {
    @JsonProperty("id")
    private long id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("latitude")
    private Double latitude;

    @JsonProperty("longitude")
    private Double longitude;

    @JsonProperty("difference")
    private Double difference;

    public Shop setId(Integer id) {
        this.id = id;
        return this;
    }

    // name
    public String getName() {
        return name;
    }

    public Shop setName(String name) {
        this.name = name;
        return this;
    }

    // latitude
    public Double getLatitude() {
        return latitude;
    }

    public Shop setLatitude(Double latitude) {
        this.latitude = latitude;
        return this;
    }

    // longitude
    public Double getLongitude() {
        return longitude;
    }

    public Shop setLongitude(Double longitude) {
        this.longitude = longitude;
        return this;
    }

    public long getId() {
        return id;
    }

    public Double getDifference() {
        return difference;
    }
}
