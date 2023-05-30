package com.example.ham_app.activities;

import static com.example.ham_app.untils.Common.currentUserLogin;
import static com.example.ham_app.untils.Common.patternMatches;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ham_app.R;
import com.example.ham_app.api.ApiService;
import com.example.ham_app.models.User;
import com.example.ham_app.untils.Common;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private EditText edt_email, edt_password;
    private Button btn_signUp, btn_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
        onClick();
    }

    private void onClick(){
        btn_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edt_email.getText().toString();
                String password = edt_password.getText().toString();
                LoadingDialog loadingDialog = new LoadingDialog(LoginActivity.this);
                if (email.length() == 0){
                    edt_email.setError("Please enter your email.");
                }else if(password.length() == 0){
                    edt_password.setError("Please enter your password.");
                }else if(!patternMatches(email)){
                    edt_email.setError("Định dạng email không chính xác. Vui lòng thử lại!");
                }else {
                    loadingDialog.show();
                    ApiService.api.login(email,password).enqueue(new Callback<Boolean>() {
                        @Override
                        public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                            if (response.isSuccessful()){
                                loadingDialog.dismissDialog();
                                if (Boolean.TRUE.equals(response.body())){
                                    getUser(email);
                                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                                    finish();
                                }else {
                                    edt_email.setError("Incorrect email or password.");
                                }
                            }else {
                                Toast.makeText(LoginActivity.this, "Error", Toast.LENGTH_SHORT).show();
                                loadingDialog.dismissDialog();
                            }
                        }

                        @Override
                        public void onFailure(Call<Boolean> call, Throwable t) {

                        }
                    });
                }

            }
        });
    }

    private void getUser(String email) {
        ApiService.api.getUserByEmail(email).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.body() != null){
                    Common.currentUserLogin = response.body();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });
    }

    private void init(){
        edt_email = findViewById(R.id.edt_email);
        edt_password = findViewById(R.id.edt_password);
        btn_signUp = findViewById(R.id.btn_signUp);
        btn_login = findViewById(R.id.btn_login);
    }
}