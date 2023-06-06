package com.example.ham_app.modules;

public class Doctor {
    private String id;
    private String name;
    private String room;
    private String dep_id;

    public Doctor() {
    }

    public Doctor(String id, String name, String room, String dep_id) {
        this.id = id;
        this.name = name;
        this.room = room;
        this.dep_id = dep_id;
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

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getDep_id() {
        return dep_id;
    }

    public void setDep_id(String dep_id) {
        this.dep_id = dep_id;
    }

}
