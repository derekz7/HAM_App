package com.example.ham_app.modules;

public class Relative {
    private String id;
    private String name;
    private String address;
    private String p_number;
    private String Pt_id;

    public Relative() {
    }

    public Relative(String id, String name, String address, String p_number, String pt_id) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.p_number = p_number;
        Pt_id = pt_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getP_number() {
        return p_number;
    }

    public void setP_number(String p_number) {
        this.p_number = p_number;
    }

    public String getPt_id() {
        return Pt_id;
    }

    public void setPt_id(String pt_id) {
        Pt_id = pt_id;
    }
}
