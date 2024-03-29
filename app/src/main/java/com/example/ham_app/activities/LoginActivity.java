package com.example.ham_app.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ham_app.R;
import com.example.ham_app.activities.doctors.DoctorMainActivity;
import com.example.ham_app.api.ApiService;
import com.example.ham_app.dialog.AlertDialog;
import com.example.ham_app.dialog.LoadingDialog;
import com.example.ham_app.modules.Department;
import com.example.ham_app.modules.User;
import com.example.ham_app.untils.ApiDataManager;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private EditText edtUsername, edt_password;
    private Button btn_signUp, btn_login;
    private AlertDialog alertDialog;
    private SharedPreferences sharedPreferences;
    private CheckBox cbRemember;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
        onClick();
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {
               getDepartment();
            }
        });
    }

    private void onClick() {
        btn_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = edtUsername.getText().toString();
                String password = edt_password.getText().toString();
                if (username.length() == 0) {
                    edtUsername.setError("Please enter your username.");
                } else if (password.length() == 0) {
                    edt_password.setError("Please enter your password.");
                } else {
                    LoadingDialog.show(LoginActivity.this);
                    ApiService.api.login(username, password).enqueue(new Callback<User>() {
                        @Override
                        public void onResponse(Call<User> call, Response<User> response) {
                            if (response.isSuccessful()) {
                                if (response.body() != null) {
                                    if (cbRemember.isChecked()) {
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putString("username", username);
                                        editor.putString("password", password);
                                        editor.putBoolean("checked", true);
                                        editor.apply();
                                    } else {
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putString("username", username);
                                        editor.remove("password");
                                        editor.putBoolean("checked", false);
                                        editor.apply();
                                    }
                                    ApiDataManager.getInstance().setUser(response.body());
                                    if (ApiDataManager.getInstance().getUser().getRole() == 2) {
                                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                    if (ApiDataManager.getInstance().getUser().getRole() == 1) {
                                        //Toast.makeText(LoginActivity.this, "Login to Main User", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(LoginActivity.this, DoctorMainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                    LoadingDialog.dismissDialog();
                                } else {
                                    alertDialog.show("Tên tài khoản hoặc mật khẩu không đúng.");
                                    LoadingDialog.dismissDialog();
                                }
                            } else {

                                LoadingDialog.dismissDialog();
                                startActivity(new Intent(LoginActivity.this, ErrorActivity.class));

                            }
                        }

                        @Override
                        public void onFailure(Call<User> call, Throwable t) {
                            Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                            Log.d("error", t.getMessage());
                            startActivity(new Intent(LoginActivity.this, ErrorActivity.class));
                            LoadingDialog.dismissDialog();
                        }
                    });
                }

            }
        });
    }

    private void init() {
        alertDialog = new AlertDialog(LoginActivity.this);
        edtUsername = findViewById(R.id.edt_username);
        edt_password = findViewById(R.id.edt_password);
        btn_signUp = findViewById(R.id.btn_signUp);
        btn_login = findViewById(R.id.btn_login);
        cbRemember = findViewById(R.id.cb_remember);
        sharedPreferences = getSharedPreferences("dataLogin", MODE_PRIVATE);
        edtUsername.setText(sharedPreferences.getString("username", ""));
        edt_password.setText(sharedPreferences.getString("password", ""));
        cbRemember.setChecked(sharedPreferences.getBoolean("checked", false));
    }

    @Override
    public void onBackPressed() {
        show();
    }

    public void show() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.custom_dialog_exit);
        dialog.setCancelable(true);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setGravity(Gravity.BOTTOM);
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().getAttributes().windowAnimations = R.style.BottomSheetAnimation;
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        final ImageButton btnExit = dialog.findViewById(R.id.btnExit);
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                System.exit(0);
            }
        });
        dialog.show();
    }

    private void getDepartment() {
        ApiService.api.getDepartments().enqueue(new Callback<List<Department>>() {
            @Override
            public void onResponse(Call<List<Department>> call, Response<List<Department>> response) {
                if (response.body() != null) {
                    ApiDataManager.getInstance().setDepartmentList(response.body());
                    Log.d("loadDep",response.body().size()+"");
                }
            }

            @Override
            public void onFailure(Call<List<Department>> call, Throwable t) {
            }
        });
    }
}