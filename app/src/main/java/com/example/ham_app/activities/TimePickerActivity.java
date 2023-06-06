package com.example.ham_app.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.ham_app.R;
import com.example.ham_app.adapters.TimeAdapter;
import com.example.ham_app.untils.ApiDataManager;

import java.util.ArrayList;
import java.util.HashMap;

public class TimePickerActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ArrayList<String> sectionList = new ArrayList<>();
    private HashMap<String, ArrayList<String>> itemList = new HashMap<>();
    private TimeAdapter adapter;
    private ImageButton igb_backTimePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_picker);
        initView();
        sectionSetUp();
        onClick();
    }

    private void onClick() {
        igb_backTimePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void sectionSetUp() {
        sectionList.add(getString(R.string.morning));
        sectionList.add(getString(R.string.afternoon));
        ArrayList<String> timeList = new ArrayList<>();
        timeList.add("08:00 - 08:30");
        timeList.add("08:30 - 09:00");
        timeList.add("09:00 - 09:30");
        timeList.add("09:30 - 10:00");
        timeList.add("10:30 - 11:00");
        timeList.add("11:00 - 11:30");
        itemList.put(sectionList.get(0), timeList);

        timeList = new ArrayList<>();
        timeList.add("13:00 - 13:30");
        timeList.add("13:30 - 14:00");
        timeList.add("14:00 - 14:30");
        timeList.add("14:30 - 15:00");
        timeList.add("15:00 - 15:30");
        timeList.add("15:30 - 16:00");
        timeList.add("16:00 - 16:30");
        itemList.put(sectionList.get(1), timeList);

        if (ApiDataManager.getInstance().getSelectedTime() != null){
            int selectedSection = ApiDataManager.getInstance().getSelectedTime()[0];
            int selectedItem = ApiDataManager.getInstance().getSelectedTime()[1];
            adapter = new TimeAdapter(this,sectionList,itemList,selectedSection,selectedItem);
        }else {
            adapter = new TimeAdapter(this,sectionList,itemList,-1,-1);
        }
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,3);
        recyclerView.setLayoutManager(gridLayoutManager);
        adapter.setLayoutManager(gridLayoutManager);
        adapter.shouldShowHeadersForEmptySections(false);
        recyclerView.setAdapter(adapter);
    }

    private void initView() {
        recyclerView = findViewById(R.id.rec_time);
        igb_backTimePicker = findViewById(R.id.igb_backTimePicker);
    }
}