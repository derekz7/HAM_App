package com.example.ham_app.activities.doctors;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import com.example.ham_app.R;
import com.example.ham_app.fragments.HomeFragment;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

public class DoctorMainActivity extends AppCompatActivity {
    private ChipNavigationBar chipNavigationBar;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_main);
        initUI();
        if (savedInstanceState == null) {
            chipNavigationBar.setItemSelected(R.id.home, true);
            fragmentManager = getSupportFragmentManager();
            DoctorHomeFragment doctorHomeFragment = new DoctorHomeFragment();
            fragmentManager.beginTransaction().replace(R.id.fragment_container, doctorHomeFragment)
                    .commit();
        }
        setFrameLayout();
    }

    private void setFrameLayout() {
        chipNavigationBar.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public void onItemSelected(int i) {
                Fragment fragment = null;
                if (i == R.id.home) {
                    fragment = new DoctorHomeFragment();
                }
                if (i == R.id.activity) {

                }
                if (fragment != null){
                    fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.fragment_container,fragment).commit();
                }
            }
        });
    }

    private void initUI() {
        chipNavigationBar = findViewById(R.id.bottom_nav);
    }
}