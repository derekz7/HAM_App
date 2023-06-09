package com.example.ham_app.activities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.ham_app.R;
import com.example.ham_app.api.ApiService;
import com.example.ham_app.dialog.LoadingDialog;
import com.example.ham_app.firebase.UserDB;
import com.example.ham_app.modules.User;
import com.example.ham_app.untils.ApiDataManager;
import com.example.ham_app.untils.Common;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateUserInfoActivity extends AppCompatActivity {
    private ImageView img_userProfilePhoto;
    private ImageButton igb_backUpdateUserInfo;
    private Button btnChangeUserImage, btn_UpdateUser;
    private EditText edtUserFullname, edtEmailE, edtPhoneNumE;
    private Uri imgUri;
    private UserDB userDB;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user_info);
        initView();
        user = ApiDataManager.instance.getUser();
        userDB = new UserDB();
        setUpView();
        onClick();
    }

    private void setUpView() {
        Picasso.get().load(user.getImgUrl()).into(img_userProfilePhoto);
        edtUserFullname.setText(user.getFullName());
        edtEmailE.setText(user.getEmail() != null ? user.getEmail() : "");
        edtPhoneNumE.setText(user.getPhoneNumber() != null ? user.getPhoneNumber() : "");
    }

    private void initView() {
        img_userProfilePhoto = findViewById(R.id.img_userProfilePhoto);
        igb_backUpdateUserInfo = findViewById(R.id.igb_backUpdateUserInfo);
        btnChangeUserImage = findViewById(R.id.btnChangeUserImage);
        edtUserFullname = findViewById(R.id.edtUserFullname);
        edtEmailE = findViewById(R.id.edtEmailE);
        edtPhoneNumE = findViewById(R.id.edtPhoneNumE);
        btn_UpdateUser = findViewById(R.id.btn_UpdateUser);
    }

    private void onClick() {
        ActivityResultLauncher<Intent> launchSomeActivity
                = registerForActivityResult(
                new ActivityResultContracts
                        .StartActivityForResult(),
                result -> {
                    if (result.getResultCode()
                            == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null
                                && data.getData() != null) {
                            imgUri = data.getData();
                            img_userProfilePhoto.setImageURI(imgUri
                            );
                            userDB.upLoadToFireBase(this, data.getData(), user);
                            Log.d("image", user.getImgUrl());
                        }
                    }
                });

        btnChangeUserImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.setType("image/*");
                i.setAction(Intent.ACTION_GET_CONTENT);
                launchSomeActivity.launch(i);
            }
        });

        igb_backUpdateUserInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btn_UpdateUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = edtUserFullname.getText().toString();
                String email = edtEmailE.getText().toString();
                String phoneNumber = edtPhoneNumE.getText().toString();
                if (name.length() <= 10) {
                    edtUserFullname.setError("Vui lòng nhập tên đầy đủ của bạn");
                } else if (email.length() > 0 && !Common.patternMatches(email)) {
                    edtEmailE.setError("Định dạng email không đúng");
                } else if (phoneNumber.length() > 0 && !Common.isValidPhoneNumber(phoneNumber)) {
                    edtPhoneNumE.setError("Định dạng số điện thoại không đúng");
                } else {
                    user.setFullName(name);
                    user.setEmail(email);
                    user.setPhoneNumber(phoneNumber);
                    LoadingDialog.show(UpdateUserInfoActivity.this);
                    ApiService.api.updateUser(user.getId(), user).enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if (response.isSuccessful()) {
                                ApiDataManager.getInstance().setUser(user);
                                Intent resultIntent = new Intent();
                                setResult(RESULT_OK, resultIntent);
                                finish();
                                LoadingDialog.dismissDialog();
                            } else {
                                LoadingDialog.dismissDialog();
                                startActivity(new Intent(new Intent(UpdateUserInfoActivity.this, ErrorActivity.class)));
                            }

                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            LoadingDialog.dismissDialog();
                            startActivity(new Intent(new Intent(UpdateUserInfoActivity.this, ErrorActivity.class)));
                        }
                    });
                }
            }
        });

    }


}