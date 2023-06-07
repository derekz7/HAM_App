package com.example.ham_app.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
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
import java.util.Objects;

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
                showDialog(patients.get(pos));
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
    public void showDialog(Patient patient) {
        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.bottom_dialog_patient);
        dialog.setCancelable(true);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setGravity(Gravity.BOTTOM);
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().getAttributes().windowAnimations = R.style.BottomSheetAnimation;
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        final ImageView imgExit = dialog.findViewById(R.id.igbExit);
        final ImageView imgCancel = dialog.findViewById(R.id.imgCancel);
        final TextView ptId = dialog.findViewById(R.id.tvPatientID);
        final TextView ptName = dialog.findViewById(R.id.tvPatientName1);
        final TextView ptDob = dialog.findViewById(R.id.tvDOB);
        final TextView ptGender = dialog.findViewById(R.id.tvPatentGender);
        final TextView ptJob = dialog.findViewById(R.id.tvPatientJob);
        final TextView ptAddress = dialog.findViewById(R.id.tvPatientAddress1);
        final Button btnDelete = dialog.findViewById(R.id.btnSelectedPatient);
        btnDelete.setText(getResources().getString(R.string.delete_patient));
        btnDelete.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.red)));
        final Button btnEdit = dialog.findViewById(R.id.btnEditPatient);

        if (ptAddress.getText().equals("")||ptJob.getText().equals("")){
            ptJob.setText(getString(R.string.not_update));
            ptAddress.setText(getString(R.string.not_update));
        }else {
            ptJob.setText(patient.getJob());
            ptAddress.setText(patient.getAddress());
        }
        ptId.setText(patient.getId());
        ptName.setText(patient.getPatientName());
        ptDob.setText(patient.getDob());
        ptGender.setText(patient.getGender());

        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        imgExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               dialogDelete(patient);
                dialog.dismiss();
            }
        });
        dialog.show();

    }

    public void dialogDelete(Patient patient){
        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.custom_alert_dialog);
        dialog.setCancelable(false);
        final Button btnDelete = dialog.findViewById(R.id.btnAlert_tryAgain);
        final LottieAnimationView lottieAnimationViewAlert = dialog.findViewById(R.id.lottieAnimationViewAlert);
        lottieAnimationViewAlert.setAnimation(R.raw.delete);
        btnDelete.setText(getResources().getString(R.string.delete_patient));
        final TextView tv_Message = dialog.findViewById(R.id.tvMessage);
        if (dialog.getWindow() != null)
        {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        tv_Message.setText("Bạn có chắc muốn xóa hồ sơ này không?");
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApiService.api.DeletePatient(patient.getId()).enqueue(new Callback<Patient>() {
                    @Override
                    public void onResponse(Call<Patient> call, Response<Patient> response) {
                        if (response.body() != null){
                            Toast.makeText(getContext(), "Đã xóa", Toast.LENGTH_SHORT).show();
                            patients.remove(patient);
                            patientAdapter.setData(patients);
                        }
                    }

                    @Override
                    public void onFailure(Call<Patient> call, Throwable t) {
                        startActivity(new Intent(getActivity(),ErrorActivity.class));
                    }
                });
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}