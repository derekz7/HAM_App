package com.example.ham_app.activities.doctors;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ham_app.R;
import com.example.ham_app.activities.ErrorActivity;
import com.example.ham_app.activities.LoginActivity;
import com.example.ham_app.adapters.AppointmentAdapter;
import com.example.ham_app.api.ApiService;
import com.example.ham_app.dialog.LoadingDialog;
import com.example.ham_app.modules.Appointment;
import com.example.ham_app.modules.Booking;
import com.example.ham_app.modules.User;
import com.example.ham_app.untils.ApiDataManager;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class DoctorHomeFragment extends Fragment {

    private ImageView profile_image;
    private TextView tvUserName, notiDoctor, tvMessageSearch;
    private ImageButton btnSetupProfile, btnPrescriptionHistory, btnLogOut1;
    private RecyclerView recycleViewAppointment;
    private AppointmentAdapter adapter;
    private List<Appointment> appointments;
    private ConstraintLayout layout_notificationB;
    private SwipeRefreshLayout swipelayoutDc;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_doctor_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        setLayout();
        loadData();
        onClick();


    }

    private void onClick() {
        btnLogOut1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("dataLogin", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.remove("password");
                editor.apply();
                startActivity(new Intent(getContext(), LoginActivity.class));
                requireActivity().finish();
            }
        });
        btnSetupProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        swipelayoutDc.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                LoadingDialog.show(getContext());
                ApiService.api.getAllAppointmentToday(ApiDataManager.getInstance().getUser().getId()).enqueue(new Callback<List<Appointment>>() {
                    @Override
                    public void onResponse(Call<List<Appointment>> call, Response<List<Appointment>> response) {
                        if (response.body() != null) {
                            if (response.body().size() > 0) {
                                appointments.addAll(response.body());
                                adapter.setData(response.body());
                                ApiDataManager.getInstance().setAppointmentListToday(response.body());
                                layout_notificationB.setVisibility(View.INVISIBLE);
                                LoadingDialog.dismissDialog();
                            } else {
                                layout_notificationB.setVisibility(View.VISIBLE);
                                LoadingDialog.dismissDialog();
                            }

                        } else {
                            LoadingDialog.dismissDialog();

                        }
                    }

                    @Override
                    public void onFailure(Call<List<Appointment>> call, Throwable t) {
                        LoadingDialog.dismissDialog();
                        startActivity(new Intent(getContext(), ErrorActivity.class));

                    }
                });
                swipelayoutDc.setRefreshing(false);
            }
        });
    }

    private void loadData() {
        if (ApiDataManager.getInstance().getAppointmentListToday() == null) {
            layout_notificationB.setVisibility(View.VISIBLE);
            tvMessageSearch.setText("Đang tìm kiếm lịch khám ngày hôm nay");
            ApiService.api.getAllAppointmentToday(ApiDataManager.getInstance().getUser().getId()).enqueue(new Callback<List<Appointment>>() {
                @Override
                public void onResponse(Call<List<Appointment>> call, Response<List<Appointment>> response) {
                    if (response.body() != null) {
                        if (response.body().size() > 0) {
                            appointments.addAll(response.body());
                            adapter.setData(response.body());
                            ApiDataManager.getInstance().setAppointmentListToday(response.body());
                            layout_notificationB.setVisibility(View.INVISIBLE);
                        } else {
                            layout_notificationB.setVisibility(View.VISIBLE);
                            tvMessageSearch.setText("Bạn không có lịch đặt khám nào hôm nay");
                        }

                    } else {
                        layout_notificationB.setVisibility(View.INVISIBLE);
                    }

                }

                @Override
                public void onFailure(Call<List<Appointment>> call, Throwable t) {
                    layout_notificationB.setVisibility(View.INVISIBLE);
                    startActivity(new Intent(getContext(), ErrorActivity.class));
                }
            });
        } else {
            if (ApiDataManager.getInstance().getAppointmentListToday().size() > 0) {
                appointments.addAll(ApiDataManager.getInstance().getAppointmentListToday());
                adapter.setData(appointments);
                layout_notificationB.setVisibility(View.INVISIBLE);
            } else {
                layout_notificationB.setVisibility(View.VISIBLE);
            }

        }
    }

    private void setLayout() {
        if (ApiDataManager.getInstance().getUser() != null) {
            User user = ApiDataManager.instance.getUser();
            Picasso.get().load(user.getImgUrl()).into(profile_image);
            tvUserName.setText(user.getFullName());
        }
        appointments = new ArrayList<>();
        adapter = new AppointmentAdapter(getContext(), appointments);
        recycleViewAppointment.setAdapter(adapter);
        recycleViewAppointment.setNestedScrollingEnabled(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recycleViewAppointment.setLayoutManager(linearLayoutManager);
    }

    private void initView(View view) {
        profile_image = view.findViewById(R.id.profile_image1);
        tvUserName = view.findViewById(R.id.tv_userName1);
        btnSetupProfile = view.findViewById(R.id.btnSetupProfile);
        btnPrescriptionHistory = view.findViewById(R.id.btnPrescriptionHistory);
        btnLogOut1 = view.findViewById(R.id.btnLogOut1);
        recycleViewAppointment = view.findViewById(R.id.recycleViewAppointment);
        layout_notificationB = view.findViewById(R.id.layout_notificationB);
        swipelayoutDc = view.findViewById(R.id.swipelayoutDc);
        notiDoctor = view.findViewById(R.id.notiDoctor);
        tvMessageSearch = view.findViewById(R.id.tvMessageSearch);
    }
}