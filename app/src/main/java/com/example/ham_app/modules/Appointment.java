package com.example.ham_app.modules;

import java.io.Serializable;

public class Appointment implements Serializable {
    private String id;
    private String bid;
    private String serviceName;
    private int orderNum;

    private String depName;
    private String room;
    private String dcName;
    private String date;
    private String time;
    private String ptName;
    private int price;
    private String status;


    public Appointment() {
    }

    public Appointment(String id, String bid, String serviceName, int orderNum, String depName, String room, String dcName, String date, String time, String ptName, int price, String status) {
        this.id = id;
        this.bid = bid;
        this.serviceName = serviceName;
        this.orderNum = orderNum;
        this.depName = depName;
        this.room = room;
        this.dcName = dcName;
        this.date = date;
        this.time = time;
        this.ptName = ptName;
        this.price = price;
        this.status = status;
    }

    public String getDepName() {
        return depName;
    }

    public void setDepName(String depName) {
        this.depName = depName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBid() {
        return bid;
    }

    public void setBid(String bid) {
        this.bid = bid;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public int getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(int orderNum) {
        this.orderNum = orderNum;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getDcName() {
        return dcName;
    }

    public void setDcName(String dcName) {
        this.dcName = dcName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPtName() {
        return ptName;
    }

    public void setPtName(String ptName) {
        this.ptName = ptName;
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
}
