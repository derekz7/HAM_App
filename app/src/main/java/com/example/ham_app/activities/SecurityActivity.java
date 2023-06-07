package com.example.ham_app.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ham_app.R;
import com.example.ham_app.api.ApiService;
import com.example.ham_app.modules.User;
import com.example.ham_app.untils.ApiDataManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SecurityActivity extends AppCompatActivity {
    private Button btnChangePasswod,btnDeleteUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security);
        initView();
        onClick();
    }

    private void onClick() {
        btnChangePasswod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogChangePassword();
            }
        });
        btnDeleteUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void dialogChangePassword() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.custom_dialog_change_password);
        dialog.setCancelable(true);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setGravity(Gravity.CENTER);
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//            dialog.getWindow().getAttributes().windowAnimations = R.style.BottomSheetAnimation;
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        final EditText edtCurrentPassword = dialog.findViewById(R.id.edt_currentPassword);
        final EditText edtNewPassword = dialog.findViewById(R.id.edt_newPassword);
        final EditText edtReNewPassword = dialog.findViewById(R.id.edt_reNewPassword);
        final Button btnSubmit = dialog.findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtCurrentPassword.getText().toString().equals(ApiDataManager.getInstance().getUser().getPassword())) {
                    if (edtNewPassword.getText().length() > 6) {
                        if (!edtNewPassword.getText().toString().equals(edtCurrentPassword.getText().toString())) {
                            if (edtNewPassword.getText().toString().equals(edtReNewPassword.getText().toString())) {
                                ApiDataManager.getInstance().getUser().setPassword(edtNewPassword.getText().toString());
                                ApiService.api.updateUser(ApiDataManager.getInstance().getUser().getId(), ApiDataManager.instance.getUser()).enqueue(new Callback<User>() {
                                    @Override
                                    public void onResponse(Call<User> call, Response<User> response) {
                                        if (response.isSuccessful()) {
                                            Toast.makeText(SecurityActivity.this, "Thay đổi mật khẩu thành công", Toast.LENGTH_SHORT).show();
                                        } else {
                                            startActivity(new Intent(SecurityActivity.this, ErrorActivity.class));
                                        }
                                        dialog.dismiss();
                                    }

                                    @Override
                                    public void onFailure(Call<User> call, Throwable t) {
                                        startActivity(new Intent(SecurityActivity.this, ErrorActivity.class));
                                    }
                                });
                            } else {
                                edtReNewPassword.setError("Vui lòng nhập lại mật khẩu");
                            }
                        } else {
                            edtNewPassword.setError("Mật khẩu mới trùng với mật khẩu hiện tại");
                        }

                    } else {
                        edtNewPassword.setError("Mật khẩu phải có tối thiểu 6 ký tự");
                    }
                } else {
                    edtCurrentPassword.setError("Mật khẩu không đúng");
                }
            }
        });


        dialog.show();
    }

    private void initView() {
        btnChangePasswod = findViewById(R.id.btnChangePasswod);
        btnDeleteUser = findViewById(R.id.btnDeleteUser);
    }
}