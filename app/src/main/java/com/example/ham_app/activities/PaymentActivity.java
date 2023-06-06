package com.example.ham_app.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.ham_app.R;
import com.example.ham_app.untils.Common;

public class PaymentActivity extends AppCompatActivity {

    private ImageButton igb_backPayment;
    private LinearLayout linear_paymentCash;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        initView();
        onClick();
    }

    private void onClick() {
        igb_backPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        linear_paymentCash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Common.selectedPayment = "cash";
                Log.d("Payment", Common.selectedPayment);
                Intent resultIntent = new Intent();
                setResult(123, resultIntent);
                finish();
            }
        });
    }

    private void initView() {
        igb_backPayment = findViewById(R.id.igb_backPayment);
        linear_paymentCash = findViewById(R.id.linear_paymentCash);
    }
}