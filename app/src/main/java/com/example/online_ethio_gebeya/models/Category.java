package com.example.online_ethio_gebeya.models;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.annotation.JsonSetter;

// non null
@JsonRootName("category")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Category {
    @JsonProperty("id")
    private Integer categoryId;

    @JsonProperty("name")
    private String name;

    @JsonProperty("amharic")
    private String amharic;

    @JsonProperty("selected")
    private Boolean isSelected = false;

    @JsonIgnore
    public String getName() {
        return name;
    }

    @JsonSetter("name")
    public void setName(String cars) {
        name = cars.trim();
    }

    @JsonIgnore
    public String getAmharic() {
        return amharic;
    }

    public void setAmharic(String amharic) {
        this.amharic = amharic;
    }

    @JsonIgnore
    public boolean isSelected() {
        return isSelected;
    }

    @JsonIgnore
    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    @JsonGetter("id")
    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }
}
