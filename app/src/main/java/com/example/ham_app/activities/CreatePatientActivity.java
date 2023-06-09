package com.example.ham_app.activities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreatePatientActivity extends AppCompatActivity {

    private ImageButton igb_backCreatePt;
    private EditText edtPatientName, edtJob, edtAddress;
    private TextView btnPickDate;
    private Patient patient = new Patient();
    private RadioButton rdMale, rdFemale;
    final Calendar myCalendar = Calendar.getInstance();
    private Button btn_CreatePt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_patient);
        initView();
        onClick();

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
        igb_backCreatePt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnPickDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(CreatePatientActivity.this,R.style.dialogDatePicker ,date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
                dialog.show();
            }
        });

        rdMale.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    patient.setGender("Nam");
                }
            }
        });
        rdFemale.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    patient.setGender("Nữ");
                }
            }
        });

        btn_CreatePt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UUID uuid = UUID.randomUUID();
                String id = "pt-" + uuid.toString().substring(0, 17);
                patient.setId(id);
                String name = edtPatientName.getText().toString();
                if (name.length() < 10) {
                    edtPatientName.setError("Vui lòng nhập họ tên đầy đủ của bạn");
                } else {
                    patient.setDob(btnPickDate.getText().toString());
                    patient.setPatientName(name);
                    patient.setAddress(edtAddress.getText().toString());
                    patient.setJob(edtJob.getText().toString());
                    patient.setUser_id(ApiDataManager.getInstance().getUser().getId());
                    LoadingDialog.show(CreatePatientActivity.this);
                    ApiService.api.createPatient(patient).enqueue(new Callback<Patient>() {
                        @Override
                        public void onResponse(Call<Patient> call, Response<Patient> response) {
                            if (response.body() != null) {
                                ApiDataManager.getInstance().getPatientList().add(response.body());
                                Toast.makeText(CreatePatientActivity.this, "Thêm hồ sơ đặt khám thành công", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(CreatePatientActivity.this, MainActivity.class);
                                intent.putExtra("FRAGMENT_TAG", "Profile");
                                setResult(111, intent);
                                startActivity(intent);
                                finish();
                            }
                            LoadingDialog.dismissDialog();
                        }

                        @Override
                        public void onFailure(Call<Patient> call, Throwable t) {
                            startActivity(new Intent(CreatePatientActivity.this, ErrorActivity.class));
                            LoadingDialog.dismissDialog();
                        }
                    });
                }
            }
        });
    }

    private void initView() {
        igb_backCreatePt = findViewById(R.id.igb_backCreatePtE);
        edtPatientName = findViewById(R.id.edtPatientName);
        edtJob = findViewById(R.id.edtJob);
        edtAddress = findViewById(R.id.edtAddress);
        btnPickDate = findViewById(R.id.btnPickDate1);
        rdMale = findViewById(R.id.rd_Male);
        rdFemale = findViewById(R.id.rd_Female);
        btn_CreatePt = findViewById(R.id.btn_CreatePt);
    }

    @SuppressLint("SimpleDateFormat")
    private void updateLabel() {
        String myFormat = "dd/MM/yyyy";
        SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat);
        btnPickDate.setText(dateFormat.format(myCalendar.getTime()));
        btnPickDate.setTextColor(Color.BLACK);
    }
}