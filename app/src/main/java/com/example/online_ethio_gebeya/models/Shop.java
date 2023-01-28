package com.example.online_ethio_gebeya.models;

public class Shop {
    private long id;
    private String name;
    private Double latitude, longitude;

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
}
