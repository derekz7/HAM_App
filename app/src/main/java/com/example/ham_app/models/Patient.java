package com.example.ham_app.models;

import com.google.gson.annotations.SerializedName;

public class Patient {
    private String id;
    @SerializedName("pt_name")
    private String patientName;

    private char gender;
    private String job;
    private String address;
    private String user_id;

    public Patient() {
    }

    public Patient(String patientName, char gender, String job, String address, String user_id) {
        this.id = id;
        this.patientName = patientName;
        this.gender = gender;
        this.job = job;
        this.address = address;
        this.user_id = user_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public char getGender() {
        return gender;
    }

    public void setGender(char gender) {
        this.gender = gender;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
