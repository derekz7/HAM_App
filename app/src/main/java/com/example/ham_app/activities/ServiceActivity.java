package com.example.ham_app.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.ham_app.R;
import com.example.ham_app.adapters.ServiceAdapter;
import com.example.ham_app.api.ApiService;
import com.example.ham_app.dialog.LoadingDialog;
import com.example.ham_app.modules.Service;
import com.example.ham_app.untils.ApiDataManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ServiceActivity extends AppCompatActivity {

    private List<Service> serviceList;
    private RecyclerView rec_services;
    private ServiceAdapter serviceAdapter;
    private ImageButton igbBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);
        initView();
        setUpView();
        loadData();
        onClick();
    }

    private void setUpView() {
        serviceList = new ArrayList<>();
        serviceAdapter = new ServiceAdapter(this,serviceList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        rec_services.setLayoutManager(linearLayoutManager);
        rec_services.setAdapter(serviceAdapter);
    }

    private void loadData() {
        if (ApiDataManager.getInstance().getServiceList() != null){
            serviceList = ApiDataManager.getInstance().getServiceList();
            serviceAdapter.setData(serviceList);
        }else {
            LoadingDialog.show(this);
            ApiService.api.getAllServices().enqueue(new Callback<List<Service>>() {
                @Override
                public void onResponse(Call<List<Service>> call, Response<List<Service>> response) {
                    if (response.body() != null){
                        serviceList.clear();
                        serviceList.addAll(response.body());
                        serviceAdapter.setData(response.body());
                        ApiDataManager.getInstance().setServiceList(response.body());
                    }
                    LoadingDialog.dismissDialog();
                }

                @Override
                public void onFailure(Call<List<Service>> call, Throwable t) {
                    LoadingDialog.dismissDialog();
                }
            });
        }
    }

    private void onClick() {
        igbBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        serviceAdapter.setOnClickListener(new ServiceAdapter.onItemClickListener() {
            @Override
            public void onItemClick(int pos, View view) {
                ApiDataManager.getInstance().setSelectService(serviceList.get(pos));
                ApiDataManager.getInstance().getBooking().setSv_id(serviceList.get(pos).getId());
                Log.d("Booking","Service: " + serviceList.get(pos).getServiceName());
                Intent resultIntent = new Intent();
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });
    }

    private void initView() {
        rec_services = findViewById(R.id.rec_services);
        igbBack = findViewById(R.id.igb_backService);
    }
}