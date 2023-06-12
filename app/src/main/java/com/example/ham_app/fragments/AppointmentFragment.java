package com.example.ham_app.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ham_app.R;
import com.example.ham_app.activities.AppointmentDetailActivity;
import com.example.ham_app.activities.ErrorActivity;
import com.example.ham_app.adapters.AppointmentAdapter;
import com.example.ham_app.adapters.PatientAdapter;
import com.example.ham_app.api.ApiService;
import com.example.ham_app.dialog.LoadingDialog;
import com.example.ham_app.modules.Appointment;
import com.example.ham_app.modules.Booking;
import com.example.ham_app.modules.Patient;
import com.example.ham_app.untils.ApiDataManager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AppointmentFragment extends Fragment {
    private ConstraintLayout layout_notification;
    private SwipeRefreshLayout swipeRefreshBooking;
    private RecyclerView rec_Booking;
    private AppointmentAdapter adapter;
    private List<Appointment> appointments;
    private ImageButton igb_filter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_appointment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
        setLayout();
        if (ApiDataManager.getInstance().getAppointments() != null) {
            loadData();
        } else {
            getData();
        }
        onClick();
    }

    private void onClick() {
        swipeRefreshBooking.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
                swipeRefreshBooking.setRefreshing(false);
            }
        });
        adapter.setOnItemClickListener(new AppointmentAdapter.onItemClickListener() {
            @Override
            public void onItemClick(int pos, View view) {
                Appointment appointment = appointments.get(pos);
                Intent intent = new Intent(getContext(), AppointmentDetailActivity.class);
                intent.putExtra("Appointment", appointment);
                startActivity(intent);
            }
        });
        igb_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });
    }

    private void init(View view) {
        layout_notification = view.findViewById(R.id.layout_notification);
        swipeRefreshBooking = view.findViewById(R.id.swipeRefreshBooking);
        rec_Booking = view.findViewById(R.id.rec_booking);
        igb_filter = view.findViewById(R.id.igb_filter);
        appointments = new ArrayList<>();
    }

    private void getData() {
        LoadingDialog.show(getContext());
        ApiService.api.getAppointmentsByUser(ApiDataManager.getInstance().getUser().getId()).enqueue(new Callback<List<Appointment>>() {
            @Override
            public void onResponse(Call<List<Appointment>> call, Response<List<Appointment>> response) {
                if (response.body() != null) {
                    appointments.clear();
                    if (response.body().size() > 0) {
                        appointments.addAll(response.body());
                        Collections.reverse(appointments);
                        adapter.setData(appointments);
                        layout_notification.setVisibility(View.INVISIBLE);
                        ApiDataManager.getInstance().setAppointments(appointments);
                        LoadingDialog.dismissDialog();
                    } else {
                        adapter.setData(appointments);
                        layout_notification.setVisibility(View.VISIBLE);
                        LoadingDialog.dismissDialog();
                    }

                } else {
                    layout_notification.setVisibility(View.VISIBLE);
                    LoadingDialog.dismissDialog();
                }

            }

            @Override
            public void onFailure(Call<List<Appointment>> call, Throwable t) {
                LoadingDialog.dismissDialog();
            }
        });
    }

    private void loadData() {
        if (ApiDataManager.getInstance().getAppointments() != null) {
            if (ApiDataManager.getInstance().getAppointments().size() > 0) {
                layout_notification.setVisibility(View.INVISIBLE);
                appointments.clear();
                appointments = ApiDataManager.getInstance().getAppointments();
                adapter.setData(appointments);
            } else {
                getData();
            }

        }
    }

    private void setLayout() {
        adapter = new AppointmentAdapter(getContext(), appointments);
        rec_Booking.setAdapter(adapter);
        rec_Booking.setNestedScrollingEnabled(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rec_Booking.setLayoutManager(linearLayoutManager);
    }

    public void showDialog() {
        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.custom_dialog_filter_appointment);
        dialog.setCancelable(true);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setGravity(Gravity.BOTTOM);
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().getAttributes().windowAnimations = R.style.BottomSheetAnimation;
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        final TextView tvToday = dialog.findViewById(R.id.tvToday);
        final TextView tvAll = dialog.findViewById(R.id.tvAll);
        tvToday.setText("Chờ khám");
        tvAll.setText("Tất cả");

        final ImageView imgCancel = dialog.findViewById(R.id.imgCancelFilter);


        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        tvToday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadingDialog.show(getContext());
                ApiService.api.getPendingAppointments(ApiDataManager.getInstance().getUser().getId()).enqueue(new Callback<List<Appointment>>() {
                    @Override
                    public void onResponse(Call<List<Appointment>> call, Response<List<Appointment>> response) {
                        if (response.body() != null) {
                            if (response.body().size() > 0) {
                                layout_notification.setVisibility(View.GONE);
                            } else {
                                layout_notification.setVisibility(View.VISIBLE);

                            }
                            adapter.setData(response.body());
                            LoadingDialog.dismissDialog();

                        }
                        LoadingDialog.dismissDialog();
                        dialog.dismiss();
                    }

                    @Override
                    public void onFailure(Call<List<Appointment>> call, Throwable t) {

                    }
                });

            }
        });

        tvAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData();
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}