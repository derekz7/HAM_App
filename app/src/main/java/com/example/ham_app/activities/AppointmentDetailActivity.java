package com.example.ham_app.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.ham_app.R;
import com.example.ham_app.modules.Appointment;
import com.example.ham_app.untils.ApiDataManager;

import java.text.DecimalFormat;

public class AppointmentDetailActivity extends AppCompatActivity {

    private Appointment appointment;
    private ImageButton igb_backDetal;
    private TextView tvStatus, tvOrderNumber, tvServiceBooked, tv_Room, tv_depBooked, tv_dcName,
            tv_dateBooked, tv_bookedTime, tv_ptName, tvTotalPrice, tv_bookedID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_detail);
        appointment = (Appointment) getIntent().getSerializableExtra("Appointment");
        initView();
        setUpView();
        onClick();
    }

    private void onClick() {
        igb_backDetal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void setUpView() {
        ColorStateList colorStateList;
        if (appointment.getStatus().equals("Pending") || appointment.getStatus().equals("Chờ khám")) {
            colorStateList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.warning));
        } else if (appointment.getStatus().equals("Đã khám")) {
            colorStateList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.green));
        } else {
            colorStateList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.grey));
        }
        tvStatus.setBackgroundTintList(colorStateList);
        tvStatus.setText(appointment.getStatus());
        tvOrderNumber.setText(appointment.getOrderNum() + "");
        tvServiceBooked.setText(appointment.getServiceName());
        tv_Room.setText(appointment.getRoom());
        tv_depBooked.setText(appointment.getDepName());
        tv_dcName.setText(appointment.getDcName());
        tv_bookedTime.setText(appointment.getTime());
        tv_dateBooked.setText(appointment.getDate());
        tv_ptName.setText(appointment.getPtName());

        DecimalFormat formatter = new DecimalFormat("###,###,###");
        tvTotalPrice.setText(formatter.format(appointment.getPrice()) + "đ");
        tv_bookedID.setText(appointment.getBid());
    }

    private void initView() {
        tvStatus = findViewById(R.id.tv_status1);
        tvOrderNumber = findViewById(R.id.tv_OrderNumber1);
        tvServiceBooked = findViewById(R.id.tvServiceBooked1);
        tv_Room = findViewById(R.id.tv_Room1);
        tv_depBooked = findViewById(R.id.tv_depBooked1);
        tv_dcName = findViewById(R.id.tv_dcName1);
        tv_dateBooked = findViewById(R.id.tv_dateBooked1);
        tv_bookedTime = findViewById(R.id.tv_bookedTime1);
        tv_ptName = findViewById(R.id.tv_ptName2);
        tvTotalPrice = findViewById(R.id.tvTotalPrice1);
        tv_bookedID = findViewById(R.id.tv_bookedID1);
        igb_backDetal = findViewById(R.id.igb_backDetal);
    }
}