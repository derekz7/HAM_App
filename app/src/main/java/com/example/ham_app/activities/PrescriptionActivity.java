package com.example.ham_app.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.ham_app.R;
import com.example.ham_app.adapters.PrescriptionAdapter;
import com.example.ham_app.api.ApiService;
import com.example.ham_app.dialog.LoadingDialog;
import com.example.ham_app.modules.Prescription;
import com.example.ham_app.untils.ApiDataManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PrescriptionActivity extends AppCompatActivity {

    private ImageButton igb_backPrescription,igb_tips;
    private ConstraintLayout layout_notificationPre;
    private RecyclerView rec_prescriptions;
    private List<Prescription> prescriptionList;
    private TextView textViewTips;
    private PrescriptionAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prescription);
        initView();
        setupLayout();
        loadData();
        onClick();
    }

    private void setupLayout() {
        adapter = new PrescriptionAdapter(this,prescriptionList);
        rec_prescriptions.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rec_prescriptions.setLayoutManager(linearLayoutManager);
    }

    private void loadData() {
        if (ApiDataManager.getInstance().getPrescriptionList() != null){
            layout_notificationPre.setVisibility(View.INVISIBLE);
            prescriptionList = ApiDataManager.getInstance().getPrescriptionList();
            adapter.setData(prescriptionList);

        }else {
            LoadingDialog.show(this);
            ApiService.api.getPrescriptionByUser(ApiDataManager.getInstance().getUser().getId()).enqueue(new Callback<List<Prescription>>() {
                @Override
                public void onResponse(Call<List<Prescription>> call, Response<List<Prescription>> response) {
                    if (response.body() != null){
                        layout_notificationPre.setVisibility(View.GONE);
                        prescriptionList.clear();
                        prescriptionList.addAll(response.body());
                        adapter.setData(response.body());
                        ApiDataManager.getInstance().setPrescriptionList(response.body());
                        LoadingDialog.dismissDialog();

                    }else{
                        layout_notificationPre.setVisibility(View.VISIBLE);
                        LoadingDialog.dismissDialog();
                    }
                }

                @Override
                public void onFailure(Call<List<Prescription>> call, Throwable t) {
                    LoadingDialog.dismissDialog();
                    startActivity(new Intent(PrescriptionActivity.this, ErrorActivity.class));
                }
            });
        }
    }

    private void onClick() {
        igb_backPrescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        igb_tips.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (textViewTips.getVisibility() == View.GONE){
                    textViewTips.setVisibility(View.VISIBLE);
                }else {
                    textViewTips.setVisibility(View.GONE);
                }
            }
        });
    }

    private void initView() {
        igb_backPrescription = findViewById(R.id.igb_backPrescription);
        layout_notificationPre = findViewById(R.id.layout_notificationPre);
        rec_prescriptions = findViewById(R.id.rec_prescription);
        igb_tips = findViewById(R.id.igb_tips);
        textViewTips = findViewById(R.id.textViewTips);
        prescriptionList = new ArrayList<>();
    }
}