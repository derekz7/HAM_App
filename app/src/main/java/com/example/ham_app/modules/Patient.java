package com.example.ham_app.modules;

import android.annotation.SuppressLint;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Objects;


public class Patient {
    private String id;
    @SerializedName("pt_name")
    private String patientName;

    private String dob;
    private String gender;
    private String job;
    private String address;
    private String user_id;

    public Patient() {
    }

    public Patient(String patientName, String dob ,String gender, String job, String address, String user_id) {
        this.id = id;
        this.patientName = patientName;
        this.dob = dob;
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

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
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
