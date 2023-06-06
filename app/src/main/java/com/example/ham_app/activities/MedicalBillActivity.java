package com.example.ham_app.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.ham_app.R;
import com.example.ham_app.untils.ApiDataManager;

import java.text.DecimalFormat;

public class MedicalBillActivity extends AppCompatActivity {
    private TextView tvStatus, tvOrderNumber, tvServiceBooked, tv_Room, tv_depBooked, tv_dcName,
            tv_dateBooked, tv_bookedTime, tv_ptName, tvTotalPrice, tv_bookedID;
    private ImageButton igbBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medical_bill);
        initView();
        setUpView();
        onClick();
    }

    @SuppressLint("SetTextI18n")
    private void setUpView() {
        tvOrderNumber.setText(ApiDataManager.getInstance().getBooking().getOrderNum()+"");
        tvServiceBooked.setText(ApiDataManager.getInstance().getSelectedService().getServiceName());
        tv_Room.setText(ApiDataManager.getInstance().getSelectedDoctor().getRoom());
        tv_depBooked.setText(ApiDataManager.getInstance().getSelectedDepartment().getName());
        tv_dcName.setText(ApiDataManager.getInstance().getSelectedDoctor().getName());
        tv_dateBooked.setText(ApiDataManager.getInstance().getBooking().getDate());
        tv_bookedTime.setText(ApiDataManager.getInstance().getBooking().getTime());
        tv_ptName.setText(ApiDataManager.getInstance().getSelectedPatient().getPatientName());

        DecimalFormat formatter = new DecimalFormat("###,###,###");
        tvTotalPrice.setText(formatter.format(ApiDataManager.getInstance().getSelectedService().getPrice()) + "đ");

        tv_bookedID.setText(ApiDataManager.getInstance().getBooking().getId());
    }

    private void onClick() {
        igbBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetVariable();
                Intent intent = new Intent(MedicalBillActivity.this, MainActivity.class);
                intent.putExtra("FRAGMENT_TAG", "appointment");
                startActivity(intent);
            }
        });
    }

    private void resetVariable() {
        ApiDataManager.getInstance().setSelectedPatient(null);
        ApiDataManager.getInstance().setSelectedService(null);
        ApiDataManager.getInstance().setSelectedDepartment(null);
        ApiDataManager.getInstance().setSelectedDoctor(null);
        ApiDataManager.getInstance().setBooking(null);
        ApiDataManager.getInstance().setSelectedTime(-1, -1);
    }

    private void initView() {
        tvStatus = findViewById(R.id.tv_status);
        tvOrderNumber = findViewById(R.id.tv_OrderNumber);
        tvServiceBooked = findViewById(R.id.tvServiceBooked);
        tv_Room = findViewById(R.id.tv_Room);
        tv_depBooked = findViewById(R.id.tv_depBooked);
        tv_dcName = findViewById(R.id.tv_dcName);
        tv_dateBooked = findViewById(R.id.tv_dateBooked);
        tv_bookedTime = findViewById(R.id.tv_bookedTime);
        tv_ptName = findViewById(R.id.tv_ptName);
        tvTotalPrice = findViewById(R.id.tvTotalPrice);
        tv_bookedID = findViewById(R.id.tv_bookedID);
        igbBack = findViewById(R.id.igb_backBill);
    }

    @Override
    public void onBackPressed() {
        resetVariable();
        startActivity(new Intent(MedicalBillActivity.this, MainActivity.class));
        finish();
    }
}