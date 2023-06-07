package com.example.ham_app.activities.doctors;

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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ham_app.R;
import com.example.ham_app.activities.ErrorActivity;
import com.example.ham_app.adapters.AppointmentAdapter;
import com.example.ham_app.api.ApiService;
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
    private TextView tvUserName,notiDoctor;
    private ImageButton btnSetupProfile, btnPrescriptionHistory, btn_help;
    private RecyclerView recycleViewAppointment;
    private AppointmentAdapter adapter;
    private List<Booking> bookingList;
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

        btnSetupProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        swipelayoutDc.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ApiService.api.getAllBookingToday(ApiDataManager.getInstance().getUser().getId()).enqueue(new Callback<List<Booking>>() {
                    @Override
                    public void onResponse(Call<List<Booking>> call, Response<List<Booking>> response) {
                        if (response.body() != null){
                            if (response.body().size() > 0){
                                bookingList.addAll(response.body());
                                adapter.setData(response.body());
                                ApiDataManager.getInstance().setBookingList(response.body());
                                layout_notificationB.setVisibility(View.INVISIBLE);
                            }
                            else {
                                layout_notificationB.setVisibility(View.VISIBLE);
                            }

                        }

                    }
                    @Override
                    public void onFailure(Call<List<Booking>> call, Throwable t) {
                        startActivity(new Intent(getContext(), ErrorActivity.class));
                    }
                });
                swipelayoutDc.setRefreshing(false);
            }
        });
    }

    private void loadData() {
        if (ApiDataManager.getInstance().getBookingList() == null){
            ApiService.api.getAllBookingToday(ApiDataManager.getInstance().getUser().getId()).enqueue(new Callback<List<Booking>>() {
                @Override
                public void onResponse(Call<List<Booking>> call, Response<List<Booking>> response) {
                    if (response.body() != null){
                        if (response.body().size() > 0){
                            bookingList.addAll(response.body());
                            adapter.setData(response.body());
                            ApiDataManager.getInstance().setBookingList(response.body());
                            layout_notificationB.setVisibility(View.INVISIBLE);
                        }
                        else {
                            layout_notificationB.setVisibility(View.VISIBLE);
                        }

                    }

                }
                @Override
                public void onFailure(Call<List<Booking>> call, Throwable t) {
                    startActivity(new Intent(getContext(), ErrorActivity.class));
                }
            });
        }else {
            if (ApiDataManager.getInstance().getBookingList().size() > 0){
                bookingList.addAll(ApiDataManager.getInstance().getBookingList());
                adapter.setData(bookingList);
                layout_notificationB.setVisibility(View.INVISIBLE);
            }else {
                layout_notificationB.setVisibility(View.VISIBLE);
            }

        }
    }

    private void setLayout() {
        if (ApiDataManager.getInstance().getUser() != null){
            User user = ApiDataManager.instance.getUser();
            Picasso.get().load(user.getImgUrl()).into(profile_image);
            tvUserName.setText(user.getFullName());
        }
        bookingList = new ArrayList<>();
        adapter = new AppointmentAdapter(getContext(),bookingList);
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
        btn_help = view.findViewById(R.id.btn_help);
        recycleViewAppointment = view.findViewById(R.id.recycleViewAppointment);
        layout_notificationB = view.findViewById(R.id.layout_notificationB);
        swipelayoutDc = view.findViewById(R.id.swipelayoutDc);
        notiDoctor = view.findViewById(R.id.notiDoctor);
    }
}