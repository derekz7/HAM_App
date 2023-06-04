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
    private Patient selectedPatient;
    private Patient selectPatient;
    private Booking booking;
    private Booking booked;
    private Service selectService;
    private Service selectedService;
    private Doctor selectedDoctor;
    private Doctor selectDoctor;
    private Department selectedDepartment;
    private Department selectDepartment;
    private List<Booking> bookingList;
    private int[] selectedTime;

    public ApiDataManager() {

    }

    public Patient getSelectPatient() {
        return selectPatient;
    }

    public void setSelectPatient(Patient selectPatient) {
        this.selectPatient = selectPatient;
    }

    public Booking getBooked() {
        return booked;
    }

    public void setBooked(Booking booked) {
        this.booked = booked;
    }

    public Service getSelectService() {
        return selectService;
    }

    public void setSelectService(Service selectService) {
        this.selectService = selectService;
    }

    public Doctor getSelectDoctor() {
        return selectDoctor;
    }

    public void setSelectDoctor(Doctor selectDoctor) {
        this.selectDoctor = selectDoctor;
    }

    public Department getSelectDepartment() {
        return selectDepartment;
    }

    public void setSelectDepartment(Department selectDepartment) {
        this.selectDepartment = selectDepartment;
    }

    public int[] getSelectedTime() {
        return selectedTime;
    }

    public void setSelectedTime(int selectedSection, int selectedItem) {
        this.selectedTime = new int[]{
                selectedSection, selectedItem
        };
    }

    public Patient getSelectedPatient() {
        return selectedPatient;
    }

    public void setSelectedPatient(Patient patient) {
        this.selectedPatient = patient;
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
