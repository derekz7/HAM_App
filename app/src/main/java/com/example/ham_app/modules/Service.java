package com.example.ham_app.modules;

import com.google.gson.annotations.SerializedName;

public class Service {
    private String id;
    @SerializedName("name")
    private String serviceName;
    private String description;
    public int price;

    public Service() {
    }

    public Service(String id, String serviceName, String description, int price) {
        this.id = id;
        this.serviceName = serviceName;
        this.description = description;
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
