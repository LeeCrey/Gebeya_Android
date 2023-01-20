package com.example.online_ethio_gebeya.models;

public class Shop {
    private Integer id;
    private String name;
    private Double latitude, longitude;

    // id
    public Integer getId() {
        return id;
    }

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
}
