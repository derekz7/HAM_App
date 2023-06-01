package com.example.ham_app.api;

import com.example.ham_app.models.Department;
import com.example.ham_app.models.News;
import com.example.ham_app.models.Service;
import com.example.ham_app.models.User;
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
            .connectTimeout(30,TimeUnit.SECONDS)
            .build();
    ApiService api = new Retrofit.Builder()
            .baseUrl(Common.baseUrl)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(okHttpClient)
            .build()
            .create(ApiService.class);


    @GET("api/User/Login")
    Call<Boolean> login(@Query("username") String email, @Query("password") String password);

    @POST("api/User/CreateUser")
    Call<User> signUp(@Body User user);

    @GET("api/User/GetUserByUsername")
    Call<User> getUserByUsername(@Query("username") String username);

    //Tin tuc
    @GET("api/News/GetAllNews")
    Call<List<News>> getAllNews();
    @GET("api/News/Get3NewestNews")
    Call<List<News>> getNewestNews();
    //Department
    @GET("api/Department/GetAllDepartments")
    Call<List<Department>> getAllDepartment();

    //Service
    @GET("api/Service/GetAllServices")
    Call<List<Service>> getAllServices();
}
