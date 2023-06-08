package com.example.ham_app.activities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ham_app.R;
import com.example.ham_app.modules.User;
import com.example.ham_app.untils.ApiDataManager;
import com.squareup.picasso.Picasso;

public class UserActivity extends AppCompatActivity {
    private ImageButton igb_back;
    private ImageView img_userImage;
    private TextView tv_userFullName,tv_username, tv_email,tv_sdt;
    private Button btn_editInfo;
    private User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        initView();
        loadView();
        onClick();
    }

    private void onClick() {
        ActivityResultLauncher<Intent> secondActivityLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        loadView();
                    }
                }
        );
        igb_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserActivity.this,MainActivity.class));
                finish();
            }
        });
        btn_editInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserActivity.this, UpdateUserInfoActivity.class);
                secondActivityLauncher.launch(intent);
            }
        });
    }

    private void loadView() {
        if (ApiDataManager.getInstance().getUser() != null){
            user = ApiDataManager.instance.getUser();
            Picasso.get().load(user.getImgUrl()).into(img_userImage);
            tv_userFullName.setText(user.getFullName());
            tv_username.setText(user.getUsername());
            if (user.getEmail() != null){
                tv_email.setText(user.getEmail());
            }else {
                tv_email.setText(getString(R.string.not_update));
            }
            tv_sdt.setText(user.getPhoneNumber() != null ? user.getPhoneNumber(): getString(R.string.not_update));
        }
    }

    private void initView() {
        igb_back = findViewById(R.id.igb_backAccount);
        img_userImage = findViewById(R.id.img_userImage);
        tv_username = findViewById(R.id.tv_username);
        tv_email = findViewById(R.id.tv_email);
        tv_sdt = findViewById(R.id.tv_sdt);
        btn_editInfo = findViewById(R.id.btn_editInfo);
        tv_userFullName = findViewById(R.id.tv_userFullName);

    }
}