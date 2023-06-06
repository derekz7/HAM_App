package com.example.ham_app.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ham_app.R;
import com.example.ham_app.activities.BookingActivity;
import com.example.ham_app.activities.CreatePatientActivity;
import com.example.ham_app.activities.ErrorActivity;
import com.example.ham_app.activities.ServiceActivity;
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

public class ProfileFragment extends Fragment {
    private ImageButton igb_addPatient;
    private RecyclerView recyclerViewPatients;
    private TextView tvNotification;
    private PatientAdapter patientAdapter;
    private List<Patient> patients;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
        setLayout();
        if (ApiDataManager.getInstance().getPatientList() != null) {
            //Toast.makeText(getContext(), ApiDataManager.getInstance().getUser().getId(), Toast.LENGTH_SHORT).show();
            loadData();
        } else {
            getData();
        }
        onClick();
    }

    private void onClick() {
        ActivityResultLauncher<Intent> secondActivityLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == 111) {
                        getData();
                    }
                }
        );
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
                Intent intent = new Intent(getContext(), CreatePatientActivity.class);
                secondActivityLauncher.launch(intent);
            }
        });

        patientAdapter.setOnItemClickListener(new PatientAdapter.onItemClickListener() {
            @Override
            public void onItemClick(int pos, View view) {

            }
        });
    }

    private void getData() {
        LoadingDialog.show(getContext());
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
        LoadingDialog.show(getContext());
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
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerViewPatients.setLayoutManager(linearLayoutManager);
    }

    private void init(View view) {
        patients = new ArrayList<>();
        swipeRefreshLayout = view.findViewById(R.id.swipelayout);
        patientAdapter = new PatientAdapter(getContext(), patients);
        igb_addPatient = view.findViewById(R.id.igb_addPatient);
        recyclerViewPatients = view.findViewById(R.id.rec_Patients);
        tvNotification = view.findViewById(R.id.tv_noti);
    }
}