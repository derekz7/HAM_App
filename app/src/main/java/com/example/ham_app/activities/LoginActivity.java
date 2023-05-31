package com.example.ham_app.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
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
import com.example.ham_app.models.User;
import com.example.ham_app.untils.ApiDataManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private EditText edtUsername, edt_password;
    private Button btn_signUp, btn_login;
    private LoadingDialog loadingDialog;
    private  SharedPreferences sharedPreferences;
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
                    loadingDialog.show();
                    ApiService.api.login(username, password).enqueue(new Callback<Boolean>() {
                        @Override
                        public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                            if (response.isSuccessful()) {
                                if (Boolean.TRUE.equals(response.body())) {
                                    if (cbRemember.isChecked()){
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putString("username", username);
                                        editor.putString("password", password);
                                        editor.putBoolean("checked",true);
                                        editor.apply();
                                    }else {
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putString("username",username);
                                        editor.remove("password");
                                        editor.putBoolean("checked",false);
                                        editor.apply();
                                    }
                                    loadingDialog.dismissDialog();
                                    Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    showAlert("Incorrect username or password.","Tên tài khoản hoặc mật khẩu không đúng.");
                                    loadingDialog.dismissDialog();
                                }
                            } else {
                                showAlert("Error.","Server gặp sự cố. Vui lòng thử lại sau.");
                                loadingDialog.dismissDialog();
                            }
                        }

                        @Override
                        public void onFailure(Call<Boolean> call, Throwable t) {
                            Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                            loadingDialog.dismissDialog();
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
                if (response.body() != null){
                    ApiDataManager.getInstance().setUser(response.body());
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });
    }

    private void showAlert(String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setIcon(R.drawable.cancel);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }
    private void init() {
        edtUsername = findViewById(R.id.edt_username);
        edt_password = findViewById(R.id.edt_password);
        btn_signUp = findViewById(R.id.btn_signUp);
        btn_login = findViewById(R.id.btn_login);
        cbRemember = findViewById(R.id.cb_remember);
        loadingDialog = new LoadingDialog(LoginActivity.this);
        sharedPreferences = getSharedPreferences("dataLogin", MODE_PRIVATE);
        edtUsername.setText(sharedPreferences.getString("username", ""));
        edt_password.setText(sharedPreferences.getString("password", ""));
        cbRemember.setChecked(sharedPreferences.getBoolean("checked",false));
    }
}