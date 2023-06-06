package com.example.ham_app.modules;

public class Prescription {
    private String id;
    private String disease;
    private  String symtoms;
    private  String medicines;
    private String ptu_medicines;
    private String user_id;
    private String dc_id;

    public Prescription() {
    }

    public Prescription(String id, String disease, String symtoms, String medicines, String ptu_medicines, String user_id, String dc_id) {
        this.id = id;
        this.disease = disease;
        this.symtoms = symtoms;
        this.medicines = medicines;
        this.ptu_medicines = ptu_medicines;
        this.user_id = user_id;
        this.dc_id = dc_id;
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

    public String getSymtoms() {
        return symtoms;
    }

    public void setSymtoms(String symtoms) {
        this.symtoms = symtoms;
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

    public String getDc_id() {
        return dc_id;
    }

    public void setDc_id(String dc_id) {
        this.dc_id = dc_id;
    }
}
