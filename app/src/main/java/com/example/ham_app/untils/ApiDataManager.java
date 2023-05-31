package com.example.ham_app.untils;

import com.example.ham_app.models.Department;
import com.example.ham_app.models.News;
import com.example.ham_app.models.User;

import java.util.ArrayList;
import java.util.List;

public  class ApiDataManager {
    public static ApiDataManager instance;
    private User user;
    private List<Department> departmentList;
    private List<News> newsList;

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
}
