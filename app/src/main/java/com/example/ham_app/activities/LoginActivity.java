package com.example.ham_app.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ham_app.R;
import com.example.ham_app.api.ApiService;
import com.example.ham_app.dialog.AlertDialog;
import com.example.ham_app.dialog.LoadingDialog;
import com.example.ham_app.modules.Department;
import com.example.ham_app.modules.User;
import com.example.ham_app.untils.ApiDataManager;

import java.util.List;

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
    }

    private void onClick() {
        btn_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                finish();
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
                    ApiService.api.login(username, password).enqueue(new Callback<Boolean>() {
                        @Override
                        public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                            if (response.isSuccessful()) {
                                if (Boolean.TRUE.equals(response.body())) {
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
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
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
                        public void onFailure(Call<Boolean> call, Throwable t) {
                            Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(LoginActivity.this, ErrorActivity.class));
                            LoadingDialog.dismissDialog();
                        }
                    });
                }

            }
        });
    }

    private void getUser(String username) {
        ApiService.api.getUserByUsername(username).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.body() != null) {
                    ApiDataManager.getInstance().setUser(response.body());
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

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


}