package com.example.ham_app.activities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
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
import android.widget.Toast;

import com.example.ham_app.R;
import com.example.ham_app.api.ApiService;
import com.example.ham_app.dialog.LoadingDialog;
import com.example.ham_app.modules.Booking;
import com.example.ham_app.modules.Department;
import com.example.ham_app.modules.Patient;
import com.example.ham_app.modules.Service;
import com.example.ham_app.untils.ApiDataManager;
import com.example.ham_app.untils.Common;

import java.util.Random;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailActivity extends AppCompatActivity {
    private ImageButton igb_backDetail;
    private ImageView img_iconPayment;
    private TextView tv_ptName,tv_ptDob,tv_bookingDate,tv_bookingTime,tv_department,tv_price,tv_total, tvPaymentMethod;
    private Button btn_nextToPay;
    private Booking currentBooking;
    private Department department;
    private Patient patient;
    private Service service;
    private LinearLayout layout_Patient,linear_payment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        intiView();
        setUpView();
        reloadView();
        onClick();
    }

    private void reloadView() {
        if(Common.selectedPayment != null){
            if (Common.selectedPayment.equals("cash")){
                linear_payment.setVisibility(View.VISIBLE);
                btn_nextToPay.setText(getString(R.string.place_an_order));
            }
        }else {
            linear_payment.setVisibility(View.INVISIBLE);
            btn_nextToPay.setText(getString(R.string.payment_method));
        }

    }

    private void onClick() {
        ActivityResultLauncher<Intent> startActivity = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == 123) {
                       reloadView();
                    }
                }
        );
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
               if(btn_nextToPay.getText().equals(getString(R.string.place_an_order))){
                   createBooking();
               }
               if(btn_nextToPay.getText().equals(getString(R.string.payment_method))){
                   Intent intent = new Intent(DetailActivity.this, PaymentActivity.class);
                   startActivity.launch(intent);
               }
            }
        });
    }

    private void createBooking() {
        UUID uuid = UUID.randomUUID();
        String id = "bk-" + uuid.toString().substring(0,17);
        currentBooking.setId(id);
        currentBooking.setOrderNum(new Random().nextInt(101));
        currentBooking.setPrice(service.getPrice());
        ApiService.api.createBooking(currentBooking).enqueue(new Callback<Booking>() {
            @Override
            public void onResponse(Call<Booking> call, Response<Booking> response) {
                if (response.isSuccessful()){
                    if(response.body() != null){
                        Toast.makeText(DetailActivity.this, "Đặt khám thành công", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(DetailActivity.this,MedicalBillActivity.class));
                        finish();
                    }
                }else {
                    Toast.makeText(DetailActivity.this, "Bạn đang có lịch khám đang chờ. Không thể đặt thêm lịch", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Booking> call, Throwable t) {
                LoadingDialog.dismissDialog();
                startActivity(new Intent(DetailActivity.this, ErrorActivity.class));
            }
        });
    }

    //tv_bookingDate,tv_bookingTime,tv_department,tv_price,tv_total;
    @SuppressLint("SetTextI18n")
    private void setUpView() {
        currentBooking = ApiDataManager.getInstance().getBooking();
        department = ApiDataManager.getInstance().getSelectedDepartment();
        patient = ApiDataManager.getInstance().getSelectedPatient();
        service = ApiDataManager.getInstance().getSelectedService();
        tv_ptName.setText(patient.getPatientName());
        tv_ptDob.setText(patient.getDob());
        tv_bookingDate.setText(currentBooking.getDate());
        tv_bookingTime.setText(currentBooking.getTime());
        tv_department.setText(department.getName());
        tv_price.setText(service.getPrice() + "đ");
        tv_total.setText(service.getPrice() + "đ");

    }

    private void intiView() {
        linear_payment = findViewById(R.id.linear_payment);
        img_iconPayment = findViewById(R.id.img_iconPayment);
        tvPaymentMethod = findViewById(R.id.tvPaymentMethod);
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