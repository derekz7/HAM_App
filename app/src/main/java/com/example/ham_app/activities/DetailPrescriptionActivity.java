package com.example.ham_app.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.ham_app.R;
import com.example.ham_app.modules.Prescription;

public class DetailPrescriptionActivity extends AppCompatActivity {
    private Prescription prescription;
    private TextView tv_Pre_PtNameCT,tv_diseaseCT,tvSymptomsCT,tvMedicinesCT,tvProcedureCT,tvDoctorNameCT,tv_PreID;
    private ImageButton igb_backCTPre;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_prescription);
        prescription = (Prescription) getIntent().getSerializableExtra("prescription");
        initUI();
        setupView();
        onClick();
    }

    private void onClick() {
        igb_backCTPre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setupView() {
        if (prescription != null){
            tv_Pre_PtNameCT.setText(prescription.getPtName());
            tv_diseaseCT.setText(prescription.getDisease());
            tvSymptomsCT.setText(prescription.getSymptoms());
            tvMedicinesCT.setText(prescription.getMedicines());
            tvProcedureCT.setText(prescription.getPtu_medicines());
            tvDoctorNameCT.setText(prescription.getDcName());
            tv_PreID.setText(prescription.getId());
        }
    }

    private void initUI() {
        igb_backCTPre = findViewById(R.id.igb_backCTPre);
        tv_Pre_PtNameCT = findViewById(R.id.tv_Pre_PtNameCT);
        tv_diseaseCT = findViewById(R.id.tv_diseaseCT);
        tvSymptomsCT = findViewById(R.id.tvSymptomsCT);
        tvMedicinesCT = findViewById(R.id.tvMedicinesCT);
        tvProcedureCT = findViewById(R.id.tvProcedureCT);
        tvDoctorNameCT = findViewById(R.id.tvDoctorNameCT);
        tv_PreID = findViewById(R.id.tv_PreID);
    }
}