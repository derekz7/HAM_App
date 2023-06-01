package com.example.ham_app.models;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Booking {
    private String id;
    @SerializedName("order_num")
    private int orderNum;
    private Date date;
    private String time;
    private int price;
    private String status;
    private String pt_id;
    private String user_id;
    private String dc_id;
    private String sv_id;

    public Booking() {
    }

    public Booking(int orderNum, Date date, String time, int price, String status, String pt_id, String user_id, String dc_id, String sv_id) {
        this.orderNum = orderNum;
        this.date = date;
        this.time = time;
        this.price = price;
        this.status = status;
        this.pt_id = pt_id;
        this.user_id = user_id;
        this.dc_id = dc_id;
        this.sv_id = sv_id;
    }

    public String getId() {
        return id;
    }

    public int getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(int orderNum) {
        this.orderNum = orderNum;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPt_id() {
        return pt_id;
    }

    public void setPt_id(String pt_id) {
        this.pt_id = pt_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getDc_id() {
        return dc_id;
    }

    public void setDc_id(String dc_id) {
        this.dc_id = dc_id;
    }

    public String getSv_id() {
        return sv_id;
    }

    public void setSv_id(String sv_id) {
        this.sv_id = sv_id;
    }
}
