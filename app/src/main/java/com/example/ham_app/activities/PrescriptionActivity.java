package com.example.ham_app.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.ham_app.R;
import com.example.ham_app.modules.Prescription;
import com.example.ham_app.untils.ApiDataManager;

import java.util.ArrayList;
import java.util.List;

public class PrescriptionActivity extends AppCompatActivity {

    private ImageButton igb_backPrescription,igb_tips;
    private ConstraintLayout layout_notificationPre;
    private RecyclerView rec_prescriptions;
    private List<Prescription> prescriptionList;
    private TextView textViewTips;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prescription);
        initView();
        loadData();

        onClick();
    }

    private void loadData() {
        if (ApiDataManager.getInstance().getPrescriptionList() != null){
            layout_notificationPre.setVisibility(View.INVISIBLE);
            prescriptionList = ApiDataManager.getInstance().getPrescriptionList();

        }else {
            layout_notificationPre.setVisibility(View.VISIBLE);
        }
    }

    private void onClick() {
        igb_backPrescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        igb_tips.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (textViewTips.getVisibility() == View.INVISIBLE){
                    textViewTips.setVisibility(View.VISIBLE);
                }else {
                    textViewTips.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    private void initView() {
        igb_backPrescription = findViewById(R.id.igb_backPrescription);
        layout_notificationPre = findViewById(R.id.layout_notificationPre);
        rec_prescriptions = findViewById(R.id.rec_prescription);
        igb_tips = findViewById(R.id.igb_tips);
        textViewTips = findViewById(R.id.textViewTips);
        prescriptionList = new ArrayList<>();
    }
}