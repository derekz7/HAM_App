package com.example.ham_app.modules;

import java.io.Serializable;

public class Prescription implements Serializable {
    private String id;
    private String disease;
    private  String symptoms;
    private  String medicines;
    private String ptu_medicines;
    private String user_id;
    private String ptName;
    private String dcName;

    public Prescription() {
    }

    public Prescription(String id, String disease, String symptoms, String medicines, String ptu_medicines, String user_id, String ptName, String dcName) {
        this.id = id;
        this.disease = disease;
        this.symptoms = symptoms;
        this.medicines = medicines;
        this.ptu_medicines = ptu_medicines;
        this.user_id = user_id;
        this.ptName = ptName;
        this.dcName = dcName;
    }

    public String getPtName() {
        return ptName;
    }

    public void setPtName(String ptName) {
        this.ptName = ptName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDisease() {
        return disease;
    }

    public void setDisease(String disease) {
        this.disease = disease;
    }

    public String getSymptoms() {
        return symptoms;
    }

    public void setSymptoms(String v) {
        this.symptoms = symptoms;
    }

    public String getMedicines() {
        return medicines;
    }

    public void setMedicines(String medicines) {
        this.medicines = medicines;
    }

    public String getPtu_medicines() {
        return ptu_medicines;
    }

    public void setPtu_medicines(String ptu_medicines) {
        this.ptu_medicines = ptu_medicines;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getDcName() {
        return dcName;
    }

    public void setDcName(String dcName) {
        this.dcName = dcName;
    }
}
