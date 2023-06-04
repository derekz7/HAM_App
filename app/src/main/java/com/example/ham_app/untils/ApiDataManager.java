package com.example.ham_app.untils;

import com.example.ham_app.modules.Booking;
import com.example.ham_app.modules.Department;
import com.example.ham_app.modules.Doctor;
import com.example.ham_app.modules.News;
import com.example.ham_app.modules.Patient;
import com.example.ham_app.modules.Service;
import com.example.ham_app.modules.User;

import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;

public class ApiDataManager {
    public static ApiDataManager instance;
    private User user;
    private List<Department> departmentList;
    private List<News> newsList;
    private List<Service> serviceList;
    private List<Patient> patientList;
    private Patient selectedpatient;
    private Booking booking;
    private Service selectedService;
    private Doctor selectedDoctor;
    private Department selectedDepartment;
    private List<Booking> bookingList;
    private int[] selectedTime;

    public ApiDataManager() {

    }

    public int[] getSelectedTime() {
        return selectedTime;
    }

    public void setSelectedTime(int selectedSection, int selectedItem) {
        this.selectedTime = new int[]{
                selectedSection, selectedItem
        };
    }

    public Patient getSelectedpatient() {
        return selectedpatient;
    }

    public void setSelectedpatient(Patient patient) {
        this.selectedpatient = patient;
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

    public Booking getBooking() {
        return booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }

    public List<Booking> getBookingList() {
        return bookingList;
    }

    public void setBookingList(List<Booking> bookingList) {
        this.bookingList = bookingList;
    }

    public Service getSelectedService() {
        return selectedService;
    }

    public void setSelectedService(Service selectedService) {
        this.selectedService = selectedService;
    }

    public Doctor getSelectedDoctor() {
        return selectedDoctor;
    }

    public void setSelectedDoctor(Doctor selectedDoctor) {
        this.selectedDoctor = selectedDoctor;
    }

    public Department getSelectedDepartment() {
        return selectedDepartment;
    }

    public void setSelectedDepartment(Department selectedDepartment) {
        this.selectedDepartment = selectedDepartment;
    }
}
