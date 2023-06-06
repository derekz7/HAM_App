package com.example.ham_app.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CalendarView;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.ham_app.R;
import com.example.ham_app.untils.ApiDataManager;

import java.util.Calendar;
import java.util.Date;

public class CalendarActivity extends AppCompatActivity {

    private CalendarView calendarView;
    private Calendar lastSelectedCalendar = null;
    private ImageButton igb_backBooking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        initView();
        lastSelectedCalendar = Calendar.getInstance();
        setUp();
        onClick();
    }

    private void onClick() {
        igb_backBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setUp() {
        calendarView.setMinDate(lastSelectedCalendar.getTimeInMillis());
        calendarView.setFirstDayOfWeek(Calendar.SUNDAY);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, dayOfMonth);
                if (calendar.equals(lastSelectedCalendar)) {
                    Toast.makeText(CalendarActivity.this, "Không thể đặt khám vào hôm nay", Toast.LENGTH_SHORT).show();
                }
                if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                    Toast.makeText(CalendarActivity.this, "Không thể đặt khám vào cuối tuần", Toast.LENGTH_SHORT).show();
                } else {
                    lastSelectedCalendar = calendar;
                    String selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
                    ApiDataManager.getInstance().getBooking().setDate(selectedDate);
                    Log.d("Booking","Date: " + selectedDate);
                    Intent resultIntent = new Intent();
                    setResult(RESULT_OK, resultIntent);
                    finish();
                }
            }
        });
    }

    private void initView() {
        calendarView = findViewById(R.id.calendarView);
        igb_backBooking = findViewById(R.id.igb_backBooking);

    }
}