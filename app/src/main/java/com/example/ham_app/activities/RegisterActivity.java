package com.example.ham_app.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ham_app.R;
import com.example.ham_app.api.ApiService;
import com.example.ham_app.dialog.AlertDialog;
import com.example.ham_app.dialog.LoadingDialog;
import com.example.ham_app.models.User;
import com.example.ham_app.untils.ApiDataManager;
import com.example.ham_app.untils.Common;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    private Button btnLogin, btnSignUp;
    private EditText edtName, edtUsername, edtPassword, edtRePassword, edtPhoneNumber;
    private AlertDialog alertDialog;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
        onClick();
    }

    private void onClick() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username, name, password, rePassword, phoneNum;
                username = edtUsername.getText().toString();
                name = edtName.getText().toString();
                password = edtPassword.getText().toString();
                rePassword = edtRePassword.getText().toString();
                phoneNum = edtPhoneNumber.getText().toString();
                if (username.length() == 0 || name.length() == 0 || password.length() == 0 || rePassword.length() == 0 || phoneNum.length() == 0) {
                    Toast.makeText(RegisterActivity.this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                } else {
                    if (password.length() < 6 || password.length() > 100) {
                        edtPassword.setError("Password cần có tối thiểu 6 ký tự.");
                    } else if (!password.equals(rePassword)) {
                        edtRePassword.setError("Vui lòng nhập lại password.");
                    }
                    if (!Common.isValidPhoneNumber(phoneNum)) {
                        edtPhoneNumber.setError("Số điện thoại không hợp lệ.");
                    }
                    if (username.length() < 5 || username.length() > 30) {
                        edtUsername.setError("Vui lòng nhập username lớn hơn 5 và không quá 30 ký tự.");
                    }else {
                        if (username.contains(" ")) {
                            edtUsername.setError("Username không được có khoảng trắng.");
                        } else {
                            LoadingDialog.show(RegisterActivity.this);
                            User user = new User(username, name, password, phoneNum);
                            ApiService.api.getUserByUsername(username).enqueue(new Callback<User>() {
                                @Override
                                public void onResponse(Call<User> call, Response<User> response) {
                                    if (response.body() != null) {
                                        alertDialog.show("Username đã tồn tại. Vui lòng chọn username khác.");
                                        LoadingDialog.dismissDialog();
                                    } else {
                                        ApiService.api.signUp(user).enqueue(new Callback<User>() {
                                            @Override
                                            public void onResponse(Call<User> call, Response<User> response) {
                                                if (response.isSuccessful()) {
                                                    if (response.body() != null) {
                                                        ApiDataManager.getInstance().setUser(response.body());
                                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                                        editor.putString("username", username);
                                                        editor.apply();
                                                        LoadingDialog.dismissDialog();
                                                        Toast.makeText(RegisterActivity.this, "Tạo tài khoản thành công.", Toast.LENGTH_SHORT).show();
                                                        startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
                                                    } else {
                                                        LoadingDialog.dismissDialog();
                                                        startActivity(new Intent(RegisterActivity.this, ErrorActivity.class));
                                                        finish();
                                                    }

                                                }else{
                                                    LoadingDialog.dismissDialog();
                                                    Toast.makeText(RegisterActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<User> call, Throwable t) {
                                                LoadingDialog.dismissDialog();
                                                startActivity(new Intent(RegisterActivity.this, ErrorActivity.class));
                                            }
                                        });
                                    }
                                }

                                @Override
                                public void onFailure(Call<User> call, Throwable t) {
                                    LoadingDialog.dismissDialog();
                                    startActivity(new Intent(RegisterActivity.this, ErrorActivity.class));
                                }
                            });
                        }
                    }
                }
            }
        });
    }

    private void initView() {
        btnSignUp = findViewById(R.id.btn_register);
        btnLogin = findViewById(R.id.edt_back_to_login);
        edtName = findViewById(R.id.edt_name);
        edtUsername = findViewById(R.id.edt_username);
        edtPassword = findViewById(R.id.edt_password);
        edtRePassword = findViewById(R.id.edt_re_password);
        edtPhoneNumber = findViewById(R.id.edt_phone_number);
        alertDialog = new AlertDialog(getApplicationContext());
        sharedPreferences = getSharedPreferences("dataLogin", MODE_PRIVATE);
    }
}