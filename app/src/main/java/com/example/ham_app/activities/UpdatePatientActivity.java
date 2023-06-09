package com.example.ham_app.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ham_app.R;
import com.example.ham_app.api.ApiService;
import com.example.ham_app.dialog.LoadingDialog;
import com.example.ham_app.modules.Patient;
import com.example.ham_app.untils.ApiDataManager;
import com.google.android.gms.common.api.Api;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdatePatientActivity extends AppCompatActivity {

    private EditText edtPatientName, edtJob, edtAddress;
    private TextView btnPickDate;
    private RadioButton rdMale, rdFemale;
    final Calendar myCalendar = Calendar.getInstance();
    private Button btn_UpdatePt;
    private ImageButton igb_backUpdatePt;
    private Patient patient;
    private int pos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_patient);
        patient = (Patient) getIntent().getSerializableExtra("patient");
        pos = getIntent().getIntExtra("pos", 0);
        initView();
        setUpView();
        onClick();
    }

    private void setUpView() {
        btnPickDate.setTextColor(Color.BLACK);
        btnPickDate.setText(patient.getDob());
        edtPatientName.setText(patient.getPatientName());
        edtAddress.setText(patient.getAddress() != null ? patient.getAddress() : "");
        edtJob.setText(patient.getJob() != null ? patient.getJob() : "");
    }

    private void onClick() {
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        igb_backUpdatePt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnPickDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(UpdatePatientActivity.this, R.style.dialogDatePicker, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
                dialog.show();
            }
        });

        rdMale.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    patient.setGender("Nam");
                }
            }
        });
        rdFemale.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    patient.setGender("Nữ");
                }
            }
        });

        btn_UpdatePt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = edtPatientName.getText().toString();
                if (name.length() < 10) {
                    edtPatientName.setError("Vui lòng nhập họ tên đầy đủ của bạn");
                } else {
                    patient.setDob(btnPickDate.getText().toString());
                    patient.setPatientName(name);
                    patient.setAddress(edtAddress.getText().toString());
                    patient.setJob(edtJob.getText().toString());
                    LoadingDialog.show(UpdatePatientActivity.this);
                    ApiService.api.updatePatient(patient.getId(), patient).enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if (response.isSuccessful()) {
                                ApiDataManager.getInstance().getPatientList().set(pos, patient);
                                Toast.makeText(UpdatePatientActivity.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                                Intent resultIntent = new Intent();
                                LoadingDialog.dismissDialog();
                                setResult(111, resultIntent);
                                finish();
                            } else {
                                LoadingDialog.dismissDialog();
                                Toast.makeText(UpdatePatientActivity.this, "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            LoadingDialog.dismissDialog();
                            startActivity(new Intent(UpdatePatientActivity.this, ErrorActivity.class));
                        }
                    });
                }
            }
        });
    }

    private void initView() {
        edtPatientName = findViewById(R.id.edtPatientNameE);
        edtJob = findViewById(R.id.edtJobE);
        edtAddress = findViewById(R.id.edtAddressE);
        btnPickDate = findViewById(R.id.btnPickDateE);
        rdMale = findViewById(R.id.rd_MaleE);
        rdFemale = findViewById(R.id.rd_FemaleE);
        btn_UpdatePt = findViewById(R.id.btn_UpdatePt);
        igb_backUpdatePt = findViewById(R.id.igb_backUpdatePt);
    }

    @SuppressLint("SimpleDateFormat")
    private void updateLabel() {
        String myFormat = "dd/MM/yyyy";
        SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat);
        btnPickDate.setText(dateFormat.format(myCalendar.getTime()));
    }
}