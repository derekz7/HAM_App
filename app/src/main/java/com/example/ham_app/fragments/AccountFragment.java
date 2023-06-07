package com.example.ham_app.fragments;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ham_app.R;
import com.example.ham_app.activities.LoginActivity;
import com.example.ham_app.activities.SecurityActivity;
import com.example.ham_app.untils.ApiDataManager;
import com.squareup.picasso.Picasso;

import java.util.Objects;


public class AccountFragment extends Fragment {
    private ImageView imgUser;
    private Button btnLogOut,btnSecurity;
    private TextView tvUserName, tvSdt;
    private SharedPreferences sharedPreferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_account, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sharedPreferences = getActivity().getSharedPreferences("dataLogin", MODE_PRIVATE);
        initView(view);
        setView();
        onClick();
    }

    private void onClick() {
        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.remove("password");
                editor.apply();
                startActivity(new Intent(getContext(), LoginActivity.class));
                requireActivity().finish();
            }
        });
        btnSecurity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), SecurityActivity.class));
            }
        });
    }

    private void setView() {
        tvSdt.setText(ApiDataManager.getInstance().getUser().getPhoneNumber());
        tvUserName.setText(ApiDataManager.getInstance().getUser().getFullName());
        Picasso.get().load(ApiDataManager.getInstance().getUser().getImgUrl()).into(imgUser);
    }

    private void initView(View view) {
        imgUser = view.findViewById(R.id.img_avatarUser);
        tvSdt = view.findViewById(R.id.tv_Sdt);
        tvUserName = view.findViewById(R.id.tv_hoTen);
        btnLogOut = view.findViewById(R.id.btnLogout);
        btnSecurity = view.findViewById(R.id.btnSecurity);
    }
}