package com.example.crisismanagement;

import java.util.List;

public class resourceItem {
    private String name;
    private String address;
    private String contact_information;
    private String date;
    private String distance;
    private String image;
    private List<String> items;
    private List<String> quantities;
    private List<String> categories;
    private String ratings;
    private String geo_location;

    public resourceItem(String name, String address, String contact_information, String date, String image, String distance, List<String> items, List<String> quantities, List<String> categories, String ratings, String geo_location) {
        this.name = name;
        this.address = address;
        this.contact_information = contact_information;
        this.date = date;
        this.image = image;
        this.distance = distance;
        this.items = items;
        this.quantities = quantities;
        this.categories = categories;
        this.ratings = ratings;
        this.geo_location = geo_location;
    }

    public String get_geo_location() {return geo_location;}

    public void set_geo_location(String geo_location) {this.geo_location = geo_location;}

    public List<String> getItems() {return items;}

    public void setItems(List<String> items) {this.items = items;}

    public List<String> getQuantities() {return quantities;}

    public void setQuantities(List<String> quantities) {this.quantities = quantities;}

    public List<String> getCategories() {return categories;}

    public void setCategories(List<String> categories) {this.categories = categories;}

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContact_information() {
        return contact_information;
    }

    public void setContact_information(String contact_information) {
        this.contact_information = contact_information;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
