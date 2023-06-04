package com.example.ham_app.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.ham_app.R;
import com.example.ham_app.adapters.DepartmentAdapter;
import com.example.ham_app.api.ApiService;
import com.example.ham_app.modules.Department;
import com.example.ham_app.untils.ApiDataManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DepartmentActivity extends AppCompatActivity {
    private RecyclerView rec_depList;
    private DepartmentAdapter adapter;
    private List<Department> departmentList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_department);
        initView();
        setLayout();
        loadData();
        onClick();
    }

    private void onClick() {
        adapter.setOnItemClickListener(new DepartmentAdapter.onItemClickListener() {
            @Override
            public void onItemClick(int pos, View view) {
                ApiDataManager.getInstance().setSelectedDepartment(departmentList.get(pos));
                Log.d("Booking","Department: " + departmentList.get(pos).getName());
                Intent resultIntent = new Intent();
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });
    }

    private void initView() {
        rec_depList = findViewById(R.id.rec_depList);
    }
    @SuppressLint("NotifyDataSetChanged")
    private void setLayout() {
        departmentList = new ArrayList<>();
        adapter = new DepartmentAdapter(this, departmentList);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 4);
        rec_depList.setLayoutManager(layoutManager);
        rec_depList.setNestedScrollingEnabled(true);
        rec_depList.setAdapter(adapter);
    }

    private void loadData() {
        if (ApiDataManager.getInstance().getDepartmentList() != null){
            departmentList.addAll(ApiDataManager.getInstance().getDepartmentList());
            adapter.setData(departmentList);
        }else {
            ApiService.api.getDepartments().enqueue(new Callback<List<Department>>() {
                @Override
                public void onResponse(Call<List<Department>> call, Response<List<Department>> response) {
                    if (response.body() != null) {
                        departmentList.addAll(response.body());
                        adapter.setData(response.body());
                        ApiDataManager.getInstance().setDepartmentList(departmentList);
                    }
                }
                @Override
                public void onFailure(Call<List<Department>> call, Throwable t) {
                }
            });
        }

    }
}