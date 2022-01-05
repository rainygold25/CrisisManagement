package com.example.crisismanagement;

public class resourceItem {
    private String name;
    private String address;
    private String contact_information;
    private String date;
    private String distance;
    private String image;

    public resourceItem(String name, String address, String contact_information, String date, String image, String distance) {
        this.name = name;
        this.address = address;
        this.contact_information = contact_information;
        this.date = date;
        this.image = image;
        this.distance = distance;
    }

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
