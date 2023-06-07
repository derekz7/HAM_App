package com.example.ham_app.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import com.example.ham_app.R;
import com.example.ham_app.activities.doctors.DoctorMainActivity;
import com.example.ham_app.api.ApiService;
import com.example.ham_app.modules.Department;
import com.example.ham_app.modules.User;
import com.example.ham_app.untils.ApiDataManager;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPreferences = getSharedPreferences("dataLogin", MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "");
        String password = sharedPreferences.getString("password", "");
        if (username.length() > 0 && password.length() > 0) {
            ApiService.api.login(username, password).enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if (response.body() != null) {
                        if (response.body().getRole() == 2) {
                            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();

                        }
                        if (response.body().getRole() == 1) {
                            Intent intent = new Intent(SplashActivity.this, DoctorMainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        ApiDataManager.getInstance().setUser(response.body());
                    } else {
                        Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }

                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                    finish();
                }
            });
        } else {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }

        ExecutorService executor = Executors.newSingleThreadExecutor();
        //Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(new Runnable() {
            @Override
            public void run() {
                ApiService.api.getDepartments().enqueue(new Callback<List<Department>>() {
                    @Override
                    public void onResponse(Call<List<Department>> call, Response<List<Department>> response) {
                        if (response.body() != null) {
                            ApiDataManager.getInstance().setDepartmentList(response.body());
                        }

                    }

                    @Override
                    public void onFailure(Call<List<Department>> call, Throwable t) {

                    }
                });
            }
        });
    }
}