package com.example.ham_app.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.example.ham_app.R;

public class RegisterActivity extends AppCompatActivity {
    private Button btnLogin, btnSignUp;
    private EditText edtName, edtUsername, edtPassword, edtRePassword, edtPhoneNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
    }

    private void initView() {
        btnSignUp = findViewById(R.id.btn_register);
        btnLogin = findViewById(R.id.edt_back_to_login);


    }
}