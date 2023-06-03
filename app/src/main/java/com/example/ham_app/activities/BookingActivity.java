package com.example.ham_app.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.ham_app.R;

import java.util.Calendar;

public class BookingActivity extends AppCompatActivity {
    private ImageButton igb_backBooking;
    private LinearLayout layout_LoaiHinhKham, layout_ChuyenKhoa, layout_ngayKham, layout_gioKham, layout_bacSi, layout_benhNhan;
    private Button btn_next,btnChonLoaiKham,btn_ChonNgayKham;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);
        initView();
        onClick();
    }

    private void onClick() {
        btnChonLoaiKham.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BookingActivity.this,ServiceActivity.class));
                finish();
            }
        });
        igb_backBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BookingActivity.this, MainActivity.class));
                finish();
            }
        });
        btn_ChonNgayKham.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(BookingActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        Toast.makeText(BookingActivity.this, ""+ dayOfMonth, Toast.LENGTH_SHORT).show();
                    }
                },2023,0,15);
                dialog.show();
            }
        });
    }

    private void initView() {
        igb_backBooking = findViewById(R.id.igb_backBooking);
        layout_LoaiHinhKham = findViewById(R.id.layout_LoaiHinhKham);
        layout_ChuyenKhoa = findViewById(R.id.layout_ChuyenKhoa);
        layout_ngayKham = findViewById(R.id.layout_ngayKham);
        layout_gioKham = findViewById(R.id.layout_gioKham);
        layout_bacSi = findViewById(R.id.layout_bacSi);
        layout_benhNhan = findViewById(R.id.layout_benhNhan);
        btn_next = findViewById(R.id.btn_next);
        btnChonLoaiKham = findViewById(R.id.btnChonLoaiKham);
        btn_ChonNgayKham = findViewById(R.id.btn_ChonNgayKham);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(BookingActivity.this, MainActivity.class));
        finish();
    }
}