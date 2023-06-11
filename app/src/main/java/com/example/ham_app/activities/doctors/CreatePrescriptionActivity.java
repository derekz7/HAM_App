package com.example.ham_app.activities.doctors;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ham_app.R;
import com.example.ham_app.activities.ErrorActivity;
import com.example.ham_app.api.ApiService;
import com.example.ham_app.dialog.LoadingDialog;
import com.example.ham_app.modules.Appointment;
import com.example.ham_app.modules.Prescription;
import com.example.ham_app.untils.ApiDataManager;

import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreatePrescriptionActivity extends AppCompatActivity {
    private ImageButton igb_backCreatePrescription;
    private TextView tv_Pre_PtName;
    private EditText edtSymptoms, edtDisease, edtMedicines, edtProcedure;
    private Button btnSubmit;
    private Appointment appointment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_prescription);
        initUI();
        appointment = (Appointment) getIntent().getSerializableExtra("appointment");
        setupView();
        onClick();
    }

    private void onClick() {
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String symptoms = edtSymptoms.getText().toString();
                String disease = edtDisease.getText().toString();
                String medicines = edtMedicines.getText().toString();
                String procedure = edtProcedure.getText().toString();
                if (symptoms.length() == 0 || disease.length() == 0 || medicines.length() == 0 || procedure.length() == 0) {
                    Toast.makeText(CreatePrescriptionActivity.this, "Vui lòng nhập đủ các trường", Toast.LENGTH_SHORT).show();
                } else {
                    UUID uuid = UUID.randomUUID();
                    String id = "PR-" + uuid.toString().substring(0,17);
                    Prescription prescription = new Prescription(id,disease,symptoms,medicines,procedure,appointment.getUid(), ApiDataManager.getInstance().getUser().getId(),appointment.getPtName());

                    LoadingDialog.show(CreatePrescriptionActivity.this);
                    ApiService.api.createPrescription(prescription).enqueue(new Callback<Prescription>() {
                        @Override
                        public void onResponse(Call<Prescription> call, Response<Prescription> response) {
                            if (response.body() != null){
                                Toast.makeText(CreatePrescriptionActivity.this, "Tạo đơn thuốc thành công", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }

                        @Override
                        public void onFailure(Call<Prescription> call, Throwable t) {
                            startActivity(new Intent(CreatePrescriptionActivity.this, ErrorActivity.class));
                        }
                    });

                }
            }
        });
    }

    private void setupView() {
        tv_Pre_PtName.setText(appointment.getPtName());
    }

    private void initUI() {
        igb_backCreatePrescription = findViewById(R.id.igb_backCreatePrescription);
        tv_Pre_PtName = findViewById(R.id.tv_Pre_PtName);
        edtSymptoms = findViewById(R.id.edtSymptoms);
        edtDisease = findViewById(R.id.edtDisease);
        edtMedicines = findViewById(R.id.edtMedicines);
        edtProcedure = findViewById(R.id.edtProcedure);
        btnSubmit = findViewById(R.id.btn_CreatePrescription);
    }
}