package com.example.ham_app.fragments;

import android.content.Intent;
import android.os.Bundle;

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
import android.widget.Toast;

import com.example.ham_app.R;
import com.example.ham_app.activities.AppointmentDetailActivity;
import com.example.ham_app.adapters.AppointmentAdapter;
import com.example.ham_app.adapters.PatientAdapter;
import com.example.ham_app.api.ApiService;
import com.example.ham_app.dialog.LoadingDialog;
import com.example.ham_app.modules.Appointment;
import com.example.ham_app.modules.Booking;
import com.example.ham_app.modules.Patient;
import com.example.ham_app.untils.ApiDataManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AppointmentFragment extends Fragment {
    private ConstraintLayout layout_notification;
    private SwipeRefreshLayout swipeRefreshBooking;
    private RecyclerView rec_Booking;
    private AppointmentAdapter adapter;
    private List<Booking> bookingList;

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
        if (ApiDataManager.getInstance().getBookingList() != null) {
            //Toast.makeText(getContext(), ApiDataManager.getInstance().getUser().getId(), Toast.LENGTH_SHORT).show();
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
                ApiService.api.getAppointment(bookingList.get(pos).getId()).enqueue(new Callback<Appointment>() {
                    @Override
                    public void onResponse(Call<Appointment> call, Response<Appointment> response) {
                        if (response.body() != null){
                            Appointment appointment = response.body();
                            Intent intent = new Intent(getContext(), AppointmentDetailActivity.class);
                            intent.putExtra("Appointment",appointment);
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onFailure(Call<Appointment> call, Throwable t) {

                    }
                });
            }
        });
    }
    private void init(View view){
        layout_notification = view.findViewById(R.id.layout_notification);
        swipeRefreshBooking = view.findViewById(R.id.swipeRefreshBooking);
        rec_Booking = view.findViewById(R.id.rec_booking);
        bookingList = new ArrayList<>();
    }
    private void getData() {
        LoadingDialog.show(getContext());
        ApiService.api.getBookingByUserId(ApiDataManager.getInstance().getUser().getId()).enqueue(new Callback<List<Booking>>() {
            @Override
            public void onResponse(Call<List<Booking>> call, Response<List<Booking>> response) {
                if (response.body() != null){
                    bookingList.clear();
                    bookingList.addAll(response.body());
                    adapter.setData(response.body());
                    ApiDataManager.getInstance().setBookingList(bookingList);
                }else {
                    layout_notification.setVisibility(View.VISIBLE);
                }
                LoadingDialog.dismissDialog();
            }

            @Override
            public void onFailure(Call<List<Booking>> call, Throwable t) {
                LoadingDialog.dismissDialog();
            }
        });
    }

    private void loadData() {
        LoadingDialog.show(getContext());
        if (ApiDataManager.getInstance().getBookingList() != null) {
            if (ApiDataManager.getInstance().getBookingList().size() > 0) {
                layout_notification.setVisibility(View.INVISIBLE);
                bookingList.clear();
                bookingList = ApiDataManager.getInstance().getBookingList();
                adapter.setData(bookingList);
            } else {
                layout_notification.setVisibility(View.VISIBLE);
            }

        }
        LoadingDialog.dismissDialog();
    }

    private void setLayout() {
        adapter = new AppointmentAdapter(getContext(),bookingList);
        rec_Booking.setAdapter(adapter);
        rec_Booking.setNestedScrollingEnabled(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rec_Booking.setLayoutManager(linearLayoutManager);
    }
}