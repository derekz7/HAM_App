package com.example.ham_app.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ham_app.R;
import com.example.ham_app.adapters.PatientAdapter;
import com.example.ham_app.api.ApiService;
import com.example.ham_app.dialog.LoadingDialog;
import com.example.ham_app.modules.Patient;
import com.example.ham_app.untils.ApiDataManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PatientActivity extends AppCompatActivity {
    private ImageButton igb_addPatient;
    private RecyclerView recyclerViewPatients;
    private TextView tvNotification;
    private PatientAdapter patientAdapter;
    private List<Patient> patients;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ImageButton igb_backPatient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient);
        init();
        setLayout();
        if (ApiDataManager.getInstance().getPatientList() != null) {
            loadData();
        } else {
            getData();
        }
        onClick();
    }

    private void onClick() {
        igb_backPatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        igb_addPatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        patientAdapter.setOnItemClickListener(new PatientAdapter.onItemClickListener() {
            @Override
            public void onItemClick(int pos, View view) {
                Patient patient = patients.get(pos);
                showDialog(patient);
            }
        });
    }

    private void getData() {
        LoadingDialog.show(this);
        ApiService.api.getPatientByUser(ApiDataManager.getInstance().getUser().getId()).enqueue(new Callback<List<Patient>>() {
            @Override
            public void onResponse(Call<List<Patient>> call, Response<List<Patient>> response) {
                if (response.body() != null) {
                    patients.clear();
                    patients.addAll(response.body());
                    patientAdapter.setData(response.body());
                    ApiDataManager.getInstance().setPatientList(patients);
                } else {
                    tvNotification.setVisibility(View.VISIBLE);
                }
                LoadingDialog.dismissDialog();
            }

            @Override
            public void onFailure(Call<List<Patient>> call, Throwable t) {
                LoadingDialog.dismissDialog();
            }
        });
    }

    private void loadData() {
        LoadingDialog.show(this);
        if (ApiDataManager.getInstance().getPatientList() != null) {
            if (ApiDataManager.getInstance().getPatientList().size() > 0) {
                tvNotification.setVisibility(View.INVISIBLE);
                patients = ApiDataManager.getInstance().getPatientList();
                patientAdapter.setData(patients);
            } else {
                tvNotification.setVisibility(View.VISIBLE);
            }

        }
        LoadingDialog.dismissDialog();
    }

    private void setLayout() {
        recyclerViewPatients.setAdapter(patientAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerViewPatients.setLayoutManager(linearLayoutManager);
    }

    private void init() {
        patients = new ArrayList<>();
        swipeRefreshLayout = findViewById(R.id.swipelayout);
        patientAdapter = new PatientAdapter(this, patients);
        igb_addPatient = findViewById(R.id.igb_addPatient);
        recyclerViewPatients = findViewById(R.id.rec_Patient);
        tvNotification = findViewById(R.id.tv_notiPatient);
        igb_backPatient = findViewById(R.id.igb_backPatient);
    }

    public void showDialog(Patient patient) {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.bottom_dialog_patient);
        dialog.setCancelable(true);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setGravity(Gravity.BOTTOM);
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().getAttributes().windowAnimations = R.style.BottomSheetAnimation;
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        final ImageView imgCancel = dialog.findViewById(R.id.igbExit);
        final TextView ptId = dialog.findViewById(R.id.tvPatientID);
        final TextView ptName = dialog.findViewById(R.id.tvPatientName1);
        final TextView ptDob = dialog.findViewById(R.id.tvDOB);
        final TextView ptGender = dialog.findViewById(R.id.tvPatentGender);
        final TextView ptJob = dialog.findViewById(R.id.tvPatientJob);
        final TextView ptAddress = dialog.findViewById(R.id.tvPatientAddress1);
        final Button btnSelectPatient = dialog.findViewById(R.id.btnSelectedPatient);

        ptId.setText(patient.getId());
        ptName.setText(patient.getPatientName());
        ptDob.setText(patient.getDob());
        ptGender.setText(patient.getGender());
        ptJob.setText(patient.getJob());
        ptAddress.setText(patient.getAddress());
        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btnSelectPatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApiDataManager.getInstance().getBooking().setPt_id(patient.getId());
                ApiDataManager.getInstance().setSelectedPatient(patient);
                Intent resultIntent = new Intent();
                setResult(RESULT_OK, resultIntent);
                finish();
                dialog.dismiss();
            }
        });
        dialog.show();

    }
}