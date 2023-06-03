package com.example.ham_app.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.CalendarView;
import android.widget.Toast;

import com.example.ham_app.R;

import java.util.Calendar;
import java.util.Date;

public class CalendarActivity extends AppCompatActivity {

    private CalendarView calendarView;
    private Calendar lastSelectedCalendar = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        initView();
        lastSelectedCalendar = Calendar.getInstance();
        setUp();
    }

    private void setUp() {
        calendarView.setMinDate(lastSelectedCalendar.getTimeInMillis());
        calendarView.setFirstDayOfWeek(Calendar.SUNDAY);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(year,month,dayOfMonth);
                if (calendar.equals(lastSelectedCalendar)){
                    Toast.makeText(CalendarActivity.this, "Không thể đặt khám vào hôm nay", Toast.LENGTH_SHORT).show();
                }
                if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY){
                    Toast.makeText(CalendarActivity.this, "Không thể đặt khám vào cuối tuần", Toast.LENGTH_SHORT).show();
                }else {
                    lastSelectedCalendar = calendar;
                }
            }
        });
    }

    private void initView() {
        calendarView = findViewById(R.id.calendarView);

    }
}