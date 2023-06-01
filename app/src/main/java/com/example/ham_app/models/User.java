package com.example.ham_app.models;

import com.google.gson.annotations.SerializedName;

public class User {
    @SerializedName("id")
    private String id;
    @SerializedName("username")
    private String username;
    @SerializedName("name")
    private String fullName;
    @SerializedName("pw")
    private String password;
    @SerializedName("email")
    private String email;
    @SerializedName("role_id")
    private int role;
    @SerializedName("p_number")
    private String phoneNumber;
    @SerializedName("img")
    private String imgUrl;

    public User() {

    }
    public User(String username, String fullName, String password, String phoneNumber) {
        this.username = username;
        this.fullName = fullName;
        this.password = password;
        this.role = 2;
        this.phoneNumber = phoneNumber;
        this.imgUrl = null;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
