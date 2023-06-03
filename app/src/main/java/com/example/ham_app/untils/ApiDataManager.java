package com.example.ham_app.untils;

import com.example.ham_app.modules.Department;
import com.example.ham_app.modules.News;
import com.example.ham_app.modules.Patient;
import com.example.ham_app.modules.Service;
import com.example.ham_app.modules.User;

import java.util.List;

public  class ApiDataManager {
    public static ApiDataManager instance;
    private User user;
    private List<Department> departmentList;
    private List<News> newsList;
    private List<Service> serviceList;
    private List<Patient> patientList;
    public ApiDataManager() {

    }

    public static ApiDataManager getInstance() {
        if (instance == null) {
            instance = new ApiDataManager();
        }
        return instance;
    }

    public List<Department> getDepartmentList() {
        return departmentList;
    }


    public void setDepartmentList(List<Department> departmentList) {
        this.departmentList = departmentList;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<News> getNewsList() {
        return newsList;
    }

    public void setNewsList(List<News> newsList) {
        this.newsList = newsList;
    }
    public List<Service> getServiceList() {
        return serviceList;
    }
    public void setServiceList(List<Service> serviceList) {
        this.serviceList = serviceList;
    }

    public List<Patient> getPatientList() {
        return patientList;
    }

    public void setPatientList(List<Patient> patientList) {
        this.patientList = patientList;
    }
}
