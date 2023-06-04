package com.example.ham_app.activities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ham_app.R;
import com.example.ham_app.api.ApiService;
import com.example.ham_app.dialog.LoadingDialog;
import com.example.ham_app.modules.Booking;
import com.example.ham_app.modules.Department;
import com.example.ham_app.modules.Doctor;
import com.example.ham_app.modules.Patient;
import com.example.ham_app.modules.Service;
import com.example.ham_app.untils.ApiDataManager;

import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookingActivity extends AppCompatActivity {
    public static final int SECOND_ACTIVITY_REQUEST_CODE = 1;
    private ImageButton igb_backBooking;
    private LinearLayout layout_LoaiHinhKham, layout_ChuyenKhoa, layout_ngayKham, layout_gioKham, layout_bacSi, layout_benhNhan;
    private TextView btn_next, btnPickService, btnPickDepartment, btn_PickDate, btnPickTime, btnPickDoctor, btnPickPatient;
    private EditText edtNote;
    private Service selectedService;
    private Department selectedDepartment;
    private Doctor selectedDoctor;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);
        initView();
        setUpView();
        loadPatient();
        onClick();
    }

    private void loadPatient() {
        ApiService.api.getPatientByUser(userId).enqueue(new Callback<List<Patient>>() {
            @Override
            public void onResponse(Call<List<Patient>> call, Response<List<Patient>> response) {
                if (response.body() != null) {
                    ApiDataManager.getInstance().setPatientList(response.body());
                    Log.d("patient",response.body().get(0).getPatientName());
                }
            }

            @Override
            public void onFailure(Call<List<Patient>> call, Throwable t) {

            }
        });
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void setUpView() {
        if (ApiDataManager.getInstance().getBooking() == null) {
            hideLayout();
            ApiDataManager.getInstance().setBooking(new Booking());
        } else {
            if (ApiDataManager.getInstance().getSelectedService() != null) {
                selectedService = ApiDataManager.getInstance().getSelectedService();
                selectedButtonChange(btnPickService, selectedService.getServiceName());
                layout_ChuyenKhoa.setVisibility(View.VISIBLE);
            }
            if (ApiDataManager.getInstance().getSelectedDepartment() != null) {
                selectedDepartment = ApiDataManager.getInstance().getSelectedDepartment();
                selectedButtonChange(btnPickDepartment, selectedDepartment.getName());
                layout_ngayKham.setVisibility(View.VISIBLE);
            }
            if (ApiDataManager.getInstance().getBooking().getDate() != null) {
                selectedButtonChange(btn_PickDate, ApiDataManager.getInstance().getBooking().getDate());
                layout_gioKham.setVisibility(View.VISIBLE);
            }
            if (ApiDataManager.getInstance().getBooking().getTime() != null) {
                selectedButtonChange(btnPickTime, ApiDataManager.getInstance().getBooking().getTime());
                layout_bacSi.setVisibility(View.VISIBLE);
            }
            if (ApiDataManager.getInstance().getSelectedDoctor() != null) {
                selectedButtonChange(btnPickDoctor, ApiDataManager.getInstance().getSelectedDoctor().getName());
                layout_benhNhan.setVisibility(View.VISIBLE);
            }
            if (ApiDataManager.getInstance().getSelectedpatient() != null) {
                selectedButtonChange(btnPickPatient, ApiDataManager.getInstance().getSelectedpatient().getPatientName());
                btn_next.setClickable(true);
                btn_next.setBackground(getDrawable(R.drawable.rounded_bg_blue));
            }
        }
    }


    private void onClick() {
        ActivityResultLauncher<Intent> secondActivityLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        setUpView();
                    }
                }
        );
        igb_backBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BookingActivity.this, MainActivity.class));
                finish();
            }
        });
        btnPickService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BookingActivity.this, ServiceActivity.class);
                secondActivityLauncher.launch(intent);
            }
        });
        btnPickDepartment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BookingActivity.this, DepartmentActivity.class);
                secondActivityLauncher.launch(intent);
            }
        });
        btn_PickDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BookingActivity.this, CalendarActivity.class);
                secondActivityLauncher.launch(intent);
            }
        });
        btnPickTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BookingActivity.this, TimePickerActivity.class);
                secondActivityLauncher.launch(intent);
            }
        });
        btnPickDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BookingActivity.this, DoctorActivity.class);
                secondActivityLauncher.launch(intent);
            }
        });
        btnPickPatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BookingActivity.this, PatientActivity.class);
                secondActivityLauncher.launch(intent);
            }
        });
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String note = edtNote.getText().toString();
                ApiDataManager.getInstance().getBooking().setNote(note);

            }
        });
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void hideLayout() {
        btn_next.setBackground(getDrawable(R.drawable.rounded_bg_gray));
        btn_next.setClickable(false);
        layout_benhNhan.setVisibility(View.INVISIBLE);
        layout_bacSi.setVisibility(View.INVISIBLE);
        layout_gioKham.setVisibility(View.INVISIBLE);
        layout_ngayKham.setVisibility(View.INVISIBLE);
        layout_ChuyenKhoa.setVisibility(View.INVISIBLE);
    }

    private void initView() {
        userId = ApiDataManager.getInstance().getUser().getId();
        igb_backBooking = findViewById(R.id.igb_backBooking);
        layout_LoaiHinhKham = findViewById(R.id.layout_LoaiHinhKham);
        layout_ChuyenKhoa = findViewById(R.id.layout_ChuyenKhoa);
        layout_ngayKham = findViewById(R.id.layout_ngayKham);
        layout_gioKham = findViewById(R.id.layout_gioKham);
        layout_bacSi = findViewById(R.id.layout_bacSi);
        layout_benhNhan = findViewById(R.id.layout_benhNhan);
        btn_next = findViewById(R.id.btn_next);
        btnPickService = findViewById(R.id.btnPickService);
        btnPickDepartment = findViewById(R.id.btnPickDepartment);
        btn_PickDate = findViewById(R.id.btnPickDate);
        btnPickTime = findViewById(R.id.btnPickTime);
        btnPickDoctor = findViewById(R.id.btnPickDoctor);
        btnPickPatient = findViewById(R.id.btnPickPatient);
        edtNote = findViewById(R.id.edtNote);
    }


    private void selectedButtonChange(TextView btn, String text) {
        btn.setText(text);
        btn.setTextColor(Color.BLACK);
        Log.d("changeButton","Button " + btn.getText());
    }


    @Override
    public void onBackPressed() {
        startActivity(new Intent(BookingActivity.this, MainActivity.class));
        finish();
    }

    @Override
    protected void onDestroy() {
        ApiDataManager.getInstance().setSelectedpatient(null);
        ApiDataManager.getInstance().setSelectedService(null);
        ApiDataManager.getInstance().setSelectedDepartment(null);
        ApiDataManager.getInstance().setSelectedDoctor(null);
        ApiDataManager.getInstance().setBooking(null);
        ApiDataManager.getInstance().setSelectedTime(-1, -1);
        super.onDestroy();

    }
}