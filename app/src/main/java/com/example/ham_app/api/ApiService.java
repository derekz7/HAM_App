package com.example.ham_app.api;

import com.example.ham_app.modules.Appointment;
import com.example.ham_app.modules.Booking;
import com.example.ham_app.modules.Department;
import com.example.ham_app.modules.Doctor;
import com.example.ham_app.modules.News;
import com.example.ham_app.modules.Patient;
import com.example.ham_app.modules.Service;
import com.example.ham_app.modules.User;
import com.example.ham_app.untils.Common;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {
    Gson gson = new GsonBuilder().setDateFormat("dd/MM/yyyy").create();
    OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .readTimeout(30, TimeUnit.SECONDS)
            .connectTimeout(30, TimeUnit.SECONDS)
            .build();
    ApiService api = new Retrofit.Builder()
            .baseUrl(Common.baseUrl)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(okHttpClient)
            .build()
            .create(ApiService.class);


    @GET("api/User/Login")
    Call<User> login(@Query("username") String email, @Query("password") String password);

    @POST("api/User/CreateUser")
    Call<User> signUp(@Body User user);

    @GET("api/User/GetUserByUsername")
    Call<User> getUserByUsername(@Query("username") String username);

    //Patient
    @GET("api/Patient/GetPatientsByUser")
    Call<List<Patient>> getPatientByUser(@Query("userId") String userid);

    @GET("api/Patient/GetPatientsByUsername")
    Call<List<Patient>> getPatientByUsername(@Query("username") String username);

    //Tin tuc
    @GET("api/News/GetAllNews")
    Call<List<News>> getAllNews();

    @GET("api/News/Get3NewestNews")
    Call<List<News>> getNewestNews();

    //Department
    @GET("api/Department/GetAllDepartments")
    Call<List<Department>> getDepartments();

    @GET("api/Department/GetDepByDoctor")
    Call<Department> getDepByDoctorID(@Query("id") String docId);

    //Service
    @GET("api/Service/GetAllServices")
    Call<List<Service>> getAllServices();

    //Doctor
    @GET("api/Doctor/available")
    Call<List<Doctor>> getDoctorAvailable(@Query("dep_id") String dep_id, @Query("date") String date, @Query("time") String time);

    //Booking
    @POST("api/Booking/Create")
    Call<Booking> createBooking(@Body Booking booking);

    @GET("api/Booking/GetBookingByUsername")
    Call<List<Booking>> getBookingByUser(@Query("username") String username);

    @GET("api/Booking/GetBookingByUserId")
    Call<List<Booking>> getBookingByUserId(@Query("userid") String userid);

    //APPOINTMENT
    @GET("api/Booking/getAppointmentByBid")
    Call<Appointment> getAppointment(@Query("bid") String bid);
}
