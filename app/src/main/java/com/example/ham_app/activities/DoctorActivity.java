package com.example.ham_app.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ham_app.R;
import com.example.ham_app.adapters.DoctorAdapter;
import com.example.ham_app.api.ApiService;
import com.example.ham_app.dialog.LoadingDialog;
import com.example.ham_app.modules.Doctor;
import com.example.ham_app.untils.ApiDataManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DoctorActivity extends AppCompatActivity {
    private DoctorAdapter adapter;
    private RecyclerView recyclerView;
    private List<Doctor> doctors;
    private ImageButton igbBack;
    private TextView tv_noti;
    private ConstraintLayout layout_loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor);
        initView();
        setUpView();
        loadData();
        onClick();
    }

    private void loadData() {
        if (ApiDataManager.getInstance().getBooking() != null){
            String dep_id = ApiDataManager.getInstance().getSelectedDepartment().getId();
            String date = ApiDataManager.getInstance().getBooking().getDate();
            String time = ApiDataManager.getInstance().getBooking().getTime();
            layout_loading.setVisibility(View.VISIBLE);
            ApiService.api.getDoctorAvailable(dep_id,date,time).enqueue(new Callback<List<Doctor>>() {
                @Override
                public void onResponse(Call<List<Doctor>> call, Response<List<Doctor>> response) {
                    if (response.body() != null){
                        if (response.body().size() > 0){
                            doctors.addAll(response.body());
                            adapter.setData(doctors);
                        }else {
                            tv_noti.setVisibility(View.VISIBLE);
                        }
                        Log.d("loadDoctor",response.body().size() + " ");

                    }
                    layout_loading.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onFailure(Call<List<Doctor>> call, Throwable t) {
                    layout_loading.setVisibility(View.INVISIBLE);
                    startActivity(new Intent(DoctorActivity.this,ErrorActivity.class));
                }
            });
        }
    }

    private void setUpView() {
        doctors = new ArrayList<>();
        adapter = new DoctorAdapter(this,doctors);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
    }


    private void onClick() {
        igbBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        adapter.setOnClickListener(new DoctorAdapter.onItemClickListener() {
            @Override
            public void onItemClick(int pos, View view) {
                ApiDataManager.getInstance().setSelectedDoctor(doctors.get(pos));
                ApiDataManager.getInstance().getBooking().setDc_id(doctors.get(pos).getId());
                Log.d("Booking","Doctor: " + doctors.get(pos).getName());
                Intent resultIntent = new Intent();
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });
    }

    private void initView() {
        layout_loading = findViewById(R.id.layout_loading);
        recyclerView = findViewById(R.id.rec_doctors);
        igbBack = findViewById(R.id.igb_backDoctor);
        tv_noti = findViewById(R.id.tv_notiDoctor);
    }
}