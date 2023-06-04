package com.example.ham_app.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.ham_app.R;
import com.example.ham_app.modules.Booking;
import com.example.ham_app.modules.Department;
import com.example.ham_app.modules.Patient;
import com.example.ham_app.modules.Service;
import com.example.ham_app.untils.ApiDataManager;

public class DetailActivity extends AppCompatActivity {
    private ImageButton igb_backDetail;
    private TextView tv_ptName,tv_ptDob,tv_bookingDate,tv_bookingTime,tv_department,tv_price,tv_total;
    private Button btn_nextToPay;
    private Booking currentBooking;
    private Department department;
    private Patient patient;
    private Service service;
    private LinearLayout layout_Patient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        intiView();
        setUpView();
        onClick();
    }

    private void onClick() {
        layout_Patient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(patient);
            }
        });

        igb_backDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resultIntent = new Intent();
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });

        btn_nextToPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    //tv_bookingDate,tv_bookingTime,tv_department,tv_price,tv_total;
    @SuppressLint("SetTextI18n")
    private void setUpView() {
        currentBooking = ApiDataManager.getInstance().getBooking();
        department = ApiDataManager.getInstance().getSelectDepartment();
        patient = ApiDataManager.getInstance().getSelectPatient();
        service = ApiDataManager.getInstance().getSelectService();
        tv_ptName.setText(patient.getPatientName());
        tv_ptDob.setText(patient.getDob());
        tv_bookingDate.setText(currentBooking.getDate());
        tv_bookingTime.setText(currentBooking.getTime());
        tv_department.setText(department.getName());
        tv_price.setText(service.getPrice() + "đ");
        tv_total.setText(service.getPrice() + "đ");

    }

    private void intiView() {
        layout_Patient = findViewById(R.id.layout_Patient);
        igb_backDetail = findViewById(R.id.igb_backDetail);
        tv_ptName = findViewById(R.id.tv_ptName2);
        tv_ptDob = findViewById(R.id.tv_ptDob);
        tv_bookingDate = findViewById(R.id.tv_bookingDate);
        tv_bookingTime = findViewById(R.id.tv_bookingTime);
        tv_department = findViewById(R.id.tv_department);
        tv_price = findViewById(R.id.tv_price);
        tv_total = findViewById(R.id.tv_total);
        btn_nextToPay = findViewById(R.id.btn_nextToPay);
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
        final Button btnEdit = dialog.findViewById(R.id.btnEditPatient);
        btnSelectPatient.setVisibility(View.INVISIBLE);
        btnEdit.setVisibility(View.INVISIBLE);
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
        dialog.show();

    }
}